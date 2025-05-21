import static org.junit.jupiter.api.Assertions.*;

import entity.Player;
import main.GamePanel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PlayerTest {
    private Player player;
    private GamePanel gp;

    @BeforeEach
    public void setup() {
        gp = new  GamePanel();
        player = new Player(gp,null);
    }

    @Test
    public void testSetDefaultValues() {
        player.setDefaultValues();

        assertNotNull(player.hitBoxWalls);
        assertNotNull(player.hitBox);
        assertEquals(1, player.level);
        assertEquals(0, player.exp);
        assertEquals(10, player.expNextLevel);
        assertEquals(2, player.damage);
        assertEquals(0, player.armor);
        assertEquals(10, player.hpMax);
        assertEquals(10, player.hp);
        assertEquals(2, player.speed);
        assertEquals("right", player.directionSprite);
    }

    @Test
    public void testGainExperience() {
        player.setDefaultValues();

        assertEquals(1, player.level);
        assertEquals(0, player.exp);

        player.gainExperience(5);

        assertEquals(5, player.exp);
        assertEquals(1, player.level);

        //sube de nivel con al menos 10 de experiencia
        player.gainExperience(6);

        assertEquals(1, player.exp);
        assertEquals(2, player.level);
        assertEquals(20, player.hpMax);
        assertEquals(20, player.hp);
        assertEquals(4, player.damage);
        assertEquals(1, player.armor);
    }
}