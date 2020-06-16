import javax.swing.*;
import java.awt.*;

public class ChasingEnemy extends Enemy{
        
    static double maxSpeed = 150;

    // Animation Variables
    int legCount = 0;
    double legModY = 0;
    Image possessedLeft, possessedRight, possessedLL, possessedRL, currentImage;

    public ChasingEnemy(Vector2 startPos, int s, double mass) {
        super(startPos, s, mass, 10);

        ImageIcon eL = new ImageIcon("images/Possessed01-Left.png");
        possessedLeft = eL.getImage();
        ImageIcon eR = new ImageIcon("images/Possessed01-Right.png");
        possessedRight = eR.getImage();
        currentImage = possessedLeft;

        ImageIcon eLL = new ImageIcon("images/Soldier01-LeftLeg.png");
        possessedLL = eLL.getImage();
        ImageIcon eRL = new ImageIcon("images/Soldier01-RightLeg.png");
        possessedRL = eRL.getImage();
    }

    public void drawEnemy(Graphics2D g2D) {

        if(dead) {
            return;
        }

        legCount++;
        if(legCount > 10000) {
        legCount = 0;
        }
        legModY = Math.sin(legCount * 1/1.2) * 6;

        if(target.p.x >= p.x) {
        currentImage = possessedRight;
        }
        else if (target.p.x < p.x) {
        currentImage = possessedLeft;
        }

        g2D.drawImage(possessedLL, (int)(p.x), (int)(p.y + legModY), null);
        g2D.drawImage(possessedRL, (int)(p.x), (int)(p.y - legModY), null);

        g2D.drawImage(currentImage, (int)p.x, (int)p.y, null);

        

    }

    public void calcPos(double deltaTime) {

        if(dead) {
            return;
        }

        velo.x = maxSpeed * deltaTime;
        velo.y = VMath.getAngleBetweenPoints(getCentre(), target.getCentre());
    
        p = VMath.addVectors(VMath.polarToCart(velo), p);

        super.calcPos(deltaTime);

    }

}