import { bootstrapApplication } from '@angular/platform-browser';
import { provideRouter } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
import { provideAnimations } from '@angular/platform-browser/animations';
import { AppComponent } from './app/app.component';
import { routes } from './app/app.routes';

bootstrapApplication(AppComponent, {
  providers: [provideRouter(routes), provideHttpClient(), provideAnimations()]
}).catch(err => {
  const errDetails = {
    message: (err as any)?.message,
    ngErrorCode: (err as any)?.ngErrorCode ?? (err as any)?.ɵngErrorCode,
    stack: (err as any)?.stack
  };
  console.error('Angular bootstrap failed', errDetails, err);
  const el = document.createElement('div');
  el.style.color = '#dc2626';
  el.style.padding = '16px';
  el.style.fontFamily = 'monospace';
  el.style.whiteSpace = 'pre-wrap';
  el.textContent = 'App failed to start. Details:\n' + JSON.stringify(errDetails, null, 2);
  document.body.prepend(el);
});
