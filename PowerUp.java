import javax.swing.*;
import java.awt.*;

public class PowerUp {
    
    Vector2 p = new Vector2();
    int size = 64;
    Image img;
    int function; // 0 = health
    boolean active = false;

    static Player target;

    public boolean isActive() {
        return active;
    }

    public void activate(Vector2 pos, int func) {

        p = pos;
        function = func;

        if(function == 0) {
            ImageIcon h = new ImageIcon("images/HealthPowerUp.png");
            img = h.getImage();
        }

        active = true;

    }

    public Vector2 getCentre() {

        return new Vector2(p.x + size/2, p.y + size/2);
    }

    public void calcPos() {

        if(!active) {
            return;
        }

        if(VMath.getDistanceBetweenPoints(target.getCentre(), getCentre()) < target.size/2 + size/2) {
                
            if(function == 0) {
                target.increaseHealth(10);
            }
            active = false;
        }
    }

    public void drawPowerUp(Graphics g2D) {

        if(!active) {
            return;
        }

        if(function == 0) {
            g2D.drawImage(img, (int)p.x, (int)p.y, null);
        }


    }

}