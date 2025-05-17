package main;

import object.HealthPack;

public class AssetSetter {
    GamePanel gp;
    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }
    public void setObject() {
        gp.obj[0] = new HealthPack();
        gp.obj[1] = new HealthPack();
    }
}
