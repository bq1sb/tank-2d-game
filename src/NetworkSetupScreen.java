import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NetworkSetupScreen extends JPanel {
    private JTextField ipTextField;
    private JTextField portTextField;
    private JButton connectButton;

    public NetworkSetupScreen() {
        setLayout(new BorderLayout());

        // Панель для ввода IP и порта
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(2, 2));

        inputPanel.add(new JLabel("IP Address:"));
        ipTextField = new JTextField("127.0.0.1");  // Значение по умолчанию
        inputPanel.add(ipTextField);

        inputPanel.add(new JLabel("Port:"));
        portTextField = new JTextField("12345");  // Значение по умолчанию
        inputPanel.add(portTextField);

        add(inputPanel, BorderLayout.CENTER);

        // Кнопка подключения
        connectButton = new JButton("Connect");
        connectButton.addActionListener(e -> connectToServer());
        add(connectButton, BorderLayout.SOUTH);
    }

    private void connectToServer() {
        String ip = ipTextField.getText();
        int port = Integer.parseInt(portTextField.getText());

        // Логика подключения к серверу (пока просто выводим данные в консоль)
        System.out.println("Подключаемся к серверу с IP: " + ip + " и портом: " + port);

        // Для теста просто выводим IP и порт в консоль, потом будет логика для подключения.
    }
}


