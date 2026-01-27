import { Component, Input, Output, EventEmitter, OnChanges, SimpleChanges, ViewChild, AfterViewInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatSelectChange } from '@angular/material/select';
import { CdkVirtualScrollViewport } from '@angular/cdk/scrolling';

@Component({
  selector: 'lib-table',
  templateUrl: './table.component.html',
  styleUrls: ['./table.component.scss']
})
export class TableComponent implements OnChanges, AfterViewInit {
  @Input() data: any[] = [];
  @Input() displayedColumns: string[] = [];
  @Input() pageSize: number = 50;
  @Input() pageIndex: number = 0;
  @Input() totalItems: number = 0;
  @Input() pageSizeOptions: number[] = [50, 100, 150];

  @Output() pageChange = new EventEmitter<PageEvent>();

  dataSource = new MatTableDataSource<any>();
  columns: string[] = [];
  isVirtualScroll: boolean = false;
  selectedPageSize: number | 'ALL' = 50;
  
  virtualPageSize = 150;
  virtualBuffer = 750; // 5 * 150 (Load 5 pages ahead)
  isLoading = false;
  currentScrollIndex = 0;
  fetchedItems: number = 0; // Track number of items loaded in virtual scroll

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;
  @ViewChild(CdkVirtualScrollViewport) viewport!: CdkVirtualScrollViewport;

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['data'] && this.data) {
      this.dataSource.data = this.data;
      this.fetchedItems = this.data.length;
      this.isLoading = false; 
      
      if (!this.displayedColumns.length && this.data.length > 0) {
        this.columns = Object.keys(this.data[0]);
      } else {
        this.columns = this.displayedColumns;
      }
      
      if (this.isVirtualScroll) {
        if (this.viewport) {
            this.viewport.checkViewportSize();
        }
        this.checkBuffer();
      }
    }
    if (changes['displayedColumns']) {
      this.columns = this.displayedColumns;
    }
    if (changes['pageSize']) {
      if (this.isVirtualScroll && this.pageSize === this.virtualPageSize) {
        this.selectedPageSize = 'ALL';
      } else {
        this.selectedPageSize = this.pageSize;
        this.isVirtualScroll = false;
      }
    }
  }

  ngAfterViewInit(): void {
    this.dataSource.sort = this.sort;
  }

  trackBySequence(_: number, row: any): number {
    return row.sequenceNumber;
  }


  onPageChange(event: PageEvent) {
    this.pageSize = event.pageSize;
    this.pageIndex = event.pageIndex;
    this.selectedPageSize = event.pageSize;
    this.pageChange.emit({
      pageIndex: this.pageIndex,
      pageSize: this.pageSize,
      length: this.totalItems,
    } as PageEvent);
  }

  onPageSizeSelectChange(event: MatSelectChange) {
    if (event.value === 'ALL') {
      this.isVirtualScroll = true;
      this.selectedPageSize = 'ALL';
      this.currentScrollIndex = 0;
      this.isLoading = true;

      // Request first batch
      this.pageChange.emit({
        pageIndex: 0,
        pageSize: this.virtualPageSize,
        length: this.totalItems,
      } as PageEvent);
    } else {
      this.isVirtualScroll = false;
      this.selectedPageSize = event.value;
      this.pageChange.emit(
        {
          pageIndex: 0,
          pageSize: event.value,
          length: this.totalItems,
        } as PageEvent);
    }
  }

  onScrollIndexChange(index: number) {
    this.currentScrollIndex = index;
    this.checkBuffer();
  }

  checkBuffer() {
    if (!this.isVirtualScroll || this.isLoading) return;

    const currentLength = this.dataSource.data.length;

    if (this.totalItems > 0 && currentLength >= this.totalItems) return;

    if (currentLength - this.currentScrollIndex < this.virtualBuffer) {
      const nextPage = Math.floor(currentLength / this.virtualPageSize);
      
      this.isLoading = true;
      this.pageChange.emit({
            pageIndex: nextPage,
            pageSize: this.virtualPageSize,
            length: this.totalItems,
        } as PageEvent);
    }
  }
}