import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Object {
    id: number;
    noradId: number;    
    name: string;
    cosparId: string;    
    type: string;
    size: string;
    countryCode: string;
    launchDate: string;
}

export interface PageResponse<T> {
    items: T[];
    total: number;
}

@Injectable({
    providedIn: 'root',
})
export class SearchService {
    private searchUrl = 'http://localhost:8080/api/objects/search';

    constructor(private http: HttpClient) {}

    search(query: string = '', page: number = 0, size: number = 10) :  Observable<PageResponse<Object>> {
        let params = new HttpParams()
            .set('name', query)
            .set('page', page)
            .set('size', size);
 //       if (noradId) {
 //           params = params.set('noradId', noradId);
 //       }

        return this.http.get<PageResponse<Object>>(this.searchUrl, { params });
    }
}
