package entity;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Clase padre para toda entidad.
 *
 */
//Esta sera la clase padre para cualquier entidad del juego
public class Entity {
    public int x,y;
    public int dx,dy; //desplazamiento
    public int speed;
    public boolean alive = true;

    public int level;
    public int hp;
    public int hpMax;
    public int damage;

    public BufferedImage left1, right1, left2, right2;
    public String directionSprite;

    public int spriteCounter = 0;
    public int spriteNum = 0;

    public Rectangle hitBoxWalls;
    public Rectangle hitBox;
    public boolean collisionOn=false;

    /**
     * Actualiza la posicion de la hitbox a la posicion de la entidad
     */
    public void updateHitbox(){
        hitBox.x = x;
        hitBox.y = y;
    }
}
