import { Component, HostListener } from '@angular/core';


@Component({
  selector: 'app-scoreboard',
  templateUrl: './scoreboard.component.html',
  styleUrls: ['./scoreboard.component.css']
})
export class ScoreboardComponent {
  player1Sets: number = 0;
  player2Sets: number = 0;
  player1Games: number = 0;
  player2Games: number = 0;
  player1Score: number = 0;
  player2Score: number = 0;

  scoreLabels: string[] = ['Love', '15', '30', '40', 'Adv', 'Game'];

  incrementPoint(player: number) {
    if (player === 1) {
      this.player1Score++;
    } else {
      this.player2Score++;
    }

    this.checkGame();
  }

  checkGame() {
    if (this.player1Score >= 3 && this.player1Score === this.player2Score) {
      this.player1Score = 3;
      this.player2Score = 3;
    } else if (this.player1Score >= 4 && this.player1Score - this.player2Score === 1) {
      this.player1Score = 4;
    } else if (this.player2Score >= 4 && this.player2Score - this.player1Score === 1) {
      this.player2Score = 4;
    } else if (this.player1Score >= 4 && this.player1Score - this.player2Score >= 2) {
      this.player1Games++;
      this.resetScore();
    } else if (this.player2Score >= 4 && this.player2Score - this.player1Score >= 2) {
      this.player2Games++;
      this.resetScore();
    } else if (this.player1Score === 4 && this.player2Score === 4) {
      this.player1Score = 3;
      this.player2Score = 3;
    }

    this.checkSet();
  }

  checkSet() {
    if (this.player1Games >= 6 && this.player1Games - this.player2Games >= 2) {
      this.player1Sets++;
      this.resetGames();
    } else if (this.player2Games >= 6 && this.player2Games - this.player1Games >= 2) {
      this.player2Sets++;
      this.resetGames();
    }

    if (this.player1Sets === 2) {
      this.resetGame();
    } else if (this.player2Sets === 2) {
      this.resetGame();
    }
  }

  resetScore() {
    this.player1Score = 0;
    this.player2Score = 0;
  }

  resetGames() {
    this.player1Games = 0;
    this.player2Games = 0;
  }

  resetGame() {
    this.player1Sets = 0;
    this.player2Sets = 0;
    this.resetGames();
    this.resetScore();
  }

  player1Scores() {
    this.incrementPoint(1);
  }

  player2Scores() {
    this.incrementPoint(2);
  }

  getScoreLabel(score: number): string {
    return this.scoreLabels[score];
  }
}
