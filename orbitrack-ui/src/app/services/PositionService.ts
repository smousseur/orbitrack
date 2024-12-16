import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { GeoPosition } from './GeoPosition';

@Injectable({
    providedIn: 'root'
})

export class PositionService {
  private apiUrl = 'http://localhost:8080/api/objects';
  private eventSource!: EventSource;

    getPositions(objectId: number, time: string, speedClock: number): Observable<GeoPosition> {
        this.eventSource = new EventSource(`${this.apiUrl}/${objectId}/position/stream?time=${time}&speed=${speedClock}`);
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

    isOpened(): boolean {
      return this.eventSource && this.eventSource.readyState == this.eventSource.OPEN;
    }
}
