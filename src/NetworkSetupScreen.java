import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class NetworkSetupScreen extends JPanel {
    private Game game;
    private JTextField ipTextField;
    private JTextField portTextField;
    private JButton invisibleConnectButton;
    private JButton invisibleBackButton;
    private Image background;

    public NetworkSetupScreen(Game game) {
        this.game = game;
        setLayout(null);

        URL url = getClass().getResource("/NetworkSetup.png");
        if (url != null) {
            background = new ImageIcon(url).getImage();
        } else {
            System.err.println(" Фон NetworkSetup.png не найден!");
        }

        ipTextField = new JTextField("127.0.0.1");
        ipTextField.setBounds(220, 176, 360, 60);
        styleTextField(ipTextField);
        add(ipTextField);

        portTextField = new JTextField("12345");
        portTextField.setBounds(220, 324, 360, 60);
        styleTextField(portTextField);
        add(portTextField);

        invisibleConnectButton = new JButton();
        invisibleConnectButton.setBounds(160, 420, 265, 50);
        styleInvisibleButton(invisibleConnectButton);
        invisibleConnectButton.addActionListener(e -> connectToServer());
        add(invisibleConnectButton);

        invisibleBackButton = new JButton();
        invisibleBackButton.setBounds(450, 420, 205, 50);
        styleInvisibleButton(invisibleBackButton);
        invisibleBackButton.addActionListener(e -> game.showGameModeSelection());
        add(invisibleBackButton);
    }

    private void styleTextField(JTextField field) {
        field.setForeground(new Color(255, 255, 255));
        field.setBackground(new Color(0, 128, 0));
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        field.setFont(new Font("Monospaced", Font.BOLD, 16));
    }

    private void styleInvisibleButton(JButton button) {
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
    }

    private void connectToServer() {
        String ip = ipTextField.getText();
        int port = Integer.parseInt(portTextField.getText());
        System.out.println(" Подключаемся к серверу: IP=" + ip + ", Port=" + port);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (background != null) {
            g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        }

        g.setColor(Color.RED);
        g.drawRect(220, 176, 360, 60);
        g.drawRect(220, 324, 360, 60);
        g.drawRect(160, 420, 265, 50);
        g.drawRect(450, 420, 205, 50);
    }
}



