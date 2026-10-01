import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import {
  Container,
  CreateContainerRequest
} from '../models/container.model';

import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ContainerService {

  private readonly http = inject(HttpClient);
  private readonly apiUrl = `${environment.apiBaseUrl}/containers`;

  getAll(): Observable<Container[]> {
    return this.http.get<Container[]>(this.apiUrl);
  }

  getById(id: number): Observable<Container> {
    return this.http.get<Container>(`${this.apiUrl}/${id}`);
  }

  create(request: CreateContainerRequest): Observable<Container> {
    return this.http.post<Container>(this.apiUrl, request);
  }
}
