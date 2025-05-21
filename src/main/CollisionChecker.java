package main;

import entity.Entity;

public class CollisionChecker {
    GamePanel gp;
    public CollisionChecker(GamePanel gp){
        this.gp=gp;
    }

    /**
     * @param entity La entidad que se esta movienoo
     * @apiNote El metodo verifica si la siguiente 'tile'
     * a la que se dirige la entidad tiene colision o no,
     * si no tiene colisoon, puede pasar a traves de ella, si no, no
     */
    public void checkTile(Entity entity){
        entity.collisionOn = false;


        int entityLeftX = entity.x + entity.hitBoxWalls.x;
        int entityRightX = entity.x + entity.hitBoxWalls.x + entity.hitBoxWalls.width;
        int entityTopY = entity.y + entity.hitBoxWalls.y;
        int entityBottomY = entity.y + entity.hitBoxWalls.y + entity.hitBoxWalls.height;
        int entityLeftCol = entityLeftX / gp.tileSize;
        int entityRightCol = entityRightX / gp.tileSize;
        int entityTopRow = entityTopY / gp.tileSize;
        int entityBottomRow = entityBottomY / gp.tileSize;

        int tileNum1, tileNum2;


        if (entity.dy < 0) { // arriba
            int nextTopRow = (entityTopY + entity.dy) / gp.tileSize;
            tileNum1 = gp.gameMap.map[entityLeftCol][nextTopRow];
            tileNum2 = gp.gameMap.map[entityRightCol][nextTopRow];
            if (gp.TileM.tile[tileNum1].collision || gp.TileM.tile[tileNum2].collision) {
                entity.dy = 0;
            }
        } else if (entity.dy > 0) { // abajo
            int nextBottomRow = (entityBottomY + entity.dy) / gp.tileSize;
            tileNum1 = gp.gameMap.map[entityLeftCol][nextBottomRow];
            tileNum2 = gp.gameMap.map[entityRightCol][nextBottomRow];
            if (gp.TileM.tile[tileNum1].collision || gp.TileM.tile[tileNum2].collision) {
                entity.dy = 0;
            }
        }


        if (entity.dx < 0) { // izquierda
            int nextLeftCol = (entityLeftX + entity.dx) / gp.tileSize;
            tileNum1 = gp.gameMap.map[nextLeftCol][entityTopRow];
            tileNum2 = gp.gameMap.map[nextLeftCol][entityBottomRow];
            if (gp.TileM.tile[tileNum1].collision || gp.TileM.tile[tileNum2].collision) {
                entity.dx = 0;
            }
        } else if (entity.dx > 0) { // derecha
            int nextRightCol = (entityRightX + entity.dx) / gp.tileSize;
            tileNum1 = gp.gameMap.map[nextRightCol][entityTopRow];
            tileNum2 = gp.gameMap.map[nextRightCol][entityBottomRow];
            if (gp.TileM.tile[tileNum1].collision || gp.TileM.tile[tileNum2].collision) {
                entity.dx = 0;
            }
        }
    }
}
