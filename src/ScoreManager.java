public class ScoreManager {
    private static ScoreManager instance;
    private int score;

    private ScoreManager() {
        score = 0;
    }

    public static ScoreManager getInstance() {
        if (instance == null) {
            instance = new ScoreManager();
        }
        return instance;
    }

    public int getScore() {
        return score;
    }

    public void addPoints(int points) {
        score += points;
    }

    public void reset() {
        score = 0; // Сбрасываем очки
    }
}
