import { Component } from '@angular/core';
import { ObjectRefreshService, CesiumRefreshService, PositionRefreshService } from '../../services/RefreshService';
import { ObjectDetails, SimpleSearchService } from '../../services/SimpleSearchService';

@Component({
  selector: 'app-details',
  imports: [],
  templateUrl: './details.component.html',
  styleUrl: './details.component.scss'
})
export class DetailsComponent {
  details: ObjectDetails = {};

  constructor(private searchService: SimpleSearchService, private objectRefreshService: ObjectRefreshService, 
    private cesiumRefreshService: CesiumRefreshService) {}

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
    this.cesiumRefreshService.triggerRefresh(this.details.id);
  }
}
