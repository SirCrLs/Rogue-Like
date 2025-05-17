package entity;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import static main.GamePanel.stateCombat;
import static main.GamePanel.statePlay;

public class Enemy extends Entity {
    public int reward;
    private Rectangle patrolArea;
    private int moveCooldown = 0;
    GamePanel gp;

    BufferedImage[] walkSprites;
    int spriteIndex = 0;
    int spriteCounter = 0;

    public Enemy(int x, int y, Rectangle patrolArea, GamePanel gp) {
        this.gp = gp;
        this.x = x;
        this.y = y;
        this.patrolArea = patrolArea;
        this.speed = 2;
        this.hitBox = new Rectangle(4, 8, 8, 8);

        setDefaultValues(1);

        walkSprites = new BufferedImage[2];
        try {
            walkSprites[0] = ImageIO.read(getClass().getResourceAsStream("/Enemy/slime1.png"));
            walkSprites[1] = ImageIO.read(getClass().getResourceAsStream("/Enemy/slime2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setDefaultValues(int lvl) {
        this.level = lvl;
        this.hpMax = 5 + (10*(lvl-1));
        this.hp = hpMax;
        this.damage = 3 + (5*(lvl-1));
        this.reward = 2 + (2*(lvl-1)); //Experiencia
    }

    public boolean isDead() {
        if (this.hp <= 0) {
            return true;
        }
        return false;
    }

    /**
     * Actualiza posicion, velocidad, estado y colisiones del enemigo
     */
    public void update() {
        if (gp.gameState != statePlay) return;
        if (moveCooldown > 0) {
            moveCooldown--;
            return;
        }

        // Movimiento aleatorio
        int dir = (int) (Math.random() * 4);
        dx = 0;
        dy = 0;

        switch (dir) {
            case 0: dy = -speed; break; // up
            case 1: dy = speed; break;  // down
            case 2: dx = -speed; break; // left
            case 3: dx = speed; break;  // right
        }

        gp.cChecker.checkTile(this);

        if (gp.gameState == statePlay && !this.isDead() && gp.currentEnemy == null) {
            if (this.hitBox.intersects(gp.player.hitBox)) {
                gp.currentEnemy = this;
                gp.gameState = stateCombat;
                gp.combatTimer = 0;
            }
        }



        if (!collisionOn) {
            x += dx;
            y += dy;
        }

        collisionOn = false;

        // Comprobar si el nuevo movimiento lo mantendría dentro del area de patrulla
        if (patrolArea.contains(x + dx, y + dy)) {
            x += dx;
            y += dy;
        }

        moveCooldown = 20 + (int)(Math.random() * 30); // Pausa entre movimientos

        spriteCounter++;
        if (spriteCounter > 3) {
            spriteIndex = (spriteIndex + 1) % walkSprites.length;
            spriteCounter = 0;
        }
    }

    public void draw(Graphics2D g2) {
        g2.drawImage(walkSprites[spriteIndex], x, y, gp.tileSize, gp.tileSize, null);
    }
}
