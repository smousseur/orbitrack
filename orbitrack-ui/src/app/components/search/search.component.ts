import { Component, OnInit } from '@angular/core';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { MatAutocompleteModule, MatAutocompleteSelectedEvent } from '@angular/material/autocomplete';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { SearchResult, SearchService } from '../../services/SearchService';
import { debounceTime, distinctUntilChanged, of, switchMap } from 'rxjs';
import { CommonModule } from '@angular/common';
import { ObjectRefreshService } from '../../services/RefreshService';

@Component({
  selector: 'app-search',
  imports: [CommonModule, MatAutocompleteModule, MatFormFieldModule, MatInputModule, ReactiveFormsModule],
  templateUrl: './search.component.html',
  styleUrl: './search.component.scss'
})
export class SearchComponent implements OnInit {
  searchControl = new FormControl('');
  filteredOptions: {objectId: string; label: string}[] = [];

  constructor(private searchService: SearchService, private refreshService: ObjectRefreshService) {}

  ngOnInit(): void {
    this.searchControl.valueChanges
      .pipe(
        debounceTime(300),
        distinctUntilChanged(),
        switchMap((query) => {
          if (query && typeof query == 'string') {
            return this.searchService.search(query);
          } else {
            return of([]);
          }
        })
      )
      .subscribe((results) => {
        this.filteredOptions = results;
    });
  }

  displayLabel(option: { objectId: string; label: string } | null): string {
    return option ? option.label : '';
  }

  onOptionSelected(event: MatAutocompleteSelectedEvent): void {
    const selectedOption = event.option.value;
    this.refreshService.triggerRefresh(selectedOption.objectId);
  }  
}
