// GameMap.java
import java.util.ArrayList;
import java.awt.*;
import java.util.List;

public class GameMap {

    public static final int TILE_SIZE = 32; // Или любое другое желаемое значение

    private String[] mapData;
    public List<Wall> walls = new ArrayList<>();
    public List<BreakableWall> breakableWalls = new ArrayList<>();
    public List<EnemyTank> enemies = new ArrayList<>();
    public List<Point> originalEnemyPositions = new ArrayList<>();
    public PlayerTank player;

    public enum TileType {
        WALL('W'),
        BREAKABLE_WALL('B'),
        ENEMY_TANK('E'),
        PLAYER('P'),
        EMPTY('.');

        private final char symbol;

        TileType(char symbol) {
            this.symbol = symbol;
        }

        public char getSymbol() {
            return symbol;
        }

        public static TileType fromChar(char c) {
            for (TileType type : TileType.values()) {
                if (type.getSymbol() == c) {
                    return type;
                }
            }
            return EMPTY;
        }
    }

    public GameMap(String[] mapData, PlayerTank player) {
        this.player = player;
        this.mapData = mapData;
        if (mapData != null) {
            loadMap();
        }
    }

    public static String[] loadLevelData() {
        String[] level = {
        };
        return level;
    }

    public void loadMap() {
        walls.clear();
        breakableWalls.clear();
        enemies.clear();
        originalEnemyPositions.clear();

        if (mapData != null) {
            for (int y = 0; y < mapData.length; y++) {
                for (int x = 0; x < mapData[y].length(); x++) {
                    char tile = mapData[y].charAt(x);
                    int pixelX = x * TILE_SIZE;
                    int pixelY = y * TILE_SIZE;

                    TileType tileType = TileType.fromChar(tile);

                    switch (tileType) {
                        case WALL:
                            walls.add(new Wall(pixelX, pixelY));
                            break;
                        case BREAKABLE_WALL:
                            breakableWalls.add(new BreakableWall(pixelX, pixelY));
                            break;
                        case ENEMY_TANK:
                            EnemyTank enemy = new EnemyTank(player, walls);
                            enemy.setPosition(pixelX, pixelY); // Устанавливаем позицию из карты
                            enemies.add(enemy);
                            originalEnemyPositions.add(new Point(pixelX, pixelY));
                            break;
                        case PLAYER:
                            player.setPosition(pixelX, pixelY);
                            break;
                        case EMPTY:
                            break;
                    }
                }
            }
        }
    }

    public void draw(Graphics g) {
        for (Wall wall : walls) {
            wall.draw(g);
        }

        for (BreakableWall bWall : breakableWalls) {
            bWall.draw(g);
        }

        for (EnemyTank enemy : enemies) {
            enemy.draw(g);
        }

        player.draw(g);
    }

    public int getWidthInTiles() {
        if (mapData != null && mapData.length > 0) {
            return mapData[0].length();
        }
        return 0;
    }
    public void loadNextLevel(int level) {
        // Загружаем данные следующего уровня в зависимости от номера уровня
        String[] nextLevelData = LevelLoader.getLevelData(level);

        if (nextLevelData != null) {
            this.mapData = nextLevelData;
            loadMap(); // Обновляем карту на основе новых данных уровня
        } else {
            System.err.println("Не удалось загрузить данные уровня: " + level);
        }
    }

    public int getHeightInTiles() {
        if (mapData != null) {
            return mapData.length;
        }
        return 0;
    }
}




