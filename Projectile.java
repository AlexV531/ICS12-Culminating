import javax.swing.*;
import java.awt.*;

public class Projectile {
    
    Vector2 p = new Vector2();
    Vector2 v = new Vector2();
    double maxSpeed = 200;
    double angle;
    static int size = 16; 

    static Player target;

    boolean active = false;

    Image projectileImage;

    public Projectile() {
        ImageIcon proj = new ImageIcon("images/Projectile01.png");
        projectileImage = proj.getImage();
    }

    public boolean isActive() {
        return active;
    }

    public Vector2 getCentre() {
        return new Vector2(p.x + size/2, p.y + size/2);
    }

    public void calcPos(double deltaTime) {

        if(active) {
            v.x = maxSpeed * deltaTime;
            v.y = angle;

            p = VMath.addVectors(VMath.polarToCart(v), p);

            // Manages collisions between the player and the enemies
            if(VMath.getDistanceBetweenPoints(target.getCentre(), getCentre()) < target.size/2 + size/2) {
                
                target.addForce(new Vector2(6000, VMath.getAngleBetweenPoints(getCentre(), target.getCentre())));
                
                target.takeDamage(10);
                active = false;
            }

            if(p.x > 1000 || p.x < -1000 || p.y > 1000 || p.y < -1000) {
                active = false;
            }
        }
    }

    public void activate(Vector2 targetPos, Vector2 startPos) {
        p = startPos;
        // targetPos must be the player's centre, not it's position
        angle = VMath.getAngleBetweenPoints(getCentre(), targetPos);
        active = true;
    }

    public void drawPojectile(Graphics2D g2D) {

        if(active) {
            g2D.drawImage(projectileImage, (int)p.x, (int)p.y, null);
        }
    }

}