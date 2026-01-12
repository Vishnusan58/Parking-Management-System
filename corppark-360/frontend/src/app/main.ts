// ...existing code...
import {provideAnimations} from "@angular/platform-browser/animations";
import {provideHttpClient} from "@angular/common/http";
import {AppComponent} from "./app.component";
import {bootstrapApplication} from "@angular/platform-browser";
import {provideRouter} from "@angular/router";
import {routes} from "./app.routes";

bootstrapApplication(AppComponent, {
  providers: [provideRouter(routes), provideHttpClient(), provideAnimations()]
}).catch(err => {
  console.error('Angular bootstrap failed', err);
  const el = document.createElement('div');
  el.style.color = '#dc2626';
  el.style.padding = '16px';
  el.style.fontFamily = 'monospace';
  el.textContent = 'App failed to start. Check console for details.';
  document.body.prepend(el);
});

