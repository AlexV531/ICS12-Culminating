import javax.swing.*;
import java.awt.*;

public class ShootingEnemy extends Enemy{

    static double maxSpeed = 120;

    // Timer Variables
    double currentTime = 0; // Timer
    double shootingDelay = 4; // Time between shooting animations
    double activationDelay = 1; // Time between the start of the animation and the end
    boolean shooting = false; // If the animation is in progress, this is true

    // Animation Variables
    int legCount = 0;
    double legModY = 0;
    Image summonerIdleLeft, summonerIdleRight, summonerActivatedLeft, summonerActivatedRight, summonerLL, summonerRL, currentImage;

    public ShootingEnemy(Vector2 startPos, int s, double mass) {
        super(startPos, s, mass, 20);

        ImageIcon eL = new ImageIcon("images/SummonerIdle-Left.png");
        summonerIdleLeft = eL.getImage();
        ImageIcon eR = new ImageIcon("images/SummonerIdle-Right.png");
        summonerIdleRight = eR.getImage();
        ImageIcon eAL = new ImageIcon("images/SummonerActivated-Left.png");
        summonerActivatedLeft = eAL.getImage();
        ImageIcon eAR = new ImageIcon("images/SummonerActivated-Right.png");
        summonerActivatedRight = eAR.getImage();
        currentImage = summonerIdleLeft;

        ImageIcon eLL = new ImageIcon("images/Summoner-LeftLeg.png");
        summonerLL = eLL.getImage();
        ImageIcon eRL = new ImageIcon("images/Summoner-RightLeg.png");
        summonerRL = eRL.getImage();
    }

    public void calcPos(double deltaTime) {

        if(dead) {
            return;
        }

        currentTime += deltaTime;
        // Animation starts
        if(currentTime >= shootingDelay && !shooting) {
            shooting = true;
            currentTime = 0;
        }
        // After animation, a projectile is fired
        if(currentTime >= activationDelay && shooting) {
            EnemyManager.activateProjectile(target.getCentre(), new Vector2(p.x + 8, p.y + 8));
            shooting = false;
            currentTime = 0;
        } 

        if(distanceFromTarget > 500) {
            velo.x = maxSpeed * deltaTime;
            velo.y = VMath.getAngleBetweenPoints(getCentre(), target.getCentre());
        }
        else if (distanceFromTarget < 400) {
            velo.x = maxSpeed/2 * deltaTime;
            velo.y = VMath.getAngleBetweenPoints(target.getCentre(), getCentre());
        }
        else {
            velo.x = 0;
            velo.y = 0;
        }

        p = VMath.addVectors(VMath.polarToCart(velo), p);

        super.calcPos(deltaTime);

    }

    public void drawEnemy(Graphics2D g2D) {

        if(dead) {
            return;
        }

        if(velo.x != 0) {

            legCount++;
            if(legCount > 10000) {
            legCount = 0;
            }
            legModY = Math.sin(legCount * 1/1.2) * 6;
        }

        if(target.p.x >= p.x) {
            if(shooting) {
                currentImage = summonerActivatedRight;
            }
            else {
                currentImage = summonerIdleRight;
            }
        }
        else if (target.p.x < p.x) {
            if(shooting) {
                currentImage = summonerActivatedLeft;
            }
            else {
                currentImage = summonerIdleLeft;
            }
        }

        g2D.drawImage(summonerLL, (int)(p.x), (int)(p.y + legModY), null);
        g2D.drawImage(summonerRL, (int)(p.x), (int)(p.y - legModY), null);

        g2D.drawImage(currentImage, (int)p.x, (int)p.y, null);

    }
}