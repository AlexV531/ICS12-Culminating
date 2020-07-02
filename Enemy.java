import java.awt.*;
import java.util.*;

public class Enemy extends PhysicsObject {
  
  static Player target = new Player();
  
  Vector2 velo = new Vector2();
  double hp;
  double maxHP;

  double distanceFromTarget;

  boolean dead = true;

  Vector2 spawnPoint;
  
  static int enemiesShot = 0; // Counts how many enemies are shot this frame

  // Animation Variables
  //int legCount = 0;
  //double legModY = 0;
  //Image possessedLeft, possessedRight, possessedLL, possessedRL, currentImage;

  // Bullet Trail Variables(This is jank maybe remove later)
  ArrayList<Vector2> bulletPoints = new ArrayList<Vector2>();

  
  public Enemy(Vector2 startPos, int s, double mass, double healthPoints) { 
    super(startPos, s, mass);
    maxHP = healthPoints;
    hp = healthPoints;
    spawnPoint = startPos;
  }
  
  public void drawEnemy(Graphics2D g2D) {
   
    if(dead) {
      return;
    }

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

    distanceFromTarget = VMath.getDistanceBetweenPoints(target.getCentre(), getCentre());

    // Manages collisions between the player and the enemies
    if(distanceFromTarget < target.size/2 + size/2) {
        
      target.addForce(new Vector2(6000, VMath.getAngleBetweenPoints(getCentre(), target.getCentre())));
      addForce(new Vector2(2000, VMath.getAngleBetweenPoints(target.getCentre(), getCentre())));
      
      target.takeDamage(10);
    }

    // If the enemy dies
    if(hp <= 0) {
      dead = true;
      Vector2 deathPos = new Vector2();
      deathPos.x = p.x;
      deathPos.y = p.y;
      if(Math.random() < 0.10) {
        // Activates a power up on the enemy's position
        EnemyManager.activatePowerUp(deathPos, 1);
      }
      
      // Moves the enemies to some "Graveyard" position so they don't interfere with the game after death
      p.x = 2000;
      p.y = 2000;
      // Adds to the enemies death count
      EnemyManager.enemyDead();
      //System.out.println("Enemy killed");
      

    }

    // Physics object calculations
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

      EnemyManager.activateParticleEffect(p);
      
      return VMath.getDistanceBetweenPoints(target.p, p);
    }
    return 0;
  }

  public void spawn() {
    hp = maxHP;
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
