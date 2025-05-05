public class LightBot {
    private char[][] originalMap;
    private char[][] currentMap;
    private int robotX;
    private int robotY;
    private Direction robotDir;
    private final int initialX;
    private final int initialY;
    final Direction initialDir;

    private enum Direction {
        N, S, E, W;

        public Direction left() {
            switch (this) {
                case N: return W;
                case W: return S;
                case S: return E;
                case E: return N;
                default: throw new IllegalStateException();
            }
        }

        public Direction right() {
            switch (this) {
                case N: return E;
                case E: return S;
                case S: return W;
                case W: return N;
                default: throw new IllegalStateException();
            }
        }
    }

    public LightBot(String[] initialMap) {
        int rows = initialMap.length;
        int cols = (rows > 0) ? initialMap[0].length() : 0;
        originalMap = new char[rows][cols];
        currentMap = new char[rows][cols];
        boolean robotFound = false;
        int tempX = 0, tempY = 0;
        Direction tempDir = null;

        for (int y = 0; y < rows; y++) {
            String row = initialMap[y];
            for (int x = 0; x < cols; x++) {
                char c = row.charAt(x);
                if (c == 'R' || c == 'L' || c == 'U' || c == 'D') {
                    if (robotFound) {
                        throw new IllegalArgumentException("Multiple robot positions");
                    }
                    robotFound = true;
                    tempX = x;
                    tempY = y;
                    switch (c) {
                        case 'R': tempDir = Direction.E; break;
                        case 'L': tempDir = Direction.W; break;
                        case 'U': tempDir = Direction.N; break;
                        case 'D': tempDir = Direction.S; break;
                    }
                    originalMap[y][x] = '.';
                } else {
                    originalMap[y][x] = c;
                }
            }
        }
        if (!robotFound) {
            throw new IllegalArgumentException("No robot found");
        }
        initialX = tempX;
        initialY = tempY;
        initialDir = tempDir;
        reset();
    }

    public void reset() {
        robotX = initialX;
        robotY = initialY;
        robotDir = initialDir;
        for (int i = 0; i < originalMap.length; i++) {
            System.arraycopy(originalMap[i], 0, currentMap[i], 0, originalMap[i].length);
        }
    }

    public void runProgram(String[] instructions) {
        for (String instr : instructions) {
            switch (instr) {
                case "LEFT":
                    robotDir = robotDir.left();
                    break;
                case "RIGHT":
                    robotDir = robotDir.right();
                    break;
                case "FORWARD":
                    moveForward();
                    break;
                case "LIGHT":
                    executeLight();
                    break;
                default:
                    throw new IllegalArgumentException("Unknown instruction: " + instr);
            }
        }
    }

    private void moveForward() {
        int newX = robotX;
        int newY = robotY;
        switch (robotDir) {
            case E: newX++; break;
            case W: newX--; break;
            case N: newY--; break;
            case S: newY++; break;
        }
        if (newX >= 0 && newX < currentMap[0].length && newY >= 0 && newY < currentMap.length) {
            robotX = newX;
            robotY = newY;
        }
    }

    private void executeLight() {
        char current = currentMap[robotY][robotX];
        if (current == 'O') {
            currentMap[robotY][robotX] = 'X';
        } else if (current == '.' || current == 'x') {
            currentMap[robotY][robotX] = 'x';
        }
    }

    public int[] getRobotPosition() {
        return new int[]{robotX, robotY};
    }

    public String[] getMap() {
        String[] result = new String[currentMap.length];
        for (int i = 0; i < currentMap.length; i++) {
            result[i] = new String(currentMap[i]);
        }
        return result;
    }
}