package main;

import entity.Player;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

import static main.GamePanel.stateCombat;

public class UI {
    GamePanel gp;
    Font fuente20;
    Font fuente10;

    private class Message {
        String text;
        int timer;//Duracion del mensaje
        Color color;
        public Message(String text) {
            this.text = text;
            this.timer = 300;
        }
        public Message(String text, Color color) {
            this.text = text;
            this.timer = 300;
            this.color = color;
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
    public void addMessage(String text, Color color) {
        messages.add(new Message(text, color));
    }

public void drawGameOver(Graphics g2) {
    // Game over
    g2.setColor(new Color(0, 0, 0, 150)); // Negro con 60% de opacidad
    g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
    g2.setColor(Color.RED);
    String text = "GAME OVER";
    int textWidth = g2.getFontMetrics().stringWidth(text);
    g2.drawString(text, (gp.screenWidth - textWidth) / 2, gp.screenHeight / 2);

    // Mensaje de reinicio
    g2.setFont(new Font("Consolas", Font.PLAIN, 20));
    g2.setColor(Color.WHITE);
    String restartText = "Presiona [R] para reiniciar";
    int restartWidth = g2.getFontMetrics().stringWidth(restartText);
    g2.drawString(restartText, (gp.screenWidth - restartWidth) / 2, gp.screenHeight / 2 + 50);
}

    /**
     * @param g2 El graficador que ilustra el UI
     * @param player Clase jugador
     * @apiNote Ilustra en la pantalla las estadisticas del jugador, acciones y eventos realizados
     * y algunos datos extras
     */
    public void draw(Graphics2D g2, Player player) {
        Color bgColor = new Color(20, 20, 20, 128);
        int x = 15;
        int y = gp.screenHeight - 15;
        // Fondo
        int padding = 10;
        int width = 510;
        int height = 40;
        g2.setColor(bgColor);
        g2.fillRoundRect(x - padding/2, y - height + padding/2, width, height, 10, 10);

        g2.setFont(fuente10);
        g2.setColor(Color.WHITE);

        g2.drawString("Seed: " + gp.gameMap.getSeed(), 10, 20);
        g2.setFont(fuente20);


        //HUD del jugador
        g2.drawString("Level: " + player.level, x, y-20);
        g2.drawString("Damage: " + player.damage, x, y);
        g2.drawString("Armor: " + player.armor, x + 120, y);
        g2.drawString("HP: " + player.hp + "/" + player.hpMax, x + 400, y);

        if (gp.gameState == stateCombat){
            g2.setColor(Color.RED);
            int ey, ex;
            ey = y-20;
            ex = x+200;
            g2.drawString("Slime Level: " + gp.currentEnemy.level, ex, ey);
            g2.drawString("HP: " + gp.currentEnemy.hp + "/" + gp.currentEnemy.hpMax, ex + 200, ey);
            g2.setColor(Color.WHITE);
        }

        //HUD de mensajes
        int msgX = gp.screenWidth - 350;
        int msgY = y;
        Iterator<Message> iterator = messages.iterator();
        while (iterator.hasNext()) {
            Message m = iterator.next();
            g2.setColor(m.color);
            g2.drawString(m.text, msgX, msgY);
            msgY -= 25;
            m.timer--;
            if (m.timer <= 0) {
                iterator.remove();
            }
            g2.setColor(Color.WHITE);
        }
        if (gp.gameState == gp.stateGameOver) {
            drawGameOver(g2);
        }
    }
}

