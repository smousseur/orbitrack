import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { GeoPosition } from './GeoPosition';

@Injectable({
    providedIn: 'root'
})

export class PositionService {
    private apiUrl = 'http://localhost:8080/api/objects/{objectId}/position/stream';
    private eventSource!: EventSource;

    getPositions(objectId: number): Observable<GeoPosition> {
        this.eventSource = new EventSource(this.apiUrl.replace("{objectId}", objectId.toString()));
        return new Observable<GeoPosition>((observer) => {
            this.eventSource.onmessage = (event) => {
            observer.next(JSON.parse(event.data));
          };
    
          this.eventSource.onerror = (error) => {
            observer.error(error);
          };
    
          this.eventSource.onopen = () => {
            console.log('Stream opened');
          };
        });
    }

    close(): void {
        if (this.eventSource) {
            this.eventSource.close();
        }
    }
}