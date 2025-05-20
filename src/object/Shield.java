package object;


import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class Shield extends InteractiveObject{
    private int upgrade = 1;
    public Shield(int x, int y, GamePanel gp) {
        super(x,y,gp);
        this.name = "Shield";
        try {
            img = ImageIO.read(getClass().getResourceAsStream("/objects/shield.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    @Override
    public void trigger() {
        triggered = true;
        gp.ui.addMessage("You found a shield: +"+ upgrade +" Armor",Color.WHITE);
        gp.player.armor += upgrade;
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.drawImage(img, x, y, gp.tileSize, gp.tileSize, null);
    }
}
