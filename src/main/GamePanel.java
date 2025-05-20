package main;

import entity.Enemy;
import entity.Player;
import map.Map;
import object.InteractiveObject;
import tile.TileManager;

import javax.swing.JPanel;
import java.awt.*;

/**
 * Clase en la que se inicializara todos los elementos necesarios para que el juego funcione,
 * ademas de estar aqui el loop jugable principal
 */
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
    public int mapLevel = 1;
    public int previousTurn;
    public int combatTurn = playerCombatTurn;
    public int combatTimer= 60;
    public TileManager tileM;

    private int messageDelay = 30;
    private int messageTimer = 0;
    public Enemy currentEnemy;
    public InteractiveObject currentObject;


    public int gameState = statePlay;

    int FPS = 60;

    public UI ui = new UI(this);
    public TileManager TileM = new TileManager(this);
    public CollisionChecker cChecker = new CollisionChecker(this);
    KeyHandler KeyH = new KeyHandler();
    public Map gameMap = new Map(1,this);
    Thread gameThread;

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
        mapLevel = 1;
        gameMap = new Map(mapLevel,this);
        player = new Player(this, KeyH);
        currentEnemy = null;
    }

    /**
     * Actualiza en cada tick de juego todas las entidades de un estado anterior al estado actual,
     * ademas de evaluar el estado del juego (Exploracion, combate o game over)
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

        if (currentObject != null) {
            currentObject.trigger();
            gameMap.interactiveObjects.remove(currentObject);
            currentObject = null;
        }

        //Combate
        if (gameState == stateCombat && currentEnemy != null) {
            combatTimer++;

            switch (combatTurn) {
                case playerCombatTurn:
                    if (combatTimer >= combatDelay) {
                        previousTurn = playerCombatTurn;
                        // Turno del jugador
                        currentEnemy.hp -= Math.max(1, player.damage);
                        ui.addMessage("Attacked an enemy: -"+player.damage+" HP!",Color.YELLOW);
                        combatTimer = 0;
                        combatTurn = combatDelay;
                        messageTimer = 0;
                    }
                    break;

                case combatDelay:
                    messageTimer++;
                    if (messageTimer >= messageDelay) {
                        if (currentEnemy.hp <= 0) {
                            ui.addMessage("Enemy Defeated: +"+currentEnemy.reward+" XP!");
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
                        int damage = currentEnemy.damage - player.armor;
                        if (damage <= 0) {
                            damage = 1;
                        }
                        player.hp -= damage;
                        ui.addMessage("Enemy attacked you: -"+(damage)+" HP!",Color.RED);
                        combatTimer = 0;
                        combatTurn = combatDelay;
                        messageTimer = 0;
                    }
                    break;
            }

            if (player.hp <= 0) {
                ui.addMessage("You died!");
                gameState = stateGameOver;
            }
        }



    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;


        gameMap.drawMap(TileM,g2);

        for (InteractiveObject i : gameMap.interactiveObjects) {
            i.draw(g2);
        }
        for (Enemy e : gameMap.enemies) {
            e.draw(g2);
        }
        player.draw(g2);


        ui.draw(g2, player);

        g2.dispose();

    }
}
