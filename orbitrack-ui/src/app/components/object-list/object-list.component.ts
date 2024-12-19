import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Object } from '../../services/search-service';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-object-list',
  imports: [CommonModule, MatButtonModule],
  templateUrl: './object-list.component.html',
  styleUrl: './object-list.component.scss'
})
export class ObjectListComponent {
  @Input() objectSelected: Object[] = [];
  @Output() removeObject  = new EventEmitter<number>();
  @Output() cesiumUpdate = new EventEmitter<Object[]>();

  remove(index: number) {
    this.removeObject.emit(index);
  }

  showInCesium() {
    console.log(this.objectSelected);
    this.cesiumUpdate.emit(this.objectSelected);
  }
}
