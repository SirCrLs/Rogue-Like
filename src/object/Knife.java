package object;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class Knife extends InteractiveObject{
    private int upgrade = 1;
    public Knife(int x, int y, GamePanel gp) {
        super(x,y,gp);
        this.name = "Knife";
        try {
            img = ImageIO.read(getClass().getResourceAsStream("/objects/knife.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    @Override
    public void trigger() {
        triggered = true;
        gp.ui.addMessage("You found a knife: +"+ upgrade +" Damage",Color.WHITE);
        gp.player.damage += upgrade;
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.drawImage(img, x, y, gp.tileSize, gp.tileSize, null);
    }
}
