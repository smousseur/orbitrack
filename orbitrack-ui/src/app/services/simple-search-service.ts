import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
    providedIn: 'root',
})
export class SimpleSearchService {
    private searchUrl = 'http://localhost:8080/api/objects';

    constructor(private http: HttpClient) {}

    search(query: string): Observable<{objectId: string, label:string}[]> {
        return this.http.get<{objectId: string, label:string}[]>(`${this.searchUrl}?name=${query}`);
    }

    getDetails(objectId: number): Observable<ObjectDetails> {
        return this.http.get<ObjectDetails>(`${this.searchUrl}/${objectId}`);
    }
}

export interface SearchResult {
    id: string;
    label: string;
}

interface Telemetry {
    period?: number;
    eccentricity?: number;
    inclination?: number;

}
export interface ObjectDetails {
    id?: number;
    name?: string;
    noradId?: number;
    type?: string;
    telemetry?: Telemetry;
}