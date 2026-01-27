import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { FormsModule } from '@angular/forms';
import { ScrollingModule } from '@angular/cdk/scrolling';
import { SharedComponentsComponent } from './shared-components.component';
import { TableComponent } from './table/table.component';

@NgModule({
  declarations: [
    SharedComponentsComponent,
    TableComponent
  ],
  imports: [
    CommonModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatSelectModule,
    MatFormFieldModule,
    MatInputModule,
    FormsModule,
    ScrollingModule
  ],
  exports: [
    SharedComponentsComponent,
    TableComponent,
    MatSelectModule,
    MatFormFieldModule,
    MatInputModule,
    FormsModule,
    ScrollingModule
  ]
})
export class SharedComponentsModule { }