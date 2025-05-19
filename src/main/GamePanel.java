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
    public static final int stateGameOver = 2;
    public static final int playerCombatTurn = 0;
    public static final int enemyCombatTurn = 1;
    public static final int combatDelay= 2;
    public int previousTurn;
    public int combatTurn = playerCombatTurn;
    public int combatTimer= 60;

    private int messageDelay = 30;
    private int messageTimer = 0;
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

    private void resetGame(){
        gameMap = new Map(this);
        player = new Player(this, KeyH);
        currentEnemy = null;
    }

    /**
     * Actualiza en cada tick de juego todas las entidades de un estado anterior al estado actual
     */
    public void update() {
        if (gameState == stateGameOver) {
            if (KeyH.RPressed) {
                resetGame(); // Método para reiniciar
                gameState = statePlay;
            }
        }
        //Exploracion
        for (Enemy e : gameMap.enemies) {
            e.update();
        }
        player.update();

        //Combate
        if (gameState == stateCombat && currentEnemy != null) {
            combatTimer++;

            switch (combatTurn) {
                case playerCombatTurn:
                    if (combatTimer >= combatDelay) {
                        previousTurn = playerCombatTurn;
                        // Turno del jugador
                        currentEnemy.hp -= Math.max(1, player.damage);
                        ui.addMessage("Attacked an enemy!",Color.YELLOW);
                        combatTimer = 0;
                        combatTurn = combatDelay;
                        messageTimer = 0;
                    }
                    break;

                case combatDelay:
                    messageTimer++;
                    if (messageTimer >= messageDelay) {
                        if (currentEnemy.hp <= 0) {
                            ui.addMessage("Enemy Defeated!");
                            player.gainExperience(currentEnemy.reward);
                            gameMap.enemies.remove(currentEnemy);
                            currentEnemy = null;
                            gameState = statePlay;
                            previousTurn = enemyCombatTurn;
                        } else if(previousTurn == playerCombatTurn) {
                            combatTurn = enemyCombatTurn;
                        } else{
                            combatTurn = playerCombatTurn;
                        }
                    }
                    break;

                case enemyCombatTurn:
                    if (combatTimer >= combatDelay) {
                        previousTurn = enemyCombatTurn;
                        // Turno del enemigo
                        player.hp -= Math.max(1, currentEnemy.damage - player.armor);
                        ui.addMessage("Enemy attacked you!",Color.RED);
                        combatTimer = 0;
                        combatTurn = combatDelay;
                        messageTimer = 0;
                    }
                    break;
            }

            // Verificar si el jugador murió después del ataque enemigo
            if (player.hp <= 0) {
                ui.addMessage("You died!");
                gameState = stateGameOver; // Cambia a tu estado de GAME_OVER si existe
            }
        }



    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;


        gameMap.drawMap(TileM,g2);
        for (Enemy e : gameMap.enemies) {
            e.draw(g2);
            g2.setColor(Color.GREEN);
            g2.drawRect(
                    e.hitBox.x,
                    e.hitBox.y,
                    e.hitBox.width,
                    e.hitBox.height
            );
        }
        player.draw(g2);
        g2.setColor(Color.RED);
        g2.drawRect(
                player.hitBox.x,
                player.hitBox.y,
                player.hitBox.width,
                player.hitBox.height
        );


        ui.draw(g2, player);

        g2.dispose();

    }
}
