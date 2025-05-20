import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.InputStream;

public class Game extends JFrame {
    private static Game instance;
    private Image background;
    private Image menuBackground;
    private ImageIcon logoIcon;

    private final Rectangle normalStartButton = new Rectangle(80, 40, 650, 110);
    private final Rectangle normalSettingsButton = new Rectangle(80, 230, 650, 110);
    private final Rectangle normalExitButton = new Rectangle(80, 420, 650, 110);

    private final Rectangle fullscreenStartButton = new Rectangle(150, 50, 1300, 160);
    private final Rectangle fullscreenSettingsButton = new Rectangle(150, 350, 1300, 160);
    private final Rectangle fullscreenExitButton = new Rectangle(150, 630, 1300, 160);

    private final Rectangle normalControlsButton = new Rectangle(230, 200, 350, 50);
    private final Rectangle normalSoundButton = new Rectangle(225, 285, 370, 70);
    private final Rectangle normalFullscreenButton = new Rectangle(190, 350, 430, 65);
    private final Rectangle normalBackButton = new Rectangle(290, 430, 200, 70);

    private final Rectangle fullscreenControlsButton = new Rectangle(470, 435, 600, 85);
    private final Rectangle fullscreenSoundButton = new Rectangle(370, 300, 700, 120);
    private final Rectangle fullscreenFullscreenButton = new Rectangle(360, 520, 750, 105);
    private final Rectangle fullscreenBackButton = new Rectangle(550, 650, 420, 90);

    private Rectangle startGameButtonArea;
    private Rectangle settingsButtonArea;
    private Rectangle exitButtonArea;

    // настройки
    private float volume = 0.8f;
    private boolean isFullscreen = false;
    private boolean isSoundOn = true;
    private Clip backgroundMusicClip;
    private String backgroundMusicPath = "menu.wav";

    private Game() {
        setTitle("Танчики");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(800, 600);
        setLocationRelativeTo(null);
        loadResources();
        loadBackgroundMusic();
        showIntroScreen();
        setVisible(true);
    }

    private void loadResources() {
        try (InputStream inputStream = getClass().getResourceAsStream("/logo.png")) {
            if (inputStream == null) throw new IllegalArgumentException("Ресурс не найден: /logo.png");
            background = ImageIO.read(inputStream);
        } catch (IOException e) {
            System.out.println("Ошибка при загрузке фона: " + e.getMessage());
        }

        try (InputStream inputStream = getClass().getResourceAsStream("/menu.png")) {
            if (inputStream == null) throw new IllegalArgumentException("Ресурс не найден: /menu.png");
            menuBackground = ImageIO.read(inputStream);
            updateButtonAreas();
        } catch (IOException e) {
            System.out.println("Ошибка при загрузке фона меню: " + e.getMessage());
        }

        try (InputStream is = getClass().getResourceAsStream("/logo.png")) {
            if (is != null) {
                logoIcon = new ImageIcon(ImageIO.read(is));
            }
        } catch (IOException e) {
            System.out.println("Ошибка загрузки логотипа: " + e.getMessage());
        }
    }

    private void loadBackgroundMusic() {
        try {
            InputStream soundStream = getClass().getClassLoader().getResourceAsStream(backgroundMusicPath);
            if (soundStream != null) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundStream);
                backgroundMusicClip = AudioSystem.getClip();
                backgroundMusicClip.open(audioIn);
                System.out.println("Фоновая музыка успешно загружена и открыта.");
                backgroundMusicClip.loop(Clip.LOOP_CONTINUOUSLY);
            } else {
                System.out.println("Файл фоновой музыки не найден: " + backgroundMusicPath);
            }
        } catch (Exception e) {
            System.out.println("Ошибка загрузки фоновой музыки: " + e.getMessage());
        }
    }

    private void showIntroScreen() {
        JPanel introPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (background != null) {
                    g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(Color.BLACK);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };

        introPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                playClickSound();
                showMainMenu();
            }
        });

        setContentPane(introPanel);
        revalidate();
        repaint();
    }

    public void showMainMenu() {
        JPanel menuPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (menuBackground != null) {
                    g.drawImage(menuBackground, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };

        menuPanel.setLayout(null);
        menuPanel.setBackground(Color.BLACK);

        menuPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point clickPoint = e.getPoint();
                if (startGameButtonArea != null && startGameButtonArea.contains(clickPoint)) {
                    playClickSound();
                    showGameModeSelection();
                } else if (settingsButtonArea != null && settingsButtonArea.contains(clickPoint)) {
                    playClickSound();
                    showSettingsMenu();
                } else if (exitButtonArea != null && exitButtonArea.contains(clickPoint)) {
                    playClickSound();
                    System.exit(0);
                }
            }
        });

        setContentPane(menuPanel);
        updateButtonAreas();
        revalidate();
        repaint();
    }

    private JButton singlePlayerButton;
    private JButton multiplayerButton;
    private JButton backButton;

    void showGameModeSelection() {
        JPanel gameModePanel = new JPanel() {
            Image background;

            {
                background = new ImageIcon(getClass().getResource("/GameMode.png")).getImage();
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
                g.setColor(Color.RED);


            }
        };

        gameModePanel.setLayout(null);

        //  Кнопка "Single Player"
        singlePlayerButton = new JButton();
        singlePlayerButton.setBounds(150, 100, 530, 80);
        singlePlayerButton.setOpaque(false);
        singlePlayerButton.setContentAreaFilled(false);
        singlePlayerButton.setBorderPainted(false);
        singlePlayerButton.addActionListener(e -> startSinglePlayerGame());

        //  Кнопка "Multiplayer"
        multiplayerButton = new JButton();
        multiplayerButton.setBounds(150, 280, 530, 80);
        multiplayerButton.setOpaque(false);
        multiplayerButton.setContentAreaFilled(false);
        multiplayerButton.setBorderPainted(false);
        multiplayerButton.addActionListener(e -> startMultiplayerGame());

        //  Кнопка "Back"
        backButton = new JButton();
        backButton.setBounds(150, 450, 530, 80);
        backButton.setOpaque(false);
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.addActionListener(e -> showMainMenu());

        gameModePanel.add(singlePlayerButton);
        gameModePanel.add(multiplayerButton);
        gameModePanel.add(backButton);

        setContentPane(gameModePanel);
        revalidate();
        repaint();
    }


    private void startSinglePlayerGame() {
        GameScreen gameScreen = new GameScreen();
        setContentPane(gameScreen);
        revalidate();
        repaint();
    }
    private void startMultiplayerGame() {
        System.out.println("Многопользовательская игра выбрана!");
        NetworkSetupScreen networkSetupScreen = new NetworkSetupScreen(this);
        setContentPane(networkSetupScreen);
        revalidate();
        repaint();
    }


    private void showSettingsMenu() {
        JPanel settingsPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (menuBackground != null) {
                    g.drawImage(menuBackground, 0, 0, getWidth(), getHeight(), this);
                }
                try {
                    InputStream inputStream = getClass().getResourceAsStream("/settings.png");
                    if (inputStream != null) {
                        Image settingsImage = ImageIO.read(inputStream);
                        g.drawImage(settingsImage, 0, 0, getWidth(), getHeight(), this);
                    }
                } catch (IOException e) {
                    System.out.println("Ошибка при загрузке изображения для настроек: " + e.getMessage());
                }

                g.setColor(Color.RED);
                if (isFullscreen) {
                    //g.drawRect(fullscreenControlsButton.x, fullscreenControlsButton.y, fullscreenControlsButton.width, fullscreenControlsButton.height);
                    //g.drawRect(fullscreenSoundButton.x, fullscreenSoundButton.y, fullscreenSoundButton.width, fullscreenSoundButton.height);
                    //g.drawRect(fullscreenFullscreenButton.x, fullscreenFullscreenButton.y, fullscreenFullscreenButton.width, fullscreenFullscreenButton.height);
                    //g.drawRect(fullscreenBackButton.x, fullscreenBackButton.y, fullscreenBackButton.width, fullscreenBackButton.height);
                } else {
                    //g.drawRect(normalControlsButton.x, normalControlsButton.y, normalControlsButton.width, normalControlsButton.height);
                    //g.drawRect(normalSoundButton.x, normalSoundButton.y, normalSoundButton.width, normalSoundButton.height);
                    //g.drawRect(normalFullscreenButton.x, normalFullscreenButton.y, normalFullscreenButton.width, normalFullscreenButton.height);
                    //g.drawRect(normalBackButton.x, normalBackButton.y, normalBackButton.width, normalBackButton.height);
                }
            }
        };

        settingsPanel.setLayout(null);
        settingsPanel.setBackground(Color.BLACK);

        settingsPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point clickPoint = e.getPoint();

                Rectangle currentControlsButton;
                Rectangle currentSoundButton;
                Rectangle currentFullscreenButton;
                Rectangle currentBackButton;

                if (isFullscreen) {
                    currentControlsButton = fullscreenControlsButton;
                    currentSoundButton = fullscreenSoundButton;
                    currentFullscreenButton = fullscreenFullscreenButton;
                    currentBackButton = fullscreenBackButton;
                } else {
                    currentControlsButton = normalControlsButton;
                    currentSoundButton = normalSoundButton;
                    currentFullscreenButton = normalFullscreenButton;
                    currentBackButton = normalBackButton;
                }

                if (currentControlsButton.contains(clickPoint)) {
                    playClickSound();
                    openKeyBindingMenu();
                } else if (currentSoundButton.contains(clickPoint)) {
                    playClickSound();
                    toggleSound();
                } else if (currentFullscreenButton.contains(clickPoint)) {
                    playClickSound();
                    toggleFullscreen(!isFullscreen);
                } else if (currentBackButton.contains(clickPoint)) {
                    playClickSound();
                    showMainMenu();
                }
            }
        });

        setContentPane(settingsPanel);
        revalidate();
        repaint();
    }

    private void toggleSound() {
        isSoundOn = !isSoundOn;
        System.out.println("Sound " + (isSoundOn ? "ON" : "OFF"));

        if (backgroundMusicClip != null) {
            if (isSoundOn) {
                setBackgroundMusicVolume(volume);
                if (!backgroundMusicClip.isRunning()) {
                    backgroundMusicClip.loop(Clip.LOOP_CONTINUOUSLY);
                }
            } else {
                if (backgroundMusicClip.isRunning()) {
                    backgroundMusicClip.stop();
                    backgroundMusicClip.setFramePosition(0);
                }
            }
        }
    }

    private void setBackgroundMusicVolume(float volume) {
        if (backgroundMusicClip != null) {
            FloatControl volumeControl = (FloatControl) backgroundMusicClip.getControl(FloatControl.Type.MASTER_GAIN);
            volumeControl.setValue(volume);
        }
    }

    private void toggleFullscreen(boolean enable) {
        isFullscreen = enable;
        dispose();
        setUndecorated(isFullscreen);

        if (isFullscreen) {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            setMaximizedBounds(ge.getMaximumWindowBounds());
            setExtendedState(JFrame.MAXIMIZED_BOTH);
        } else {
            setSize(800, 600);
            setLocationRelativeTo(null);
            setExtendedState(JFrame.NORMAL);
        }

        setVisible(true);
        updateButtonAreas();
    }

    private void openKeyBindingMenu() {
        System.out.println("Open key binding menu");
    }

    private void updateButtonAreas() {
        if (isFullscreen) {
            startGameButtonArea = fullscreenStartButton;
            settingsButtonArea = fullscreenSettingsButton;
            exitButtonArea = fullscreenExitButton;
        } else {
            startGameButtonArea = normalStartButton;
            settingsButtonArea = normalSettingsButton;
            exitButtonArea = normalExitButton;
        }
    }

    private void playClickSound() {
        if (isSoundOn) {
            System.out.println("Click sound played!");
        }
    }

    public static Game getInstance() {
        if (instance == null) {
            instance = new Game();
        }
        return instance;
    }

    public static void main(String[] args) {
        Game.getInstance();
    }
}
















