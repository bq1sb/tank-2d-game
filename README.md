# tank-2d-game

classDiagram

class Game {
  - static instance: Game
  - background: Image
  - menuBackground: Image
  - logoIcon: ImageIcon
  - volume: float
  - isFullscreen: boolean
  - isSoundOn: boolean
  - backgroundMusicClip: Clip
  + getInstance(): Game
  + showMainMenu(): void
  + showGameModeSelection(): void
  + showSettingsMenu(): void
  + toggleSound(): void
  + toggleFullscreen(enable: boolean): void
}

class GameFrame {
  - gamePanel: GamePanel
  - playerTank: PlayerTank
  - walls: List~Wall~
  - enemies: List~EnemyTank~
  - gameMap: GameMap
  - bulletFactory: BulletFactory
  + GameFrame()
  - startGameLoop(gamePanel: GamePanel): void
}

class GamePanel {
  - playerTank: PlayerTank
  - enemies: List~EnemyTank~
  - walls: List~Wall~
  - gameOver: boolean
  - currentRenderStrategy: RenderStrategy
  + GamePanel(...)
  - gameOver(): void
  + update(): void
  + paintComponent(g: Graphics): void
}

class GameScreen {
  - gameMap: GameMap
  - playerTank: PlayerTank
  - currentLevel: int
  - gameOver: boolean
  - items: List~Item~
  - bulletFactory: BulletFactory
  + GameScreen()
  - loadLevel(levelNumber: int): void
  + updateGame(e: ActionEvent): void
  + paintComponent(g: Graphics): void
}

class GameMap {
  + static TILE_SIZE: int
  + walls: List~Wall~
  + enemies: List~EnemyTank~
  + originalEnemyPositions: List~Point~
  - playerTank: PlayerTank
  - bulletFactory: BulletFactory
  + GameMap(data: String[], ...)
  + static loadLevelData(levelNumber: int): String[]
  - loadMap(data: String[]): void
  + draw(g: Graphics): void
}

class Wall {
  # x: int
  # y: int
  # health: int
  + getBounds(): Rectangle
  + takeDamage(damage: int): void
  + isDestroyed(): boolean
  + draw(g: Graphics): void
}

class BrickWall {
  - wallData: BrickWallData
  + takeDamage(): void
  + isDestroyed(): boolean
  + draw(g: Graphics): void
}

class BrickWallData {
  - health: int
  - maxHealth: int
  - sprite: BufferedImage
  + takeDamage(): void
  + isDestroyed(): boolean
  + getHealth(): int
  + getSprite(): BufferedImage
}

class PlayerTank {
  - x: int
  - y: int
  - direction: Direction
  - bullets: List~Bullet~
  - health: int
  - moveSpeed: int
  + moveUp(): void
  + shoot(): void
  + update(): void
  + draw(g: Graphics): void
  + takeDamage(damage: int): void
  + isAlive(): boolean
}

class EnemyTank {
  - x: int
  - y: int
  - direction: int
  - health: int
  - currentSpeed: int
  - playerTank: PlayerTank
  - currentBehavior: EnemyBehaviorStrategy
  + update(...): void
  + draw(g: Graphics): void
  + takeDamage(): void
  + isAlive(): boolean
  + fireBullet(): void
}

class Bullet {
  - x: int
  - y: int
  - direction: String
  - speed: int
  + update(...): void
  + draw(...): void
  + getBounds(): Rectangle
}

class Item {
  - x: int
  - y: int
  - collected: boolean
  - effectCommand: ItemEffectCommand
  + draw(...): void
  + applyEffect(playerTank: PlayerTank): void
}

class ScoreManager {
  - static instance: ScoreManager
  - score: int
  + getInstance(): ScoreManager
  + addPoints(points: int): void
  + reset(): void
}

class SpriteLoader {
  + load(path: String): BufferedImage
}

class NetworkSetupScreen {
  - game: Game
  + connectToServer(): void
}

class BulletFactory {
  + createBullet(x: int, y: int, direction: String): Bullet
}

class SimpleBulletFactory {
  + createBullet(x: int, y: int, direction: String): Bullet
}

class EnemyBehaviorStrategy {
  + update(...): void
}

class AggressiveStrategy {
  + update(...): void
}

class FleeStrategy {
  + update(...): void
}

class PatrolStrategy {
  + update(...): void
}

class ItemEffectCommand {
  + execute(playerTank: PlayerTank): void
}

class HealCommand {
  - healAmount: int
  + execute(playerTank: PlayerTank): void
}

class RenderStrategy {
  + render(...): void
}

class GamePlayRenderStrategy {
  + render(...): void
}

class GameOverRenderStrategy {
  + render(...): void
}

Game --> GameFrame
Game --> NetworkSetupScreen
GameFrame --> GamePanel
GameFrame --> GameMap
GameFrame --> PlayerTank
GameFrame --> BulletFactory
GamePanel --> PlayerTank
GamePanel --> EnemyTank
GamePanel --> Wall
GamePanel --> RenderStrategy
GamePanel --> GamePlayRenderStrategy
GamePanel --> GameOverRenderStrategy
GameScreen --> GameMap
GameScreen --> PlayerTank
GameScreen --> Item
GameScreen --> BulletFactory
GameScreen --> ScoreManager
GameMap --> Wall
GameMap --> EnemyTank
GameMap --> PlayerTank
GameMap --> BulletFactory
BrickWall --> BrickWallData
BrickWall --|> Wall
EnemyTank --> EnemyBehaviorStrategy
EnemyBehaviorStrategy <|-- AggressiveStrategy
EnemyBehaviorStrategy <|-- FleeStrategy
EnemyBehaviorStrategy <|-- PatrolStrategy
BulletFactory <|-- SimpleBulletFactory
Item --> ItemEffectCommand
ItemEffectCommand <|-- HealCommand
RenderStrategy <|-- GamePlayRenderStrategy
RenderStrategy <|-- GameOverRenderStrategy
