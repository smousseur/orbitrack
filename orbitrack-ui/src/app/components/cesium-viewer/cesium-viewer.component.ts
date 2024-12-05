import { Component } from '@angular/core';
import { createWorldTerrainAsync, Viewer, Cartesian3, Color } from 'cesium';
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
    
  constructor(private positionService: PositionService, private cesiumRefreshService: CesiumRefreshService,
    private positionRefreshService: PositionRefreshService) {}
  

  async ngAfterViewInit() {
    this.viewer = new Viewer('cesiumContainer', {
      terrainProvider: await createWorldTerrainAsync(),
    });
    this.cesiumRefreshService.refresh$.subscribe(objectId => {
      if (objectId) {
        this.startPositionStream(objectId);
      }
    })

  }

  ngOnDestroy(): void {
    this.clear();
  }

  private startPositionStream(objectId: number) : void {
    this.clear();
    this.positionService.getPositions(objectId).subscribe({
      next: (position) => {
        this.updateSatellitePosition(position);
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

  private updateSatellitePosition(position: GeoPosition): void {
    if (typeof this.entity === 'undefined') {
      this.entity = this.addObjectPoint(position.longitude, position.latitude, position.altitude);
    } else {
      this.entity.position = Cartesian3.fromDegrees(position.longitude, position.latitude, position.altitude);
    }
    this.positionRefreshService.triggerRefresh(position);
    //console.log(`speed = ${position.speed}`);
  }
}
