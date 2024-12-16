import { Component, OnInit } from '@angular/core';
import { SatelliteDetails, SatelliteService } from '../../services/SatelliteService';
import { ObjectRefreshService, SatelliteRefreshService } from '../../services/RefreshService';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { defaultIfEmpty, isEmpty, of, switchMap, take, tap } from 'rxjs';

@Component({
  selector: 'app-satellite',
  imports: [CommonModule, MatCardModule, MatFormFieldModule],
  templateUrl: './satellite.component.html',
  styleUrl: './satellite.component.scss'
})
export class SatelliteComponent implements OnInit {
  show: boolean = false;
  satelliteDetails: SatelliteDetails = {};
  hasEmitted = false;
  constructor(private satelliteRefreshService: SatelliteRefreshService, private satelliteService: SatelliteService) {}

  ngOnInit(): void {
    this.satelliteRefreshService.refresh$
    .subscribe(satellite => {
      if (satellite) {
        this.satelliteService.getSatellite(satellite)
        .subscribe(satelliteDetails => {
          if (satelliteDetails) {
            this.show = true;
            this.satelliteDetails = satelliteDetails;
          } else {
            this.show = false;
          }
        });
      }
    });
  }
}
