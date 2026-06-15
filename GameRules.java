import java.util.List;

public class GameRules {
    public int score = 0;
    public int lives = 5;
    public boolean gameOver = false;
    public boolean showNext = false;

    // Di GamePanel cukup panggil method ini, tidak perlu tahu path "sound/Hungry.wav"
    public void playSuccessSound() { SoundPlayer.play("sound/Hungry.wav"); }
    public void playFailSound() { SoundPlayer.play("sound/AnswerWrong.wav"); }
    public void playGameOverSound() { SoundPlayer.play("sound/gameOver.wav"); }
    public void playWinLevelSound() { SoundPlayer.play("sound/Congratulations.wav"); }

    public void handleCorrectAnswer(Snake snake) {
        score++;
        snake.grow();
        playSuccessSound();
    }

    public void handleWrongAnswer() {
        lives--;
        playFailSound();
    }

    public void checkGameStatus(List<Answer> answers) {
        if (lives <= 0) {
            gameOver = true;
            playGameOverSound();
        }
        if (answers.stream().noneMatch(a -> a.isCorrect)) {
            showNext = true;
            playWinLevelSound();
        }
    }

    public void reset(int initialLives) {
        this.lives = initialLives;
        this.score = 0;
        this.gameOver = false;
        this.showNext = false;
    }
}