import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;


public class Player extends PhysicsObject {
  // Physics stuff
  Vector2 walkVelo = new Vector2();
  Vector2 walkAcc = new Vector2();
  double maxVelo;
  double maxAcc;
  boolean mUp, mLeft, mDown, mRight, shooting;
  // Weapon stats
  int weapon = 1; // 0 == Hands, 1 == Shotgun
  double damage = 10;
  double range = 500;
  double rateOfFire = 0.5;
  double power = 10000;
  // Personal stats
  double hp = 100;
  // Shooting variables
  double shootTimer = 0;
  Vector2 bullet = new Vector2();
  double centreMouseRad = 0;
  // Player sprites
  Image playerP1Image, playerP2Image, playerP3Image, playerP4Image, playerP5Image, playerP6Image, playerP7Image, playerP8Image, currentImage;
  // Player limb sprites
  Image playerLLImage, playerRLImage, playerLHImage, playerRHImage, shotgunLeft, shotgunRight;

  // Animation Helpers
  double legCount = 0;
  double legModY, legModX, handModX = 0;
  boolean leftHandBehind, rightHandBehind= false;
  boolean weaponBehind = false;
  boolean handsShown = false;
  
  public Player() { 
    super(new Vector2(0, 0), 0, 0);
    maxVelo = 300;
    maxAcc = 900;
  }
  
  public Player(Vector2 startPos, int s, double mass) { 
    super(startPos, s, mass);
    maxVelo = 300;
    maxAcc = 900;
    ImageIcon pP1 = new ImageIcon("images/Soldier01-P1-NoLimbs.png");
    playerP1Image = pP1.getImage();
    ImageIcon pP2 = new ImageIcon("images/Soldier01-P2-NoLimbs.png");
    playerP2Image = pP2.getImage();
    ImageIcon pP3 = new ImageIcon("images/Soldier01-P3-NoLimbs.png");
    playerP3Image = pP3.getImage();
    ImageIcon pP4 = new ImageIcon("images/Soldier01-P4-NoLimbs.png");
    playerP4Image = pP4.getImage();
    ImageIcon pP5 = new ImageIcon("images/Soldier01-P5-NoLimbs.png");
    playerP5Image = pP5.getImage();
    ImageIcon pP6 = new ImageIcon("images/Soldier01-P6-NoLimbs.png");
    playerP6Image = pP6.getImage();
    ImageIcon pP7 = new ImageIcon("images/Soldier01-P7-NoLimbs.png");
    playerP7Image = pP7.getImage();
    ImageIcon pP8 = new ImageIcon("images/Soldier01-P8-NoLimbs.png");
    playerP8Image = pP8.getImage();
    currentImage = playerP1Image;

    ImageIcon pLL = new ImageIcon("images/Soldier01-LeftLeg.png");
    playerLLImage = pLL.getImage();
    ImageIcon pRL = new ImageIcon("images/Soldier01-RightLeg.png");
    playerRLImage = pRL.getImage();
    ImageIcon pLH = new ImageIcon("images/Soldier01-LeftHand.png");
    playerLHImage = pLH.getImage();
    ImageIcon pRH = new ImageIcon("images/Soldier01-RightHand.png");
    playerRHImage = pRH.getImage();

    ImageIcon sgl = new ImageIcon("images/ShotgunLeft.png");
    shotgunLeft = sgl.getImage();
    ImageIcon sgr = new ImageIcon("images/ShotgunRight.png");
    shotgunRight = sgr.getImage();
  }
  
  public Vector2 getCentre() {
    
    return new Vector2(p.x + size/2, p.y + size/2);
  }
  
  public void drawPlayer(Graphics2D g2D, int CANVAS_WIDTH, int CANVAS_HEIGHT) {
    // Vertical leg modifier calculations (sine wave!)
    if(mUp == true || mDown == true || mRight == true || mLeft == true) {
      legCount++;
      if(legCount > 10000) {
        legCount = 0;
      }
      legModY = Math.sin(legCount * 1/1.2) * 6;
    } 
    else {
      legModY = 0;
    }
    // Sets up animation helper variables according to which way the player is looking
    // Down (1)
    if(centreMouseRad >= 3 * Math.PI / 8 && centreMouseRad <= 5 * Math.PI / 8) {
      currentImage = playerP1Image;
      legModX = 0;
      handModX = 0;
      leftHandBehind = false;
      rightHandBehind = false;
      weaponBehind = false;
    }
    // Down Right (2)
    else if(centreMouseRad > Math.PI / 8 && centreMouseRad < 3 * Math.PI / 8) {
      currentImage = playerP2Image;
      legModX = 5;
      handModX = 4;
      leftHandBehind = false;
      rightHandBehind = true;
      weaponBehind = false;
    }
    // Right (3)
    else if(centreMouseRad >= -1 * Math.PI / 8 && centreMouseRad <= Math.PI / 8) {
      currentImage = playerP3Image;
      legModX = 10;
      handModX = 24;
      leftHandBehind = false;
      rightHandBehind = true;
      weaponBehind = false;
    }
    // Up Right (4)
    else if(centreMouseRad > -3 * Math.PI / 8 && centreMouseRad < -1 * Math.PI / 8) {
      currentImage = playerP4Image;
      legModX = 5;
      handModX = 4;
      leftHandBehind = true;
      rightHandBehind = false;
      weaponBehind = true;
    }
    // Up (5)
    else if(centreMouseRad >= -5 * Math.PI / 8 && centreMouseRad <= -3 * Math.PI / 8) {
      currentImage = playerP5Image;
      legModX = 0;
      handModX = 0;
      leftHandBehind = true;
      rightHandBehind = true;
      weaponBehind = true;
    }
    // Up Left (6)
    else if(centreMouseRad > -7 * Math.PI / 8 && centreMouseRad < -5 * Math.PI / 8) {
      currentImage = playerP6Image;
      legModX = 5;
      handModX = 4;
      leftHandBehind = false;
      rightHandBehind = true;
      weaponBehind = true;
    }
    // Left (7)
    else if(centreMouseRad >= 7 * Math.PI / 8 || centreMouseRad <= -7 * Math.PI / 8) {
      currentImage = playerP7Image;
      legModX = 10;
      handModX = 24;
      leftHandBehind = false;
      rightHandBehind = true;
      weaponBehind = false;
    }
    // Down Left (8)
    else if(centreMouseRad > 5 * Math.PI / 8 && centreMouseRad < 7 * Math.PI / 8) {
      currentImage = playerP8Image;
      legModX = 5;
      handModX = 10;
      leftHandBehind = true;
      rightHandBehind = false;
      weaponBehind = false;
    }
    // Draws the player leg sprites
    g2D.drawImage(playerLLImage, (int)(p.x + legModX), (int)(p.y + legModY), null);
    g2D.drawImage(playerRLImage, (int)(p.x - legModX), (int)(p.y - legModY), null);
    // Draws the hands behind the player if nessesary
    animateHands(g2D, CANVAS_WIDTH, CANVAS_HEIGHT, true);
    // Draws the player sprite
    g2D.drawImage(currentImage, (int)p.x, (int)p.y, null);
    // Draws the player hand sprites in front of the player
    animateHands(g2D, CANVAS_WIDTH, CANVAS_HEIGHT, false);
  }

  public void animateHands(Graphics2D g2D, int CANVAS_WIDTH, int CANVAS_HEIGHT, boolean behind) {
    // Hands (no weapon equiped)
    if(weapon == 0) {
      if(behind && leftHandBehind) {
        g2D.drawImage(playerLHImage, (int)(p.x + handModX), (int)(p.y + legModY/4), null);
      }
      if(behind && rightHandBehind) {
        g2D.drawImage(playerRHImage, (int)(p.x - handModX), (int)(p.y + legModY/4), null);
      }
      if(!behind && !leftHandBehind) {
        g2D.drawImage(playerLHImage, (int)(p.x + handModX), (int)(p.y + legModY/4), null);
      }
      if(!behind && !rightHandBehind) {
        g2D.drawImage(playerRHImage, (int)(p.x - handModX), (int)(p.y + legModY/4), null);
      }

    } 
    // Shotgun 
    else if(weapon == 1) {
      // Draw behind player
      if(behind && weaponBehind) {
        // Draw the correct shotgun sprite depending on direction the player is looking
        if(centreMouseRad >= -Math.PI/2) {
          animateShotgun(g2D, CANVAS_WIDTH, CANVAS_HEIGHT, shotgunRight);
        }
        else if(centreMouseRad < -Math.PI/2) {
          animateShotgun(g2D, CANVAS_WIDTH, CANVAS_HEIGHT, shotgunLeft);
        }
      }
      // Draw in front of player
      else if(!behind && !weaponBehind) {
        // Draw the correct shotgun sprite depending on direction the player is looking
        if(centreMouseRad <= Math.PI/2 && centreMouseRad > -Math.PI/2) {
          animateShotgun(g2D, CANVAS_WIDTH, CANVAS_HEIGHT, shotgunRight);
        }
        else if(centreMouseRad < -Math.PI/2 || centreMouseRad > Math.PI/2) {
          animateShotgun(g2D, CANVAS_WIDTH, CANVAS_HEIGHT, shotgunLeft);
        }
      }
    }
  }

  public void animateShotgun(Graphics2D g2D, int CANVAS_WIDTH, int CANVAS_HEIGHT, Image shotgun) {
    //Make a backup so that we can reset our graphics object after using it.
    AffineTransform backup = g2D.getTransform();
    //rx is the x coordinate for rotation, ry is the y coordinate for rotation, and angle
    //is the angle to rotate the image. If you want to rotate around the center of an image,
    //use the image's center x and y coordinates for rx and ry.
    AffineTransform a = AffineTransform.getRotateInstance(centreMouseRad, CANVAS_WIDTH/2, CANVAS_HEIGHT/2 + 8);
    //Set our Graphics2D object to the transform
    g2D.setTransform(a);
    //Draw our image like normal
    g2D.drawImage(shotgun, CANVAS_WIDTH/2, CANVAS_HEIGHT/2 - 8, null);
    //Reset our graphics object so we can draw with it again.
    g2D.setTransform(backup);
  }

  public void shoot() {
    
    if(shootTimer == rateOfFire) {
      // Recoil force
      addForce(new Vector2(10000, centreMouseRad - Math.PI));
      // Bullet Vector (polar)
      bullet.x = range;
      bullet.y = centreMouseRad;
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
  
  public void calcPos(double deltaTime, Vector2 mousePos) {
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

    // Calculates the angle between the mouse and the player's centre
    centreMouseRad = VMath.getAngleBetweenPoints(getCentre(), mousePos);
    //System.out.println(centreMouseRad);

    // Adds the walking velocity to the physics position
    p = VMath.addVectors(VMath.multiplyByScalar(walkVelo, deltaTime), p);
    super.calcPos(deltaTime);
  }
  
}
