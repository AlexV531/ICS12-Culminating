import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

@SuppressWarnings("serial")
class MainGame extends JFrame implements ActionListener { 
  
  // Name-constants for the various dimensions
  public static final int CANVAS_WIDTH = 1200;
  public static final int CANVAS_HEIGHT = 900;
  public static final Color CANVAS_BACKGROUND = Color.CYAN;
  
  private DrawCanvas canvas; // the custom drawing canvas (extends JPanel)
  
  // the buttons
  JButton btnQ1, btnQ2, btnQ3, btnExit;
  
  Player player = new Player(new Vector2(0, 0), 64, 10);
  
  ChasingEnemy enemy1 = new ChasingEnemy(new Vector2(100, 500), 40, 5);
  
  ChasingEnemy enemy2 = new ChasingEnemy(new Vector2(100, 400), 40, 5);
  
  boolean playerShooting = false;
  
  // timer to update the screen
  Timer timer;
  public MainGame() {
    
    // update the screen every amount of milliseconds (specified below
    timer =  new Timer(40, this);
    
    // Set up a custom drawing JPanel
    canvas = new DrawCanvas();
    canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
    
    // Key Bindings (True: on key release False: on key press)
    // Move up
    addKeyBinding(canvas, KeyEvent.VK_W, false, "MoveUp", (evt) -> {
      player.mUp = true;
    });
    addKeyBinding(canvas, KeyEvent.VK_W, true, "MoveUpRelease", (evt) -> {
      player.mUp = false;
    });
    // Move down
    addKeyBinding(canvas, KeyEvent.VK_S, false, "MoveDown", (evt) -> {
      player.mDown = true;
    });
    addKeyBinding(canvas, KeyEvent.VK_S, true, "MoveDownRelease", (evt) -> {
      player.mDown = false;
    });
    // Move right
    addKeyBinding(canvas, KeyEvent.VK_D, false, "MoveRight", (evt) -> {
      player.mRight = true;
    });
    addKeyBinding(canvas, KeyEvent.VK_D, true, "MoveRightRelease", (evt) -> {
      player.mRight = false;
    });
    //Move left
    addKeyBinding(canvas, KeyEvent.VK_A, false, "MoveLeft", (evt) -> {
      player.mLeft = true;
    });
    addKeyBinding(canvas, KeyEvent.VK_A, true, "MoveLeftRelease", (evt) -> {
      player.mLeft = false;
    });
    // Adding force opposite to the direction the player is facing
    addKeyBinding(canvas, KeyEvent.VK_SPACE, true, "AddForce", (evt) -> {
      if(player.shootTimer == player.rateOfFire) {
        playerShooting = true;
      }
      
    });
    
    
    // Add both panels to this JFrame
    Container cp = getContentPane();
    cp.setLayout(new BorderLayout());
    cp.add(canvas, BorderLayout.CENTER);
    
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
    setTitle("Main Game");
    pack();           // pack all the components in the JFrame
    setVisible(true); // show it
    requestFocus();
    
    // Gives enemies a reference to the player
    enemy1.chooseTarget(player);
    enemy2.chooseTarget(player);
    
  }
  
  public void actionPerformed(ActionEvent e)
  {
    if (e.getSource()== timer){
      //System.out.println("Timer ticked");
      canvas.repaint();
      // Calls the paintComponent in the DrawCanvas class
    }
  }
  
  // Boiler plate for adding Key Bindings
  public void addKeyBinding(JComponent comp, int keyCode, boolean released, String id, ActionListener actionListener) {
    InputMap im = comp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
    ActionMap ap = comp.getActionMap();
    im.put(KeyStroke.getKeyStroke(keyCode, 0, released), id);
    ap.put(id, new AbstractAction() {
      public void actionPerformed(ActionEvent e){
        actionListener.actionPerformed(e);
      }
    });
  }
  
  /* 
   * DrawCanvas (inner class) is a JPanel used for custom drawing
   */
  class DrawCanvas extends JPanel {

    // Add images
    ImageIcon ic = new ImageIcon("MetalPanel.png");
    Image i = ic.getImage();

    // // Add the player images
    // ImageIcon pP1 = new ImageIcon("Soldier01-P1-NoLimbs.png");
    // Image playerP1Image = pP1.getImage();
    // ImageIcon pLL = new ImageIcon("Soldier01-LeftLeg.png");
    // Image playerLLImage = pLL.getImage();
    // ImageIcon pRL = new ImageIcon("Soldier01-RightLeg.png");
    // Image playerRLImage = pRL.getImage();
    // ImageIcon pLH = new ImageIcon("Soldier01-LeftHand.png");
    // Image playerLAImage = pLH.getImage();
    // ImageIcon pRH = new ImageIcon("Soldier01-RightHand.png");
    // Image playerRAImage = pRH.getImage();
    
    double deltaTime = 0;

    Vector2 mousePos = new Vector2();

    public void paintComponent(Graphics g) {
      // Erase the screen 
      super.paintComponent(g);
      
      // Gets the mouse position
      Point mousePoint = canvas.getMousePosition();
      if(mousePoint != null) {
        mousePos.x = (mousePoint.getX() - CANVAS_WIDTH/2) + player.p.x;
        mousePos.y = (mousePoint.getY() - CANVAS_HEIGHT/2) + player.p.y;
      }
      
      // Calculate the timestep
      deltaTime = Time.calcTime();
      
      // Updates the player/enemy positions
      player.calcPos(deltaTime);
      enemy1.calcPos(deltaTime);
      //enemy2.calcPos(deltaTime);
      
      // Moves the camera to focus on player
      g.translate((int)-(player.p.x - CANVAS_WIDTH/2), (int)-(player.p.y - CANVAS_HEIGHT/2));
      
      // Draws the background
      setBackground(CANVAS_BACKGROUND); 
      
      // Testing floor
      Level.addFloor(g, -1000, -1000, 20, 20, i);
      
      // // Draws the player leg sprites
      // g.drawImage(playerLLImage, (int)player.p.x, (int)player.p.y, null);
      // g.drawImage(playerRLImage, (int)player.p.x, (int)player.p.y, null);
      // // Draws the player sprite
      // g.drawImage(playerP1Image, (int)player.p.x, (int)player.p.y, null);
      // // Draws the player hand sprites
      // g.drawImage(playerLAImage, (int)player.p.x, (int)player.p.y, null);
      // g.drawImage(playerRAImage, (int)player.p.x, (int)player.p.y, null);


      // Draws the player/enemies
      player.drawPlayer(g);

      g.setColor(Color.RED);
      g.fillOval((int)enemy1.p.x, (int)enemy1.p.y, enemy1.size, enemy1.size);
      //g.fillOval((int)enemy2.p.x, (int)enemy2.p.y, enemy2.size, enemy2.size);
      
      // Manages collisions between the player and the enemies (should move elsewhere, enemy class?)
      if(VMath.getDistanceBetweenPoints(player.getCentre(), enemy1.getCentre()) < player.size/2 + enemy1.size/2) {
        
        player.addForce(new Vector2(6000, VMath.getAngleBetweenPoints(enemy1.getCentre(), player.getCentre())));
        enemy1.addForce(new Vector2(2000, VMath.getAngleBetweenPoints(player.getCentre(), enemy1.getCentre())));
        
      }
      // Manages player shooting (should try to move to Keybinding once I have a Start method)
      if(playerShooting) {
        // Player's shoot method
        player.shoot(mousePos);
        // Enemy/Bullet collisions
        enemy1.collisionCheck(player.getCentre(), player.bullet);
        enemy2.collisionCheck(player.getCentre(), player.bullet);
        // Draws the bullet trail
        g.setColor(Color.WHITE);
        g.drawLine((int)player.getCentre().x, (int)player.getCentre().y, (int)(player.bullet.x + player.getCentre().x), (int)(player.bullet.y + player.getCentre().y));
        playerShooting = false;
      }
      
    }    
  }
  
  // Main Method
  public static void main(String[] args)
  {    
    MainGame prog = new MainGame();
    prog.timer.start();
  }
  
  
}
