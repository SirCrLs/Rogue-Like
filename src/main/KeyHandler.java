package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Esta clase recibe el input del teclado del jugador como un evento.
 * Es necesaria para que el usuario pueda interactuar con el sistema
 */
//La clase que acepta los inputs de teclas de los jugadores
public class KeyHandler implements KeyListener {
    public boolean upPressed, downPressed, leftPressed, rightPressed,pressed;

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        pressed = true;
        if (keyCode == KeyEvent.VK_W) {
            upPressed = true;
        }
        if (keyCode == KeyEvent.VK_S) {
            downPressed = true;
        }
        if (keyCode == KeyEvent.VK_A) {
            leftPressed = true;
        }
        if (keyCode == KeyEvent.VK_D) {
            rightPressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_W) {
            upPressed = false;
        }
        if (keyCode == KeyEvent.VK_S) {
            downPressed = false;
        }
        if (keyCode == KeyEvent.VK_A) {
            leftPressed = false;
        }
        if (keyCode == KeyEvent.VK_D) {
            rightPressed = false;
        }
        if (!upPressed && !downPressed && !leftPressed && !rightPressed) {
            pressed = false;
        }
    }
}
