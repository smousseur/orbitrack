import { Component, OnInit } from '@angular/core';
import { PositionRefreshService } from '../../services/RefreshService';

@Component({
  selector: 'app-infos',
  imports: [],
  templateUrl: './infos.component.html',
  styleUrl: './infos.component.scss'
})
export class InfosComponent implements OnInit {
  constructor(private positionRefreshService: PositionRefreshService) {}
  infos: Infos = {};
  ngOnInit(): void {
    this.positionRefreshService.refresh$.subscribe(position => {
      this.infos.latitude = `${position?.latitude.toFixed(2) ?? 0}°`;
      this.infos.longitude = `${position?.longitude.toFixed(2) ?? 0}°`;
      const altitude = (position?.altitude ?? 0) / 1000;
      this.infos.altitude = `${altitude.toFixed(2)} km`;
      this.infos.speed = `${position?.speed.toFixed(2) ?? 0} m/s`
    });
  }
}

interface Infos {
  latitude?: string;
  longitude?: string;
  altitude?: string;
  speed?: string;
}
