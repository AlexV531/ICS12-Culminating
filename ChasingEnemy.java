import javax.swing.*;
import java.awt.*;

public class ChasingEnemy extends PhysicsObject {
  
  Player target = new Player();
  
  Vector2 velo = new Vector2();
  static double maxSpeed = 100;
  double hp = 10;

  // Animation Variables
  int legCount = 0;
  double legModY = 0;
  Image possessedLeft, possessedRight, possessedLL, possessedRL, currentImage;
  
  public ChasingEnemy(Vector2 startPos, int s, double mass) { 
    super(startPos, s, mass);
    
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

  public void chooseTarget(Player player) {
    
    target = player;
  }
  
  public Vector2 getCentre() {
    
    return new Vector2(p.x + size/2, p.y + size/2);
  }
  
  public void calcPos(double deltaTime) {
    
    velo.x = maxSpeed * deltaTime;
    velo.y = VMath.getAngleBetweenPoints(getCentre(), target.getCentre());
    
    p = VMath.addVectors(VMath.polarToCart(velo), p);

    super.calcPos(deltaTime);
    
  }
  
  public void collisionCheck(Vector2 playerPos, Vector2 bullet) {
    
    Vector2 bulletPos = new Vector2();
    
    bulletPos.x = playerPos.x + bullet.x;
    bulletPos.y = playerPos.y + bullet.y;
    
    if(VMath.lineCircle(playerPos, bulletPos, getCentre(), size/2)) {
      // subtracts health
      hp -= target.damage;
      // adds a force to the enemy
      addForce(new Vector2(target.power, VMath.getAngleBetweenPoints(playerPos, bulletPos)));
    }
  }
  
  
  
  
}
