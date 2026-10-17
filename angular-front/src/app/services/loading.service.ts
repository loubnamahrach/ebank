import { Injectable, signal, WritableSignal } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class LoadingService {
  private loading: WritableSignal<boolean> = signal<boolean>(false);
  public isLoading = this.loading.asReadonly();

  public setLoading(value: boolean): void {
    this.loading.set(value);
  }

  public setLoaded(): void {
    this.loading.set(true);
  }
}
