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
            ImageIcon ic = new ImageIcon("images/HealthPowerUp.png");
            img = ic.getImage();
        }
        else if(function == 1) {
            ImageIcon ic = new ImageIcon("images/RateOfFire.png");
            img = ic.getImage();
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
            else if(function == 1) {
                PlayerManager.activateStatusEffect(1, 0.5, 5);
            }
            active = false;
        }
    }

    public void drawPowerUp(Graphics g2D) {

        if(!active) {
            return;
        }
        
        g2D.drawImage(img, (int)p.x, (int)p.y, null);
        


    }

}