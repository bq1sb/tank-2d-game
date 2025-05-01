public class LevelLoader {
    public static String[] getLevelData(int level) {
        switch (level) {
            case 1:
                return new String[] {
                        "WWWWWWWWWWWWWWWWWWWW",
                        "W........E........W",
                        "W..P..............W",
                        "WWWWWWWWWWWWWWWWWWWW"
                };
            case 2:
                return new String[] {
                        "WWWWWWWWWWWWWWWWWWWW",
                        "W.....E..W.........W",
                        "W.........P........W",
                        "WWWWWWWWWWWWWWWWWWWW"
                };
            case 3:
                return new String[] {
                        "WWWWWWWWWWWWWWWWWWWW",
                        "W.E...............W",
                        "W..P.....W........W",
                        "WWWWWWWWWWWWWWWWWWWW"
                };
            default:
                return null; // Уровень не найден
        }
    }
}
