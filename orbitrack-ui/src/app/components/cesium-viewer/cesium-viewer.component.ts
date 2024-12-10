import { Component } from '@angular/core';
import { createWorldTerrainAsync, Viewer, Cartesian3, Color, JulianDate, ClockViewModel, Clock, ScreenSpaceEventHandler, ScreenSpaceEventType, PolylineCollection } from 'cesium';
import { PositionService } from '../../services/PositionService';
import { CesiumRefreshService, PositionRefreshService } from '../../services/RefreshService';
import { GeoPosition } from '../../services/GeoPosition';


@Component({
  selector: 'app-cesium-viewer',
  templateUrl: './cesium-viewer.component.html',
  styleUrl: './cesium-viewer.component.scss',
  host: {'class': 'cesium-viewer'}
})
export class CesiumViewerComponent {
  private viewer: any;
  private entity: any;
  private objectId!: number;
  private polygon: Array<number> = [];
  private maxPolygonPoints: number = 50000;
  constructor(private positionService: PositionService, private cesiumRefreshService: CesiumRefreshService,
    private positionRefreshService: PositionRefreshService) {}
  

  async ngAfterViewInit() {
    this.viewer = new Viewer('cesiumContainer', {
      terrainProvider: await createWorldTerrainAsync(),
    });
    const clock = this.viewer.clock;
    clock.shouldAnimate = true;
    this.cesiumRefreshService.refresh$.subscribe(objectId => {
      if (objectId) {
        this.objectId = objectId;
        this.startPositionStream(objectId);
      }
    });
    let lastMultiplier = clock.multiplier;
    let lastClockTime = clock.currentTime.clone();
    let timeoutId: any = null;
    const that = this;
    clock.onTick.addEventListener(() => {
      const areComparableTime = JulianDate.equalsEpsilon(clock.currentTime, lastClockTime, 5);
        if (clock.multiplier !== lastMultiplier || !areComparableTime) {
            timeoutId = setTimeout(() => {
                if (clock.multiplier !== lastMultiplier) {
                  if (that.objectId) {
                    that.startPositionStream(that.objectId);
                  }
                  lastMultiplier = clock.multiplier;
                }
            }, 500);
        }
        lastClockTime = clock.currentTime;
    });
    var polylines = new PolylineCollection();
    polylines.add({
      show: true,
      positions: Cartesian3.fromDegreesArray(this.polygon),
      width: 2,
      material: Color.RED
    });
    this.viewer.primitives.add(polylines);
    this.viewer.entities.add({
      polyline: {
          positions: Cartesian3.fromDegreesArray(this.polygon),
          width: 2,
          material: Color.RED
      }
    });
  }

  ngOnDestroy(): void {
    this.clear();
  }

  private startPositionStream(objectId: number) : void {
    this.clear();
    const currentTime = this.viewer.clock.currentTime;
    let currentIsoTime: string = JulianDate.toIso8601(currentTime);
    currentIsoTime = currentIsoTime.replace(/\.(\d{3})\d*/, '.$1').replace(/Z$/, '');
    const speedClock = this.viewer.clock.multiplier;
    this.positionService.getPositions(objectId, currentIsoTime, speedClock).subscribe({
      next: (position) => {
        this.updateSatellitePosition(position, this);
      },
      error: (error) => {
        console.error('Error fetching objet positions', error);
      }      
    });
  }

  private clear(): void {
    this.entity = undefined;
    this.viewer.entities.removeAll();
    this.positionService.close();
  }

  private addObjectPoint(longitude: number, latitude: number, altitude: number): void {
    return this.viewer.entities.add({
      position: Cartesian3.fromDegrees(longitude, latitude, altitude),
      point: {
        pixelSize: 10,
        color: Color.RED,
        outlineColor:Color.BLACK,
        outlineWidth: 2,
      },
    });
  }  

  private updateSatellitePosition(position: GeoPosition, context: CesiumViewerComponent): void {
    if (typeof context.entity === 'undefined') {
      context.entity = context.addObjectPoint(position.longitude, position.latitude, position.altitude);
    } else {
      context.entity.position = Cartesian3.fromDegrees(position.longitude, position.latitude, position.altitude);
    }
    if (this.polygon.length < this.maxPolygonPoints) {
      this.polygon.push(position.longitude, position.latitude);
    } else {
      this.polygon.unshift(position.longitude, position.latitude);
      this.polygon.pop();
    }
    context.positionRefreshService.triggerRefresh(position);
  }
}
