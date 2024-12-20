import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { GeoPosition } from './geo-position';

@Injectable({
    providedIn: 'root'
})

export class PositionService {
  private apiUrl = 'http://localhost:8080/api/objects';
  private eventSource!: EventSource;

    getMultiplePositions(objectIds: number[], time: string, speedClock: number): Observable<GeoPosition> {
      const url = this.apiUrl + '/position/stream?ids=' + objectIds.join() + '&time=' + time + '&speed=' + speedClock;
      this.eventSource = new EventSource(url);
      return new Observable<GeoPosition>((observer) => {
        this.eventSource.onmessage = (event) => {
        observer.next(JSON.parse(event.data));
        };

        this.eventSource.onerror = (error) => {
          observer.error(error);
        };

        this.eventSource.onopen = () => {
          console.log('Multi stream opened');
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
