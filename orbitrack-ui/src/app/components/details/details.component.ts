import { Component } from '@angular/core';
import { ObjectRefreshService, CesiumRefreshService, PositionRefreshService } from '../../services/RefreshService';
import { ObjectDetails, SearchService } from '../../services/SearchService';

@Component({
  selector: 'app-details',
  imports: [],
  templateUrl: './details.component.html',
  styleUrl: './details.component.scss'
})
export class DetailsComponent {
  details: ObjectDetails = {};

  constructor(private searchService: SearchService, private objectRefreshService: ObjectRefreshService, 
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
    console.log(this.details.id);
    this.cesiumRefreshService.triggerRefresh(this.details.id);
  }
}
