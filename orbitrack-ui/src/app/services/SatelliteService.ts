import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Satellite } from './Satellite';

export interface SatelliteDetails {
    id?: number;
    name?: string;
    objectId?: string;
    kind?: number;
    operator?: string;
    contractors?: string;
    equipment?: string;
    configuration?: string;
    propulsion?: string;
    power?: string;
    lifetime?: string;
    mass?: string;
    orbit?: string;
    hasImage?: boolean;
}

@Injectable({
    providedIn: 'root',
})
export class SatelliteService {
    private searchUrl = 'http://localhost:8080/api/satellites';

    constructor(private http: HttpClient) {}

    getSatellite(satellite: Satellite): Observable<SatelliteDetails> {
        let params = new HttpParams();
        if (satellite.objectId) {
            params = params.set('objectId', satellite.objectId);
        }
        if (satellite.name) {
            params = params.set('satelliteName', satellite.name);
        }
        return this.http.get<SatelliteDetails>(this.searchUrl, { params });
    }
}

