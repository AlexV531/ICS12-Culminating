import java.awt.*;

public class PlayerManager {
    // Player
    static Player player;
    static Vector2 mousePos;
    // Status Effects
    static StatusEffect[] effectList = new StatusEffect[10];

    public static Player getPlayer() {
        return player;
    }

    public static Vector2 getCentre() {
        return player.getCentre();
    }

    public static void createPlayer() {
        player = new Player(new Vector2(0, 0), 64, 10);
        for(int i = 0; i < effectList.length; i++) {
            effectList[i] = new StatusEffect();
        }
        StatusEffect.target = player;
    }

    public static boolean activateStatusEffect(int func, double modifier, double delay) {
        // Runs through each status effect
        for(int i = 0; i < effectList.length; i++) {
            // When (or if) it finds a status effect that isn't active, it activates it with a target and starting position
            if(!effectList[i].isActive()) {
                effectList[i].activate(func, modifier, delay);
                return true;
            }
        }
        return false;
    }

    public static boolean calcPos(double deltaTime, Vector2 mousePos) {
        for(int i = 0; i < effectList.length; i++) {
            effectList[i].calcTimer(deltaTime);;
        }
        return player.calcPos(deltaTime, mousePos);
        
    }

    public static void drawPlayer(Graphics2D g2D, int CANVAS_WIDTH, int CANVAS_HEIGHT) {
        player.drawPlayer(g2D, CANVAS_WIDTH, CANVAS_HEIGHT);
    }

    public static void switchWeapon(int weaponID) {
        player.switchWeapon(weaponID);
    }

    public static void shoot(Graphics2D g2D) {
        player.shoot(g2D);
    }

    public static void reset() {
        player.reset();
    }
}