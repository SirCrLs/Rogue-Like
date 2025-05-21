package object;
import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class InteractiveObject {
    public Rectangle hitBox;
    public String name;
    public int x, y;
    public boolean triggered = false;
    BufferedImage img;
    GamePanel gp;

    public InteractiveObject(int x, int y, GamePanel gp) {
        this.x = x;
        this.y = y;
        this.gp = gp;
        hitBox = new Rectangle(x, y, gp.tileSize, gp.tileSize);
    }

    /**
     * Ejecuta las acciones asignadas al objeto
     */
    public abstract void trigger();

    /**
     * @param g2 Graficador
     * @apiNote Ilustra el objeto en pantalla
     */
    public abstract void draw(Graphics2D g2);
}

