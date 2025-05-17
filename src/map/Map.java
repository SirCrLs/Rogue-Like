package map;

import entity.Enemy;
import main.GamePanel;
import tile.TileManager;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Map {
    public Random random;
    public ArrayList<Room> rooms = new ArrayList<>();
    public ArrayList<Enemy> enemies = new ArrayList<>();
    private long seed;
    public int[][] map;
    private int width;
    private int height;
    GamePanel gp;

    //Constructor con semilla
    public Map(GamePanel gp, long seed) {
        this.gp = gp;
        this.width = gp.maxScreenCol;
        this.height = gp.maxScreenRow;
        this.seed = seed;
        random = new Random(seed);
        map = new int[width][height];
        generateMap();
    }
    //Constructor sin semilla
    public Map(GamePanel gp) {
        this(gp, new Random().nextLong());
    }

    /**
     * Devuelve la semilla usada por el generador random.
     */
    public long getSeed() {
        return seed;
    }

    /**
     * Delimita el tamaño del mapa dados sus variables altura y anchura
     */
    private void generateMap() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                map[x][y] = 0;
            }
        }

        // Crea las habitaciones
        carveRooms();
        connectRoomsWithMST();
        placeWallsAround();
        spawnEnemies();
    }

    /**
     * Genera rectangulos aleatorios que representaran las habitaciones.
     */
    private void carveRooms() {
        // Crea rectangulos aleatorios
        int numRooms = 15;
        int roomMaxDim = 5;
        for (int i = 0; i < numRooms; i++) {
            int roomWidth = random.nextInt(roomMaxDim) + 5;
            int roomHeight = random.nextInt(roomMaxDim) + 5;
            int x = 2 + random.nextInt(width - roomWidth -3);
            int y = 2 + random.nextInt(height - roomHeight -3);

            Room room = new Room(x, y, roomWidth, roomHeight);
            rooms.add(room);

            for (int dx = 0; dx < roomWidth; dx++) {
                for (int dy = 0; dy < roomHeight; dy++) {
                    map[x + dx][y + dy] = 3;
                }
            }

        }


    }

    /**
     * Connecta las habitaciones siguiendo una serie de requisitos
     * 1. Todas las habitaciones tienen que estar conectadas directa
     *    o indirectamente.
     * 2. Conecta solamente las habitaciones mas cercanas.
     * 3. Los tuneles no pueden cruzarse entre si
     */
    private void connectRoomsWithMST() {
            int n = rooms.size();
            List<Edge> edges = new ArrayList<>();

            // Crear todas las posibles conexiones entre habitaciones
            for (int i = 0; i < n; i++) {
                Room r1 = rooms.get(i);
                for (int j = i + 1; j < n; j++) {
                    Room r2 = rooms.get(j);
                    double dist = Math.hypot(r1.centerX() - r2.centerX(), r1.centerY() - r2.centerY());
                    edges.add(new Edge(i, j, dist));
                }
            }

            Collections.sort(edges);
            UnionFind uf = new UnionFind(n);

            for (Edge edge : edges) {
                if (uf.union(edge.roomA, edge.roomB)) {
                    Room r1 = rooms.get(edge.roomA);
                    Room r2 = rooms.get(edge.roomB);
                    connectRoomsSafe(r1, r2);  // Túneles
                }
            }
        }


    /**
     * @param r1 Objeto Room 1
     * @param r2 Objeto Room 2
     * @apiNote Obtiene los objetos room y obtiene las coordenadas de
     * su centro para despues crear los tuneles
     */
        private void connectRoomsSafe(Room r1, Room r2) {
        int x1 = r1.centerX();
        int y1 = r1.centerY();
        int x2 = r2.centerX();
        int y2 = r2.centerY();

        // Primero horizontal, luego vertical
        if (random.nextBoolean()) {
            if (carveHorizontalTunnel(x1, x2, y1))
                carveVerticalTunnel(y1, y2, x2);
        } else {
            if (carveVerticalTunnel(y1, y2, x1))
                carveHorizontalTunnel(x1, x2, y2);
        }
    }


    /**
     * @param r1 primera habitacion
     * @param r2 segunda habitacion.
     * @apiNote Conecta 2 habitaciones en caminos creados horizontal o verticalmente
     */
    private void connectRooms(Room r1, Room r2) {
        int x1 = r1.centerX();
        int y1 = r1.centerY();
        int x2 = r2.centerX();
        int y2 = r2.centerY();

        if (random.nextBoolean()) {
            // Horizontal primero, luego vertical
            carveHorizontalTunnel(x1, x2, y1);
            carveVerticalTunnel(y1, y2, x2);
        } else {
            // Vertical primero, luego horizontal
            carveVerticalTunnel(y1, y2, x1);
            carveHorizontalTunnel(x1, x2, y2);
        }
    }



    /**
     * @param x1 Centro x de la habitcion 1
     * @param x2 Centro x de la habitacion 2
     * @param y Centro y de la habitacion 1
     * @apiNote Crea un camino en el arreglo del mapa, conectando
     * el centro de la habitacion 1 con el centro de la habitacion 2
     */
    private boolean carveHorizontalTunnel(int x1, int x2, int y) {
        for (int x = Math.min(x1, x2); x <= Math.max(x1, x2); x++) {
            if (map[x][y] == 0) {
                map[x][y] = 2;
            }
        }
        return true;
    }

    /**
     * @param y1 Centro y de la habitacion 1
     * @param y2 Centro y de la habitacion 2
     * @param x Centro x de la habitacion 1
     * @apiNote Crea un camino en el arreglo del mapa, conectando
     * el centro de la habitacion 1 con el centro de la habitacion 2
     */
    private boolean carveVerticalTunnel(int y1, int y2, int x) {
        for (int y = Math.min(y1, y2); y <= Math.max(y1, y2); y++) {
            if (map[x][y] == 0) {
                map[x][y] = 2;
            }
        }
        return true;
    }

    /**
     * Verifica que alrededor de las habitaciones haya lugar para colocar paredes
     */
    private void placeWallsAround() {
        int[][] directions = {
                // Direcciones adyacentes del tile
                {-1, -1}, {-1, 0}, {-1, 1},
                {0, -1},          {0, 1},
                {1, -1},  {1, 0}, {1, 1}
        };


        for (int x = 1; x < width - 1; x++) {
            for (int y = 1; y < height - 1; y++) {
                // Solo procesar celdas que son parte de habitaciones (2) o túneles (3)
                if (map[x][y] == 2 || map[x][y] == 3) {
                    // Recorremos los 8 vecinos
                    for (int[] d : directions) {
                        int nx = x + d[0];
                        int ny = y + d[1];

                        // Verificar si los índices están dentro de los límites del mapa
                        if ((nx < width-1  && ny < height-1) && (nx > 0  && ny > 0)) {
                            if (map[nx][ny] == 0) {

                                int adjacentTile = getAdjacentTileType( nx, ny);
                                if (adjacentTile != -1) {
                                    map[nx][ny] = adjacentTile;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * @param nx indice x del mapa
     * @param ny indice y del mapa
     * @return retorna el tipo de tile (pared, piso, etc)
     * que se debe colocar
     */
    private int getAdjacentTileType(int nx, int ny) {
        // Verificar las celdas adyacentes para decidir el tipo de pared
        if (map[nx - 1][ny] == 3 || map[nx - 1][ny] == 2) {
            return 6; // Pared izquierda
        } else if (map[nx + 1][ny] == 3 || map[nx + 1][ny] == 2) {
            return 7; // Pared derecha
        } else if (map[nx][ny - 1] == 3 || map[nx][ny - 1] == 2) {
            return 5; // Pared arriba
        } else if (map[nx][ny + 1] == 3 || map[nx][ny + 1] == 2) {
            return 4; // Pared abajo
        }

        return -1; // No se encuentra ninguna celda adyacente válida
    }

    private void spawnEnemies() {
        for (Room room : rooms) {
            int enemyCount = 1 + (int)(Math.random() * 2); // 1 o 2 enemigos por habitación

            for (int i = 0; i < enemyCount; i++) {
                int ex = room.x + 1 + (int)(Math.random() * (room.width - 2));
                int ey = room.y + 1 + (int)(Math.random() * (room.height - 2));

                Rectangle patrolArea = new Rectangle(
                        room.x * gp.tileSize,
                        room.y * gp.tileSize,
                        room.width * gp.tileSize,
                        room.height * gp.tileSize
                );

                Enemy e = new Enemy(ex * gp.tileSize, ey * gp.tileSize, patrolArea,gp);
                enemies.add(e); // Asegúrate de tener acceso a la lista `enemies`
            }
        }
    }



    /**
     * Dibuja el mapa en pantalla.
     */
    public void drawMap(TileManager tm, Graphics g2) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                //Coordenadas y dimensiones de los tiles
                int screenX = x * gp.tileSize;
                int screenY = y * gp.tileSize;

                g2.drawImage(tm.tile[map[x][y]].image,screenX,screenY,gp.tileSize,gp.tileSize,null);
            }
        }
        for (Enemy enemy : enemies) {
            if (enemy.alive) {
                g2.drawImage(enemy.left1, enemy.x, enemy.y, gp.tileSize, gp.tileSize, null);
            }
        }
    }


    /**
     * Obtiene la distancia de 2 puntos y los compara
     */
    private static class Edge implements Comparable<Edge> {
        int roomA, roomB;
        double distance;

        public Edge(int roomA, int roomB, double distance) {
            this.roomA = roomA;
            this.roomB = roomB;
            this.distance = distance;
        }

        @Override
        public int compareTo(Edge other) {
            return Double.compare(this.distance, other.distance);
        }
    }

    /**
     * Algoritmo para unir 2 puntos mas cercanos
     */
    private static class UnionFind {
        int[] parent;

        public UnionFind(int size) {
            parent = new int[size];
            for (int i = 0; i < size; i++)
                parent[i] = i;
        }

        public int find(int x) {
            if (parent[x] != x)
                parent[x] = find(parent[x]);
            return parent[x];
        }

        public boolean union(int a, int b) {
            int rootA = find(a);
            int rootB = find(b);
            if (rootA == rootB) return false;
            parent[rootA] = rootB;
            return true;
        }
    }


}

