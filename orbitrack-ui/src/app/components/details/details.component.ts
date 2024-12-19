import { Component } from '@angular/core';
import { ObjectRefreshService, CesiumRefreshService, PositionRefreshService, SatelliteRefreshService } from '../../services/refresh-service';
import { ObjectDetails, SimpleSearchService } from '../../services/simple-search-service';
import { SatelliteDetails } from '../../services/satellite-service';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';

@Component({
  selector: 'app-details',
  imports: [CommonModule, MatCardModule, MatFormFieldModule],
  templateUrl: './details.component.html',
  styleUrl: './details.component.scss'
})
export class DetailsComponent {
  details: ObjectDetails = {};
  show = false;

  constructor(private searchService: SimpleSearchService, private objectRefreshService: ObjectRefreshService, 
    private cesiumRefreshService: CesiumRefreshService, private satelliteRefreshService: SatelliteRefreshService) {}

  ngOnInit(): void {
    this.objectRefreshService.refresh$.subscribe(objectId => {
      if (objectId) {
        this.searchService.getDetails(objectId).subscribe(details => {
          this.show = true;
          this.update(details);
          this.satelliteRefreshService.triggerRefresh({objectId: details.id, name: details.name});
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
