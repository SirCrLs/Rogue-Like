package object;

import main.GamePanel;
import map.Map;
import map.Room;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class Orb extends InteractiveObject {

    public Orb(int x, int y, GamePanel gp) {
        super(x, y, gp);
        this.name = "Orb";
        try {
            img = ImageIO.read(getClass().getResourceAsStream("/objects/orb.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void trigger() {
        if (!triggered) {
            triggered = true;
            gp.ui.addMessage("You found the Orb!",Color.BLUE);
            gp.player.gainExperience(20);
            gp.mapLevel++;
            gp.gameMap = new Map(gp.mapLevel,gp);
            int px, py;
            Room initialRoom = gp.gameMap.rooms.getFirst();
            px =initialRoom.centerX()*gp.tileSize;
            py =initialRoom.centerY()*gp.tileSize;
            gp.player.x = px;
            gp.player.y = py;

        }
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.drawImage(img, x, y, gp.tileSize, gp.tileSize, null);
    }
}

