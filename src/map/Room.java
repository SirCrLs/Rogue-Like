package map;

import java.util.Random;

public class Room {
    int x, y, width, height;

    public Room(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * @return Retorna la posicion central x de la habitacion
     */
    public int centerX() {
        return x + width / 2;
    }
    /**
     * @return Retorna la posicion central y de la habitacion
     */
    public int centerY() {
        return y + height / 2;
    }


}
