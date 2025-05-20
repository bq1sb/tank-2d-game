import javax.swing.*;
import java.awt.*;

public class GameOverRenderStrategy implements RenderStrategy {
    private JPanel panel; // Нужен для получения размеров

    public GameOverRenderStrategy(JPanel panel) {
        this.panel = panel;
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, panel.getWidth(), panel.getHeight());
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 50));
        g.drawString("Game Over", panel.getWidth() / 2 - 150, panel.getHeight() / 2);
    }
}
