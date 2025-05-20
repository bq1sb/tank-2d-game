import java.awt.Graphics;
import java.util.List;

public class GamePlayRenderStrategy implements RenderStrategy {
    private PlayerTank playerTank;
    private List<EnemyTank> enemies;
    private List<Wall> walls; // Добавим стены, если они рисуются в GamePanel

    public GamePlayRenderStrategy(PlayerTank playerTank, List<EnemyTank> enemies, List<Wall> walls) {
        this.playerTank = playerTank;
        this.enemies = enemies;
        this.walls = walls;
    }

    @Override
    public void render(Graphics g) {
        // Логика отрисовки игрового процесса, как была в GamePanel
        if (playerTank != null) {
            playerTank.draw(g);
        }
        for (EnemyTank enemy : enemies) {
            enemy.draw(g);
        }
        // Если стены рисуются здесь, добавьте их
        // for (Wall wall : walls) {
        //     wall.draw(g);
        // }
    }
}
