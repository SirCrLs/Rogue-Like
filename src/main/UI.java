package main;

import entity.Player;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

public class UI {
    GamePanel gp;
    Font fuente20;
    Font fuente10;

    private class Message {
        String text;
        int timer;//Duracion del mensaje
        public Message(String text) {
            this.text = text;
            this.timer = 300;
        }
    }

    private ArrayList<Message> messages = new ArrayList<>();

    public UI(GamePanel gp) {
        this.gp = gp;
        fuente20 = new Font("Consolas", Font.PLAIN, 20);
        fuente10 = new Font("Consolas", Font.PLAIN, 15);
    }

    /**
     * @param text texto String que se guardara como clase Mensaje
     */
    public void addMessage(String text) {
        messages.add(new Message(text));
    }

    /**
     * @param g2 El graficador que ilustra el UI
     * @param player Clase jugador
     * @apiNote Ilustra en la pantalla las estadisticas del jugador, acciones y eventos realizados
     * y algunos datos extras
     */
    public void draw(Graphics2D g2, Player player) {
        g2.setFont(fuente10);
        g2.setColor(Color.WHITE);

        g2.drawString("Seed: " + gp.gameMap.getSeed(), 10, 20);
        g2.setFont(fuente20);

        int x = 15;
        int y = gp.screenHeight - 15;
        //HUD del jugador
        g2.drawString("Level: " + player.level, x, y);
        g2.drawString("HP: " + player.hp + "/" + player.hpMax, x + 120, y);
        g2.drawString("Damage: " + player.damage, x + 270, y);
        g2.drawString("Armor: " + player.armor, x + 400, y);

        //HUD de mensajes
        int msgX = gp.screenWidth - 350;
        int msgY = y;
        Iterator<Message> iterator = messages.iterator();
        while (iterator.hasNext()) {
            Message m = iterator.next();
            g2.drawString(m.text, msgX, msgY);
            msgY -= 25;
            m.timer--;
            if (m.timer <= 0) {
                iterator.remove();
            }
        }
    }
}

