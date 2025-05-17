package entity;

import main.GamePanel;
import main.KeyHandler;
import map.Room;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static main.GamePanel.statePlay;

public class Player extends Entity {
    public int exp;
    public int expNextLevel;
    public int armor;
    GamePanel gp;
    KeyHandler keyH;

    public Player(GamePanel gp,KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;

        getPlayerImage();
        setDefaultValues();
    }

    public void setDefaultValues() {
        Room initialRoom;
        initialRoom = gp.gameMap.rooms.getFirst();
        x = initialRoom.centerX()* gp.tileSize;
        y = initialRoom.centerY()* gp.tileSize;
        hitBoxWalls = new Rectangle(4,8,8,8);
        hitBox = new Rectangle(x*gp.tileSize,y*gp.tileSize,16,16);

        level = 1;
        exp = 0;
        expNextLevel = 10;
        damage = 5;
        armor = 0;
        hpMax = 10;
        hp = hpMax;
        speed = 2;

        directionSprite = "right";
    }

    /**
     * Este metodo carga las imagenes del jugador de la carpeta de recursos
     */
    public void getPlayerImage(){
        try{
            right1 = ImageIO.read(getClass().getResourceAsStream("/player/PlayerRight.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/player/PlayerRight1.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/player/PlayerLeft.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/player/PlayerLeft1.png"));
        }catch (IOException e ){
            e.printStackTrace();
        }
    }

    public Rectangle getHitBox() {
        return new Rectangle(hitBoxWalls.x, hitBoxWalls.y, hitBoxWalls.width, hitBoxWalls.height);
    }

    /**
     * @param amount Cantidad de experiencia que el jugador recibe
     * @apiNote si la cantidad de experiencia actual excede la experiencia
     * necesaria para subir de nivel, el jugador sube de nivel; levelUp()
     */
    public void gainExperience(int amount) {
        exp += amount;

        while (exp >= expNextLevel) {
            exp -= expNextLevel;
            levelUp();
        }
    }

    /**
     * El jugador aumenta en 1 el nivel y todas sus estadisticas aumentan
     */
    private void levelUp() {
        level++;
        expNextLevel += 10 * level;

        // Mejora de estadísticas
        hpMax += 10;
        hp = hpMax; // Curarse al subir de nivel

        damage += 2;
        armor += 1;

        gp.ui.addMessage("Level up!");
    }

    public boolean isPlayerInRange() {
        // Calcula distancia entre este enemigo y el jugador
        float dx = this.x - gp.player.x;
        float dy = this.y - gp.player.y;
        double distance = Math.sqrt(dx*dx + dy*dy);

        // Solo devuelve true si el jugador está lo suficientemente cerca
        return distance < 2 * 16;
    }


    /**
     * Capta cada accion que realiza el jugador para cambiar su posicion ademas
     * de evaluar las colisiones del mapa
     */
    public void update() {
        if (gp.gameState != statePlay) return;
        dy = 0;
        dx = 0;

        updateHitbox();

        if (keyH.pressed){
            if (keyH.upPressed){
                dy = -speed;
            }
            if (keyH.downPressed){
                dy = speed;
            }
            if (keyH.leftPressed){
                dx = -speed;
                directionSprite = "left";
            }
            if (keyH.rightPressed){
                dx = speed;
                directionSprite = "right";
            }

            gp.cChecker.checkTile(this);


            if (!collisionOn) {
                x += dx;
                y += dy;
            }



            collisionOn = false;

            spriteCounter++;
            if (spriteCounter > 8) {
                spriteNum = (spriteNum + 1) % 2;
                spriteCounter = 0;
            }
        }

    }

    /**
     * @param g2 Objeto que captura la instancia de un objeto y lo 'dibuja' en la pantalla.
     * La funcion draw ilustra la entidad a la pantalla conociendo su ubicacion,
     *           tamaño e imagen
     */
    public void draw(Graphics g2) {
        //Dibujar el jugador
        BufferedImage image = null; //Imagen actual

        switch (directionSprite) {
            case "left":
                if (spriteNum == 0) {
                    image = left1;
                }
                else if (spriteNum == 1) {
                    image = left2;
                }
                break;
            case "right":
                if (spriteNum == 0) {
                    image = right2;
                }
                else if (spriteNum == 1) {
                    image = right1;
                }
                break;
        }
        g2.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);

    }

}
