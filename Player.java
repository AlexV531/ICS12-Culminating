import javax.swing.*;
import java.awt.*;


public class Player extends PhysicsObject {
  // Physics stuff
  Vector2 walkVelo = new Vector2();
  Vector2 walkAcc = new Vector2();
  double maxVelo;
  double maxAcc;
  boolean mUp, mLeft, mDown, mRight, shooting;
  // Weapon stats
  double damage = 10;
  double range = 500;
  double rateOfFire = 0.5;
  double power = 5000;
  // Personal stats
  double hp = 100;
  // Shooting variables
  double shootTimer = 0;
  Vector2 bullet = new Vector2();
  
  // Player sprites
  Image playerP1Image;
  Image playerLLImage;
  Image playerRLImage;
  Image playerLHImage;
  Image playerRHImage;
  // Animation Helpers
  double legCount = 0;
  double legMod = 0;

  public Player() { 
    super(new Vector2(0, 0), 0, 0);
    maxVelo = 300;
    maxAcc = 900;
    mUp = false;
    mDown = false;
    mLeft = false;
    mRight = false;
  }
  
  public Player(Vector2 startPos, int s, double mass) { 
    super(startPos, s, mass);
    maxVelo = 300;
    maxAcc = 900;
    mUp = false;
    mDown = false;
    mLeft = false;
    mRight = false;
    ImageIcon pP1 = new ImageIcon("Soldier01-P1-NoLimbs.png");
    playerP1Image = pP1.getImage();
    ImageIcon pLL = new ImageIcon("Soldier01-LeftLeg.png");
    playerLLImage = pLL.getImage();
    ImageIcon pRL = new ImageIcon("Soldier01-RightLeg.png");
    playerRLImage = pRL.getImage();
    ImageIcon pLH = new ImageIcon("Soldier01-LeftHand.png");
    playerLHImage = pLH.getImage();
    ImageIcon pRH = new ImageIcon("Soldier01-RightHand.png");
    playerRHImage = pRH.getImage();
  }
  
  public Vector2 getCentre() {
    
    return new Vector2(p.x + size/2, p.y + size/2);
  }
  
  public void drawPlayer(Graphics g) {
    if(mUp == true || mDown == true || mRight == true || mLeft == true) {
      legCount++;
      if(legCount > 10000) {
        legCount = 0;
      }
      legMod = Math.sin(legCount * 1/1.2) * 6;
    } 
    else {
      legMod = 0;
    }
    // Draws the player leg sprites
    g.drawImage(playerLLImage, (int)p.x, (int)(p.y + legMod), null);
    g.drawImage(playerRLImage, (int)p.x, (int)(p.y - legMod), null);
    // Draws the player sprite
    g.drawImage(playerP1Image, (int)p.x, (int)p.y, null);
    // Draws the player hand sprites
    g.drawImage(playerLHImage, (int)p.x, (int)p.y, null);
    g.drawImage(playerRHImage, (int)p.x, (int)p.y, null);

  }

  public void shoot(Vector2 mousePos) {
    
    if(shootTimer == rateOfFire) {
      // Recoil force
      addForce(new Vector2(10000, VMath.getAngleBetweenPoints(getCentre(), mousePos) - Math.PI));
      // Bullet Vector (polar)
      bullet.x = range;
      bullet.y = VMath.getAngleBetweenPoints(getCentre(), mousePos);
      // Bullet Vector (cartesian)
      bullet = VMath.polarToCart(bullet);
      // Resets the timer/shooting variable
      shootTimer = 0;
    }
    
  }
  
  private void shootTimer(double deltaTime) {
    
    if(shootTimer < rateOfFire) {
        shootTimer += deltaTime;
        if(shootTimer > rateOfFire) {
          shootTimer = rateOfFire;
        }
      }
  }
  
  public void calcPos(double deltaTime) {
    // Overrides Physics Object's calcPos
    // Y Axis
    if(mUp == true) {
      walkAcc.y = -maxAcc;
    } 
    else if(mDown == true) {
      walkAcc.y = maxAcc;
    } 
    else {
      // If there is no player imput, then:
      if(walkVelo.y > 0) {
        // If the acceleration would go too far:
        if(walkVelo.y + walkAcc.y * deltaTime < 0) {
          walkVelo.y = 0;
          walkAcc.y = 0;
        } 
        else {
        walkAcc.y = -maxAcc;
        }
      }
      else if(walkVelo.y < 0) {
        // If the acceleration would go too far:
        if(walkVelo.y + walkAcc.y * deltaTime > 0) {
          walkVelo.y = 0;
          walkAcc.y = 0;
        }
        else {
          walkAcc.y = maxAcc;
        }
      }
      else {
        walkAcc.y = 0;
      }
    }
    // X Axis
    if(mLeft == true) {
      walkAcc.x = -maxAcc;
    } 
    else if(mRight == true) {
      walkAcc.x = maxAcc;
    } 
    else {
      // If there is no player input, then:
      if(walkVelo.x > 0) {
        // If the acceleration would go too far:
        if(walkVelo.x + walkAcc.x * deltaTime < 0) {
          walkVelo.x = 0;
          walkAcc.x = 0;
        }
        else {
          walkAcc.x = -maxAcc;
        }
      }
      else if(walkVelo.x < 0) {
        // If the acceleration would go too far:
        if(walkVelo.x + walkAcc.x * deltaTime > 0) {
          walkVelo.x = 0;
          walkAcc.x = 0;
        } 
        else {
          walkAcc.x = maxAcc;
        }
      }
      else {
        walkAcc.x = 0;
      }
    }
    
    // Acceleration calculated, now on to velocity
    walkVelo = VMath.addVectors(walkVelo, VMath.multiplyByScalar(walkAcc, deltaTime));
    // If the velocity has reached maximum velocity
    if(walkVelo.x >= maxVelo) {
      walkVelo.x = maxVelo;
    }
    else if(walkVelo.x <= -maxVelo) {
      walkVelo.x = -maxVelo;
    }
    if(walkVelo.y >= maxVelo) {
      walkVelo.y = maxVelo;
    }
    else if(walkVelo.y <= -maxVelo) {
      walkVelo.y = -maxVelo;
    }
    // Increments the shootTimer
    shootTimer(deltaTime);

    // Adds the walking velocity to the physics position
    p = VMath.addVectors(VMath.multiplyByScalar(walkVelo, deltaTime), p);
    super.calcPos(deltaTime);
  }
  
}
