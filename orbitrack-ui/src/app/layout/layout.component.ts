import { Component } from '@angular/core';
import { CesiumViewerComponent } from "../components/cesium-viewer/cesium-viewer.component";
import { SearchComponent } from "../components/search/search.component";
import { DetailsComponent } from "../components/details/details.component";
import { InfosComponent } from '../components/infos/infos.component';

@Component({
  selector: 'app-layout',
  imports: [CesiumViewerComponent, SearchComponent, DetailsComponent, InfosComponent],
  templateUrl: './layout.component.html',
  styleUrl: './layout.component.scss'
})
export class LayoutComponent {

}
