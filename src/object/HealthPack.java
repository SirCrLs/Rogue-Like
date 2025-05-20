package object;

import main.GamePanel;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class HealthPack extends InteractiveObject{
    public HealthPack(int x, int y, GamePanel gp) {
        super(x,y,gp);
        this.name = "Health Pack";
        try {
            img = ImageIO.read(getClass().getResourceAsStream("/objects/healthPack.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void trigger() {

    }

    /**
     * @param g2 Graficador que ilustrara el objeto en pantalla
     */
    public void draw(Graphics2D g2) {
        g2.drawImage(img, x, y, gp.tileSize, gp.tileSize, null);
    }
}
