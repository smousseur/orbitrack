import { Component, OnInit } from '@angular/core';
import { PositionRefreshService } from '../../services/refresh-service';

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
      const latitude = position?.latitude ?? 0;
      let latDir = latitude < 0 ? 'South' : 'North';
      const longitude = position?.longitude ?? 0;
      let longDir = longitude < 0 ? 'West' : 'East';

      this.infos.latitude = `${Math.abs(latitude).toFixed(2)}° ${latDir}`;
      this.infos.longitude = `${Math.abs(longitude).toFixed(2)}° ${longDir}`;
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
