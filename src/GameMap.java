import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GameMap {
    public static final int TILE_SIZE = 32;
    public List<Wall> walls;
    public List<EnemyTank> enemies;
    public List<Point> originalEnemyPositions;
    private PlayerTank playerTank;

    public GameMap(String[] data, PlayerTank player) {
        this.playerTank = player;
        this.walls = new ArrayList<>();
        this.enemies = new ArrayList<>();
        this.originalEnemyPositions = new ArrayList<>();
        loadMap(data); // Теперь loadMap принимает данные уровня
    }

    public static String[] loadLevelData(int levelNumber) {
        if (levelNumber == 1) {
            return new String[]{
                    "WWWWWWWWWWWWWWWWWWWWWWWWW",
                    "W............BB.........W",
                    "W....BB............BB...W",
                    "W..WWWWWWWWWWWWWWWWWWW..W",
                    "W..W...BB........E...W..W",
                    "W..W.................W..W",
                    "W.E.....BB......BB......W",
                    "W.......................W",
                    "W...BB......P...BB......W",
                    "W.......................W",
                    "W..W.........BB......W..W",
                    "W..W..BB.............W..W",
                    "W..W..E.......BB.....W..W",
                    "W..W...BB............W..W",
                    "W..WWWWWWWWWWWWWWWWWWW..W",
                    "W.........BB.......BB...W",
                    "W...BB..................W",
                    "WWWWWWWWWWWWWWWWWWWWWWWWW"
            };
        } else if (levelNumber == 2) {
            return new String[]{
                    "WWWWWWWWWWWWWWWWWWWWWWWWW",
                    "W.......................W",
                    "W.....P...BB............W",
                    "W..W.................W..W",
                    "W..W..........BB.....W..W",
                    "W..W..........BB.....W..W",
                    "W..W..E.............EW..W",
                    "W..W.................W..W",
                    "W..W.....BB..........W..W",
                    "W..W.....BB..........W..W",
                    "W..W.................W..W",
                    "W..W.................W..W",
                    "W..W........E....BB..W..W",
                    "W.....BB................W",
                    "W.....BB.....B....BB....W",
                    "W..........E............W",
                    "W.......................W",
                    "WWWWWWWWWWWWWWWWWWWWWWWWW"
            };
        } else if (levelNumber == 3) {
            return new String[]{
                    "WWWWWWWWWWWWWWWWWWWWWWWWW",
                    "W........WW.............W",
                    "W..P.....WW..E...WW.....W",
                    "W....WW..........WW.....W",
                    "W....WW.................W",
                    "W.............WW........W",
                    "W..WW....E....WW........W",
                    "W..WW...WW........E.....W",
                    "W.......WW.........WW...W",
                    "W...........WW.....WW...W",
                    "W....E......WW..........W",
                    "W..WW...........WW......W",
                    "W..WW....WW.....WW..WW..W",
                    "W........WW...E.....WW..W",
                    "W....WW......WW.........W",
                    "W....WW......WW.........W",
                    "W.......................W",
                    "WWWWWWWWWWWWWWWWWWWWWWWWW"
            };
        } else if (levelNumber == 4) {
            return new String[]{
                    "WWWWWWWWWWWWWWWWWWWWWWWWW",
                    "W..................E....W",
                    "W..P....................W",
                    "W...W..............W....W",
                    "W...WWWWWWWWWWWWWWWWW...W",
                    "W..........WWW..........W",
                    "W...W....E..........W...W",
                    "W...WWWWWWWWWWWWWWWWW...W",
                    "W....................E..W",
                    "W.....E.........E.......W",
                    "W...WWWWWWWWWWWWWWWWW...W",
                    "W...W...............W...W",
                    "W..........WWW..........W",
                    "W...WWWWWWWWWWWWWWWWW...W",
                    "W...W..E............W...W",
                    "W.......................W",
                    "W.......................W",
                    "WWWWWWWWWWWWWWWWWWWWWWWWW"
            };
        } else { // levelNumber == 5 (или любой другой)
            return new String[]{
                    "WWWWWWWWWWWWWWWWWWWWWWWWW",
                    "W...........W...........W",
                    "W..P....W..........W....W",
                    "W............W..........W",
                    "W....E.............W....W",
                    "W.....W..W.....W........W",
                    "W....W...E.........W....W",
                    "W.....W.....W...........W",
                    "W..W...........W.....E..W",
                    "W........W.........W....W",
                    "W...W..........W........W",
                    "W........W.........W....W",
                    "W....E.......W..........W",
                    "W......W................W",
                    "W....E...W....W....W....W",
                    "W....W...........W......W",
                    "W........W.E.....W......W",
                    "WWWWWWWWWWWWWWWWWWWWWWWWW"
            };
        }
    }

    private void loadMap(String[] data) {
        walls.clear();
        enemies.clear();
        originalEnemyPositions.clear();
        for (int y = 0; y < data.length; y++) {
            for (int x = 0; x < data[y].length(); x++) {
                switch (data[y].charAt(x)) {
                    case 'W' -> walls.add(new Wall(x * TILE_SIZE, y * TILE_SIZE));
                    case 'E' -> {
                        EnemyTank enemyTank = new EnemyTank(playerTank, walls);
                        enemyTank.setPosition(x * TILE_SIZE, y * TILE_SIZE);
                        enemies.add(enemyTank);
                        originalEnemyPositions.add(new Point(x * TILE_SIZE, y * TILE_SIZE));
                    }
                    case 'P' -> playerTank.setPosition(x * TILE_SIZE, y * TILE_SIZE);
                    case 'B' -> walls.add(new BrickWall(x * TILE_SIZE, y * TILE_SIZE)); // Создаем BrickWall
                }
            }
        }
    }

    public void update() {
        // Обновление логики карты, если необходимо
    }

    public void draw(Graphics g) {
        for (Wall wall : walls) {
            wall.draw(g);
        }
        for (EnemyTank enemy : enemies) {
            if (enemy.isAlive()) {
                enemy.draw(g);
            }
        }
    }
}
