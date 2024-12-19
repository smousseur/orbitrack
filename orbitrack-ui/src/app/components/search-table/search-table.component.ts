import { Component, EventEmitter, OnInit, Output, ViewChild } from '@angular/core';
import { MatTable, MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { Object, SearchService } from '../../services/search-service';
import { MatInputModule } from '@angular/material/input';
import { ObjectRefreshService } from '../../services/refresh-service';

@Component({
  selector: 'app-search-table',
  imports: [MatTableModule, MatPaginatorModule, MatInputModule],
  templateUrl: './search-table.component.html',
  styleUrl: './search-table.component.scss'
})
export class SearchTableComponent implements OnInit {
  displayedColumns: string[] = ['name', 'cosparId', 'type', 'launchDate'];
  dataSource = new MatTableDataSource<Object>();
  totalItems = 0;
  pageSize = 10;
  currentPage = 0;
  searchQuery = '';
  searchCosparId?: string;
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  //@ViewChild(MatSort) sort!: MatSort;

  @Output() objectSelected = new EventEmitter<Object>();

  constructor(private searchService: SearchService, private refreshService: ObjectRefreshService) {}

  ngOnInit(): void {
    this.loadObjects();
  }

  search(event: any): void {
    this.searchQuery = event.target.value.toUpperCase().trim();
    this.currentPage = 0;
    this.loadObjects();
  }

  loadObjects(): void {
    this.searchService.search(this.searchQuery, this.currentPage, this.pageSize).subscribe((data) => {
      this.dataSource.data = data.items;
      this.totalItems = data.total;
    });
  }

  loadPage(event: any): void {
    this.pageSize = event.pageSize;
    this.currentPage = event.pageIndex;
    this.loadObjects();
  }

  selectObject(row: Object): void {
    this.refreshService.triggerRefresh(row.id);
    this.objectSelected.emit(row);
  }
}
