import { Component } from '@angular/core';
import { CesiumViewerComponent } from "../components/cesium-viewer/cesium-viewer.component";
import { DetailsComponent } from "../components/details/details.component";
import { InfosComponent } from '../components/infos/infos.component';
import { SearchTableComponent } from "../components/search-table/search-table.component";
import { SatelliteComponent } from "../components/satellite/satellite.component";
import { ObjectListComponent } from '../components/object-list/object-list.component';
import { Object } from '../services/search-service';

@Component({
  selector: 'app-layout',
  imports: [CesiumViewerComponent, SearchTableComponent, DetailsComponent, 
    InfosComponent, SearchTableComponent, SatelliteComponent, ObjectListComponent],
  templateUrl: './layout.component.html',
  styleUrl: './layout.component.scss'
})
export class LayoutComponent {
  selectedObjects: Object[] = [];
  objectsToDisplay: Object[] = [];

  addObject(object: Object) {
    if (!this.selectedObjects.includes(object)) {
      this.selectedObjects.push(object);
    }
  }

  removeObject(index: number) {
    this.selectedObjects.splice(index, 1);
  }

  cesiumUpdate(objectsToDisplay: Object[]) {
    this.objectsToDisplay = [... objectsToDisplay];
  }
}
