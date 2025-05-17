package object;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import java.io.IOException;

public class HealthPack extends SuperObject{
    public HealthPack() {
        name = "Health Pack";
        try {
            img = ImageIO.read(getClass().getResourceAsStream("/objects/healthPack.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
