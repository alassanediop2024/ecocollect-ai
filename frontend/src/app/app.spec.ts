import { TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';

import { App } from './app';

describe('App', () => {

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [App],
      providers: [
        provideRouter([])
      ]
    }).compileComponents();
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(App);
    const app = fixture.componentInstance;

    expect(app).toBeTruthy();
  });

  it('should render EcoCollect AI branding', async () => {
    const fixture = TestBed.createComponent(App);

    fixture.detectChanges();
    await fixture.whenStable();

    const compiled = fixture.nativeElement as HTMLElement;

    expect(compiled.textContent).toContain('EcoCollect AI');
    expect(compiled.textContent).toContain('Gestion municipale');
  });

  it('should render the main navigation', async () => {
    const fixture = TestBed.createComponent(App);

    fixture.detectChanges();
    await fixture.whenStable();

    const compiled = fixture.nativeElement as HTMLElement;

    expect(compiled.textContent).toContain('Tableau de bord');
    expect(compiled.textContent).toContain('Contenants');
    expect(compiled.textContent).toContain('Collectes');
    expect(compiled.textContent).toContain('Anomalies');
    expect(compiled.textContent).toContain('Secteurs');
  });

});
