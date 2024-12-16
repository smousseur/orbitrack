import { Component } from '@angular/core';
import { CesiumViewerComponent } from "../components/cesium-viewer/cesium-viewer.component";
import { DetailsComponent } from "../components/details/details.component";
import { InfosComponent } from '../components/infos/infos.component';
import { SearchTableComponent } from "../components/search-table/search-table.component";
import { SatelliteComponent } from "../components/satellite/satellite.component";

@Component({
  selector: 'app-layout',
  imports: [CesiumViewerComponent, SearchTableComponent, DetailsComponent, 
    InfosComponent, SearchTableComponent, SatelliteComponent],
  templateUrl: './layout.component.html',
  styleUrl: './layout.component.scss'
})
export class LayoutComponent {

}
