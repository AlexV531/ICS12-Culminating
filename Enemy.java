import javax.swing.*;
import java.awt.*;
import java.util.*;

public class Enemy extends PhysicsObject {
  
  static Player target = new Player();
  
  Vector2 velo = new Vector2();
  static double maxSpeed = 150;
  double hp = 10;

  boolean dead = true;

  Vector2 spawnPoint;
  
  static int enemiesShot = 0; // Counts how many enemies are shot this frame

  // Animation Variables
  int legCount = 0;
  double legModY = 0;
  Image possessedLeft, possessedRight, possessedLL, possessedRL, currentImage;

  // Bullet Trail Variables(This is jank maybe remove later)
  ArrayList<Vector2> bulletPoints = new ArrayList<Vector2>();

  
  public Enemy(Vector2 startPos, int s, double mass) { 
    super(startPos, s, mass);
    
    spawnPoint = startPos;

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

  public void chooseTarget(Player player) {
    
    target = player;
  }
  
  public Vector2 getCentre() {
    
    return new Vector2(p.x + size/2, p.y + size/2);
  }
  
  public void calcPos(double deltaTime) {

    if(dead) {
      return;
    }
    
    velo.x = maxSpeed * deltaTime;
    velo.y = VMath.getAngleBetweenPoints(getCentre(), target.getCentre());
    
    p = VMath.addVectors(VMath.polarToCart(velo), p);

    // Manages collisions between the player and the enemies (should move elsewhere, enemy class?)
    if(VMath.getDistanceBetweenPoints(target.getCentre(), getCentre()) < target.size/2 + size/2) {
        
      target.addForce(new Vector2(6000, VMath.getAngleBetweenPoints(getCentre(), target.getCentre())));
      addForce(new Vector2(2000, VMath.getAngleBetweenPoints(target.getCentre(), getCentre())));
      
      target.takeDamage(10);
    }

    if(hp <= 0) {
      dead = true;
      p.x = 2000;
      p.y = 2000;
      // Adds to the enemies death count
      EnemyManager.enemyDead();
      //System.out.println("Enemy killed");
    }

    super.calcPos(deltaTime);
    
  }

  public double collisionCheck(Vector2 playerPos, Vector2 bullet) {
    
    Vector2 bulletPos = new Vector2();
    
    bulletPos.x = playerPos.x + bullet.x;
    bulletPos.y = playerPos.y + bullet.y;
    //System.out.println(enemiesShot);
    if(VMath.lineCircle(playerPos, bulletPos, getCentre(), size/2) && enemiesShot <= target.piercing) {
      // For calculating bullets piercing enemies
      enemiesShot++;
      // Subtracts health
      hp -= target.damage;
      // Adds a force to the enemy
      addForce(new Vector2(target.power, VMath.getAngleBetweenPoints(playerPos, bulletPos)));
      
      return VMath.getDistanceBetweenPoints(target.p, p);
    }
    return 0;
  }

  public void spawn() {
    hp = 10;
    dead = false;
    p.x = spawnPoint.x;
    p.y = spawnPoint.y;
    v.setToZero();
    a.setToZero();
    dead = false;
  }
  
  public String toString() {
    return "" + VMath.getDistanceBetweenPoints(target.p, p);
  }
  
  
  
}
