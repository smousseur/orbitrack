import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { createWorldTerrainAsync, ScreenSpaceEventHandler, Viewer, Cartesian3, Color, Clock, Ion, ConstantPositionProperty, ScreenSpaceEventType, Cartesian2, ColorMaterialProperty } from 'cesium';
import { PositionService } from '../../services/position-service';
import { CesiumRefreshService, ObjectRefreshService, PositionRefreshService, SatelliteRefreshService } from '../../services/refresh-service';
import { GeoPosition } from '../../services/geo-position';
import { environment } from '../../../environments/environment';
import { OrbitalBody } from './entites';
import { CesiumClockHandler } from './cesium-clock-handler';
import { Object } from '../../services/search-service';
import { CesiumMouseHandler } from './cesium-mouse-handler';
import { CesiumGraphicHandler } from './cesium-graphic-handler';

@Component({
  selector: 'app-cesium-viewer',
  templateUrl: './cesium-viewer.component.html',
  styleUrl: './cesium-viewer.component.scss',
  host: {'class': 'cesium-viewer'}
})
export class CesiumViewerComponent implements OnChanges {
  private viewer!: Viewer;
  private clock!: Clock;
  private bodies = new Map<number, OrbitalBody>();
  private cesiumClockHandler!: CesiumClockHandler;
  private cesiumMouseHandler!: CesiumMouseHandler;
  private cesiumGraphicHandler!: CesiumGraphicHandler;
  private eventHandler!: ScreenSpaceEventHandler;

  @Input() cesiumObjects: Object[] = [];

  constructor(private positionService: PositionService, private cesiumRefreshService: CesiumRefreshService,
    private positionRefreshService: PositionRefreshService, private satelliteRefreshService: SatelliteRefreshService,
    private objectRefreshService: ObjectRefreshService) {}

  async ngAfterViewInit() {
    Ion.defaultAccessToken = environment.cesiumKey;
    this.viewer = new Viewer('cesiumContainer', {
      terrainProvider: await createWorldTerrainAsync(),
    });
    this.cesiumGraphicHandler = new CesiumGraphicHandler(this.viewer);
//    this.viewer.selectionIndicator.viewModel.showSelection = false;
    this.clock = this.viewer.clock;
    this.clock.shouldAnimate = true;
    this.cesiumClockHandler = new CesiumClockHandler(this.clock, this.viewer);
    this.clock.onTick.addEventListener(() => {
      this.cesiumClockHandler.handleClock(
        () => this.restartStream(),
        () => this.stopStream(),
        () => this.startStream()
      );
    });
    this.eventHandler = new ScreenSpaceEventHandler(this.viewer.scene.canvas);
    this.cesiumMouseHandler = new CesiumMouseHandler(this.viewer, this.eventHandler, this.bodies);
    this.cesiumMouseHandler.handleMouseMove(
      (body, position) => {
        if (body?.cesiumTrajectory.polyline) {
          body.cesiumTrajectory.polyline.material = new ColorMaterialProperty(Color.YELLOW);
        }
      },
      (body, position) => {
        if (body?.cesiumTrajectory.polyline) {
          body.cesiumTrajectory.polyline.material = new ColorMaterialProperty(Color.BLUE);
        }
      }
    );
    this.cesiumMouseHandler.handleMouseClick((body, position) => {
      if (body) {
        this.satelliteRefreshService.triggerRefresh({ objectId: body.id, name: body.body.cesiumEntity.name });
        this.objectRefreshService.triggerRefresh(body.id);
        this.positionRefreshService.triggerRefresh(body.body.position);
      }
    });
  }
  
  ngOnChanges(changes: SimpleChanges): void {
    if (this.cesiumObjects.length > 0) {
      const objsToDisplay = this.cesiumObjects.map(obj => obj.id);

      for (const key of this.bodies.keys()) {
        if (!objsToDisplay.includes(key)) {
          this.removeBody(this.bodies.get(key), key);
        }
      }
      this.stopStream();
      this.startEntitesStream(this.cesiumObjects.map(obj => obj.id));
    } else {
      this.clear();
    }
  }

  ngOnDestroy(): void {
    this.clear();
  }

  private removeBody(body: OrbitalBody | undefined, key: number): void {  
    if (body) {
      this.viewer.entities.remove(body.cesiumEntity);
      this.viewer.entities.remove(body.cesiumTrajectory);  
    }
    this.bodies.delete(key);
  }

  private startStream(): void {
    if (!this.positionService.isOpened() && this.bodies.size > 0) {
      this.startEntitesStream(Array.from(this.bodies.keys()));
    }
  }

  private stopStream(): void {
    this.positionService.close();
  }

  private restartStream(): void {
    this.stopStream();
    this.startStream();
  }

  private startEntitesStream(entityIds: number[]): void {
    const speedClock = this.clock.multiplier;
    const time = this.cesiumClockHandler.getIsoClockTime();
    this.positionService.getMultiplePositions(entityIds, time, speedClock).subscribe({
      next: (position) => {
        this.updateEntity(position);
      },
      error: (error) => {
        console.error('Error fetching objet positions', error);
      }
    });
  }
  
  private updateEntity(position: GeoPosition): void {
    let body = this.bodies.get(position.objectId);
    const cartesianPos = Cartesian3.fromDegrees(position.longitude, position.latitude, position.altitude);
    const pos = new ConstantPositionProperty(cartesianPos);
    this.positionRefreshService.triggerRefresh(this.cesiumMouseHandler.selectedBody?.position);
    if (body) {
      body.cesiumEntity.position = pos;
      body.position = position;
      body.addTrajectoryPoint(cartesianPos);
    } else {
      const entity = this.cesiumGraphicHandler.createPoint(position.objectName, cartesianPos, Color.RED, 10);
      let trajectoryPoints: Array<Cartesian3> = [];
      const trajectoryEntity = this.cesiumGraphicHandler.createPolyline(Color.BLUE, trajectoryPoints);
      this.bodies.set(position.objectId, new OrbitalBody(entity, trajectoryEntity, trajectoryPoints, position));
    }
  }

  private clear(): void {
    if (this.viewer) {
      this.viewer.entities.removeAll();
    }
    this.bodies.clear();
    this.positionService.close();
  }
}
