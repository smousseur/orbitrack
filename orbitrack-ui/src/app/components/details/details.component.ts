import { Component } from '@angular/core';
import { ObjectRefreshService, CesiumRefreshService, PositionRefreshService, SatelliteRefreshService } from '../../services/RefreshService';
import { ObjectDetails, SimpleSearchService } from '../../services/SimpleSearchService';
import { SatelliteDetails } from '../../services/SatelliteService';

@Component({
  selector: 'app-details',
  imports: [],
  templateUrl: './details.component.html',
  styleUrl: './details.component.scss'
})
export class DetailsComponent {
  details: ObjectDetails = {};

  constructor(private searchService: SimpleSearchService, private objectRefreshService: ObjectRefreshService, 
    private cesiumRefreshService: CesiumRefreshService, private satelliteRefreshService: SatelliteRefreshService) {}

  ngOnInit(): void {
    this.objectRefreshService.refresh$.subscribe(objectId => {
      if (objectId) {
        this.searchService.getDetails(objectId).subscribe(details => {
          this.update(details);
        });
      }
    })
  }

  update(details: ObjectDetails) : void {
    this.details = details;
  }

  onClick(): void {
    const that = this;
    this.cesiumRefreshService.triggerRefresh(this.details.id);
    this.satelliteRefreshService.triggerRefresh({objectId: this.details.id, name: this.details.name});
  }
}
