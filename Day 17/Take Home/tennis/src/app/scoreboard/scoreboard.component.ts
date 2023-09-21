import { Component, HostListener } from '@angular/core';
import { Scoreboard } from '../models/Scoreboard';


@Component({
  selector: 'app-scoreboard',
  templateUrl: './scoreboard.component.html',
  styleUrls: ['./scoreboard.component.css'],
})
export class ScoreboardComponent {
  constructor(public scoreboard: Scoreboard) {}

  player1Scores() {
    this.scoreboard.incrementPoint(1);
  }

  player2Scores() {
    this.scoreboard.incrementPoint(2);
  }

  @HostListener('document:keydown', ['$event'])
  handleKeyboardEvent(event: KeyboardEvent) {
    if (event.key === 'ArrowUp') {
      this.player1Scores();
    } else if (event.key === 'ArrowDown') {
      this.player2Scores();
    }
  }
  
}
