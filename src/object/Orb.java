package object;

import main.GamePanel;

import java.awt.*;

public class Orb extends InteractiveObject {

    public Orb(int x, int y, GamePanel gp) {
        super(x, y, gp);
        this.name = "Orb";
    }

    @Override
    public void trigger() {
        if (!triggered) {
            triggered = true;
            gp.ui.addMessage("¡Has encontrado un orbe!");
            gp.player.gainExperience(50);
            // Puedes agregar animaciones, ítems, sonidos, etc.
        }
    }

    @Override
    public void draw(Graphics2D g2) {

    }
}

