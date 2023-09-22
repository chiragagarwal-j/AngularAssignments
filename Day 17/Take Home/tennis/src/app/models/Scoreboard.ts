import { Injectable } from '@angular/core';
import { Player } from './player';

@Injectable({
  providedIn: 'root',
})
export class Scoreboard {
  player1: Player = new Player();
  player2: Player = new Player();

  scoreLabels: string[] = ['Love', '15', '30', '40', 'Adv', 'Game'];

  incrementPoint(player: number) {
    if (player === 1) {
      this.player1.score++;
    } else {
      this.player2.score++;
    }

    this.checkGame();
  }

  checkGame() {
    const { player1, player2 } = this;

    if (player1.score >= 3 && player1.score === player2.score) {
      player1.score = 3;
      player2.score = 3;
    } else if (player1.score >= 4 && player1.score - player2.score === 1) {
      player1.score = 4;
    } else if (player2.score >= 4 && player2.score - player1.score === 1) {
      player2.score = 4;
    } else if (player1.score >= 4 && player1.score - player2.score >= 2) {
      player1.games++;
      this.resetScore();
    } else if (player2.score >= 4 && player2.score - player1.score >= 2) {
      player2.games++;
      this.resetScore();
    } else if (player1.score === 4 && player2.score === 4) {
      player1.score = 3;
      player2.score = 3;
    }

    this.checkSet();
  }

  checkSet() {
    const { player1, player2 } = this;

    if (player1.games >= 6 && player1.games - player2.games >= 2) {
      player1.sets++;
      this.resetGames();
    } else if (player2.games >= 6 && player2.games - player1.games >= 2) {
      player2.sets++;
      this.resetGames();
    }

    if (player1.sets === 2 || player2.sets === 2) {
      this.resetGame();
    }
  }

  resetScore() {
    this.player1.score = 0;
    this.player2.score = 0;
  }

  resetGames() {
    this.player1.games = 0;
    this.player2.games = 0;
  }

  resetGame() {
    this.player1.sets = 0;
    this.player2.sets = 0;
    this.resetGames();
    this.resetScore();
  }

  getScoreLabel(score: number): string {
    return this.scoreLabels[score];
  }
}
