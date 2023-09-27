import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'forum';

  constructor(private router: Router) { }

  onSubmit(postId: number) {
    this.router.navigate(['/post', postId]);
  }
}
