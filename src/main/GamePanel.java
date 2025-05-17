package main;

import entity.Enemy;
import entity.Player;
import map.Map;
import object.SuperObject;
import tile.TileManager;

import javax.swing.JPanel;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {
    //Ajustes de la pantalla
    final public int originalTileSize = 16; //16 x 16 pixeles
    final public int scale = 1;

    final public int tileSize = originalTileSize * scale;
    final public int maxScreenCol = 64;
    final public int maxScreenRow = 42;
    public final int screenWidth = maxScreenCol * tileSize;
    public final int screenHeight = maxScreenRow * tileSize;

    public static final int statePlay = 0;
    public static final int stateCombat = 1;
    public boolean readyForCombat = true;
    public int combatTimer= 60;
    public int combatDelay= 0;
    public Enemy currentEnemy;


    public int gameState = statePlay;

    int FPS = 60;

    public UI ui = new UI(this);
    public TileManager TileM = new TileManager(this);
    public CollisionChecker cChecker = new CollisionChecker(this);
    KeyHandler KeyH = new KeyHandler();
    public Map gameMap = new Map(this);
    Thread gameThread;
    SuperObject obj[] = new SuperObject[10];

    //Entidades
    public Player player = new Player(this, KeyH);


    /**
     * Inicializa los elementos necesarios para que el juego pueda iniciar.
     *
     */
    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(KeyH);
        this.setFocusable(true); //"Focused" en el input
    }

    /**
     * Inicia un hilo para el procesamiento paralelo.
     * En otras palabras, permite que el juego pueda ejecutar otras instrucciones mientras
     * se ejecuta.
     */
    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    /**
     * Inicia el ciclo de juego.
     * En esta funcion es donde todos los elementos del juego existen y se actualizan
     *
     */
    @Override
    public void run(){

        //Control de FPS
        double drawInterval = 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

            while (gameThread !=null) {

                currentTime = System.nanoTime();
                delta += (currentTime - lastTime) / drawInterval;
                lastTime = currentTime;

                if (delta >= 1) {
                    update(); //Actualiza lo que esta pasando en el juego
                    repaint(); //Recrea la ventana del juego
                    delta--;
                }

            }
    }

    /**
     * Actualiza en cada tick de juego todas las entidades de un estado anterior al estado actual
     */
    public void update() {
        //Combate

        if (gameState == stateCombat && currentEnemy != null) {
            combatTimer++;

            if (combatTimer >= combatDelay) {
                combatTimer = 0;

                // Turno del jugador
                currentEnemy.hp -= Math.max(1, player.damage);
                ui.addMessage("¡Atacaste al enemigo!");

                if (currentEnemy.hp <= 0) {
                    ui.addMessage("¡Enemigo derrotado!");
                    player.gainExperience(currentEnemy.reward);
                    gameMap.enemies.remove(currentEnemy);

                    // Finaliza combate
                    currentEnemy = null;
                    gameState = statePlay;
                    return;
                }

                // Turno del enemigo
                player.hp -= Math.max(1, currentEnemy.damage - player.armor);
                ui.addMessage("¡El enemigo te atacó!");

                if (player.hp <= 0) {
                    ui.addMessage("¡Has sido derrotado!");
                    // Aquí podrías cambiar a un estado de GAME_OVER, si lo tienes
                    currentEnemy = null;
                    gameState = stateCombat;
                }
            }
        }


        //Exploracion
        player.update();
        for (Enemy e : gameMap.enemies) {
            e.update();
        }

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;


        gameMap.drawMap(TileM,g2);
        for (Enemy e : gameMap.enemies) {
            e.draw(g2);
        }
        player.draw(g2);
        ui.draw(g2, player);

        g2.dispose();

    }
}
