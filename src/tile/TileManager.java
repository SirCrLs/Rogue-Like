package tile;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class TileManager {

    GamePanel gp;
    public Tile[] tile;


    public TileManager(GamePanel gp) {
        this.gp = gp;
        tile = new Tile[10];


        getTileImage();
    }

    /**
     *  Carga las imagenes de los fondos o paredes de los recursos a variables.
     */
    public void getTileImage(){
        try {
            for(int i = 0; i < 10; i++){
                tile[i] = new Tile();

            }
            tile[0].image = ImageIO.read(getClass().getResourceAsStream("/tiles/void.png"));
            tile[1].image = ImageIO.read(getClass().getResourceAsStream("/tiles/wall.png"));
            tile[2].image = ImageIO.read(getClass().getResourceAsStream("/tiles/trail.png"));
            tile[3].image = ImageIO.read(getClass().getResourceAsStream("/tiles/floor.png"));

            tile[4].image = ImageIO.read(getClass().getResourceAsStream("/tiles/wallDown.png"));
            tile[4].collision = true;
            tile[5].image = ImageIO.read(getClass().getResourceAsStream("/tiles/wallUp.png"));
            tile[5].collision = true;
            tile[6].image = ImageIO.read(getClass().getResourceAsStream("/tiles/wallLeft.png"));
            tile[6].collision = true;
            tile[7].image = ImageIO.read(getClass().getResourceAsStream("/tiles/wallRight.png"));
            tile[7].collision = true;

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void draw(Graphics g2){

    }
}
