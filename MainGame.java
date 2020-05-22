import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

@SuppressWarnings("serial")
class MainGame extends JFrame implements ActionListener, MouseListener { 
  
  // Name-constants for the various dimensions
  public static final int CANVAS_WIDTH = 1200;
  public static final int CANVAS_HEIGHT = 900;
  public static final Color CANVAS_BACKGROUND = Color.BLACK;
  
  private DrawCanvas canvas; // the custom drawing canvas (extends JPanel)
  
  // the buttons
  JButton btnQ1, btnQ2, btnQ3, btnExit;
  
  Player player = new Player(new Vector2(0, 0), 64, 10);
  
  ChasingEnemy enemy1 = new ChasingEnemy(new Vector2(100, 500), 40, 5);
  
  ChasingEnemy enemy2 = new ChasingEnemy(new Vector2(100, 400), 40, 5);
  
  boolean playerShooting = false;
  
  int currentScreen = -1; // -1 == Game Setup, 0 == Start Menu, 1 == Game, 2 == Pause

  // timer to update the screen
  Timer timer;
  public MainGame() {

    // update the screen every amount of milliseconds (specified below
    timer =  new Timer(40, this);
    
    // Set up a custom drawing JPanel
    canvas = new DrawCanvas();
    canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
    canvas.addMouseListener(this);
    
    // Add both panels to this JFrame
    Container cp = getContentPane();
    cp.setLayout(new BorderLayout());
    cp.add(canvas, BorderLayout.CENTER);
    
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
    setTitle("Main Game");
    pack();           // pack all the components in the JFrame
    setVisible(true); // show it
    requestFocus();
    
  }
  
  public void actionPerformed(ActionEvent e)
  {
    if (e.getSource()== timer){
      //System.out.println("Timer ticked");
      canvas.repaint();
      // Calls the paintComponent in the DrawCanvas class
    }
  }
  
  public void mousePressed(MouseEvent e) {
    // During game screen
    if(currentScreen == 1) {
      if(player.shootTimer == player.rateOfFire) {
        playerShooting = true;
      }
    }
  }

  public void mouseReleased(MouseEvent e) {

  }

  public void mouseClicked(MouseEvent e) {
    // During Pause Screen
    if(currentScreen == 2) {
      System.out.println("Mouse clicked");
    }
  }

  public void mouseEntered(MouseEvent e) {

  }

  public void mouseExited(MouseEvent e) {

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
  
  
  //DrawCanvas (inner class) is a JPanel used for custom drawing
  class DrawCanvas extends JPanel {

    // Add images
    ImageIcon ic = new ImageIcon("images/MetalPanel.png");
    Image i = ic.getImage();

    ImageIcon ps = new ImageIcon("images/PauseScreen.png");
    Image pauseScreen = ps.getImage();
    
    double deltaTime = 0;

    Vector2 mousePos = new Vector2();

    private long lastTime = System.currentTimeMillis();
    private long time;
    private double timestep;

    // Game setup (-1)
    public void gameSetup() {
      System.out.println("GameSetup");
      // Key Bindings
      defineKeyBindings();

      // Gives enemies a reference to the player
      enemy1.chooseTarget(player);

      // Change this to two once a menu screen is added
      currentScreen = 1;
    }

    // Start screen (0)
    public void startScreen(Graphics2D g2D) {
      
      
    }
    // Game screen (1)
    public void gameScreen(Graphics2D g2D) {
      // Updates the player/enemy positions
      player.calcPos(deltaTime, mousePos);
      enemy1.calcPos(deltaTime);
      
      // Moves the camera to focus on player
      g2D.translate((int)-(player.getCentre().x - CANVAS_WIDTH/2), (int)-(player.getCentre().y - CANVAS_HEIGHT/2));
      
      // Draws the background
      setBackground(CANVAS_BACKGROUND); 
      
      // Testing floor
      Level.addFloor(g2D, -1000, -1000, 50, 50, i);

      // Draws the player/enemies
      player.drawPlayer(g2D, CANVAS_WIDTH, CANVAS_HEIGHT);

      g2D.setColor(Color.RED);
      g2D.fillOval((int)enemy1.p.x, (int)enemy1.p.y, enemy1.size, enemy1.size);

      // Manages collisions between the player and the enemies (should move elsewhere, enemy class?)
      if(VMath.getDistanceBetweenPoints(player.getCentre(), enemy1.getCentre()) < player.size/2 + enemy1.size/2) {
        
        player.addForce(new Vector2(6000, VMath.getAngleBetweenPoints(enemy1.getCentre(), player.getCentre())));
        enemy1.addForce(new Vector2(2000, VMath.getAngleBetweenPoints(player.getCentre(), enemy1.getCentre())));
        
      }
      // Manages player shooting (should try to move to Keybinding once I have a Start method)
      if(playerShooting) {
        // Player's shoot method
        player.shoot();
        // Used when drawing the bullet trails
        g2D.setColor(Color.WHITE);

        // Enemy/Bullet collisions
        
        // If the player is using a shotgun
        if(player.weapon == 1) {
          for(int i = 0; i < player.pelletCount; i++) {
            enemy1.collisionCheck(player.getCentre(), player.pellets[i]);
            g2D.drawLine((int)player.getCentre().x, (int)player.getCentre().y, (int)(player.pellets[i].x + player.getCentre().x), (int)(player.pellets[i].y + player.getCentre().y));
          }
        }
        
        // If the player is using any other gun
        else {
          enemy1.collisionCheck(player.getCentre(), player.bullet);
          enemy2.collisionCheck(player.getCentre(), player.bullet);
          // Draws the bullet trail
          g2D.drawLine((int)player.getCentre().x, (int)player.getCentre().y, (int)(player.bullet.x + player.getCentre().x), (int)(player.bullet.y + player.getCentre().y));
        }
        
        playerShooting = false;
      }
    }

    // Pause screen (2)
    public void pauseScreen(Graphics2D g2D) {

      g2D.setColor(new Color(255, 0, 0));

      g2D.drawImage(pauseScreen, 0, 0, null);

    }

    public double calcTimestep() {
      time = System.currentTimeMillis();
      timestep = 0.001 * (time - lastTime);
      lastTime = time;
      return timestep;
    }

    public void calcMousePos() {
      Point mousePoint = canvas.getMousePosition();
      if(mousePoint != null) {
        mousePos.x = (mousePoint.getX() - CANVAS_WIDTH/2) + player.getCentre().x;
        mousePos.y = (mousePoint.getY() - CANVAS_HEIGHT/2) + player.getCentre().y;
      }
    } 

    public void paintComponent(Graphics g) {
      // Erase the screen 
      super.paintComponent(g);

      Graphics2D g2D = (Graphics2D)g;

      // Gets the mouse position
      calcMousePos();
      
      // Calculate the timestep
      deltaTime = calcTimestep();
      // Sets up keybindings etc.
      if(currentScreen == -1) {
        gameSetup();
      }
      // Shows the right screen
      else if(currentScreen == 0) {
        startScreen(g2D);
      }
      else if(currentScreen == 1) {
        gameScreen(g2D);
      }
      else if(currentScreen == 2) {
        pauseScreen(g2D);
      }
    }    
  }

  public void defineKeyBindings() {
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
    // Pause game
    addKeyBinding(canvas, KeyEvent.VK_P, false, "Pause", (evt) -> {
      if(currentScreen == 1) {
        currentScreen = 2;
      }
      else {
        currentScreen = 1;
      }
    });
  }
  
  // Main Method
  public static void main(String[] args)
  {    
    MainGame prog = new MainGame();
    prog.timer.start();
  }
  
  
}
