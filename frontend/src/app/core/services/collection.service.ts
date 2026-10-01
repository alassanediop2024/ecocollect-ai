import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import {
  Collection,
  CreateCollectionRequest
} from '../models/collection.model';

import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CollectionService {

  private readonly http = inject(HttpClient);
  private readonly apiUrl = `${environment.apiBaseUrl}/collections`;

  getAll(): Observable<Collection[]> {
    return this.http.get<Collection[]>(this.apiUrl);
  }

  getById(id: number): Observable<Collection> {
    return this.http.get<Collection>(`${this.apiUrl}/${id}`);
  }

  create(request: CreateCollectionRequest): Observable<Collection> {
    return this.http.post<Collection>(this.apiUrl, request);
  }
}
