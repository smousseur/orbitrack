import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { GeoPosition } from './GeoPosition';

@Injectable({ providedIn: 'root' })
export class RefreshService<T> {
  private refreshSubject = new BehaviorSubject<T | null>(null);
  refresh$ = this.refreshSubject.asObservable();

  triggerRefresh(data: T) {
    this.refreshSubject.next(data);
  }
}

@Injectable({ providedIn: 'root' })
export class ObjectRefreshService extends RefreshService<any> {}

@Injectable({ providedIn: 'root' })
export class CesiumRefreshService extends RefreshService<number | undefined> {}

@Injectable({ providedIn: 'root' })
export class PositionRefreshService extends RefreshService<GeoPosition | undefined> {}
