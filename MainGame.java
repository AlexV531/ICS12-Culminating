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

  // Rest in peace enemy1, you will be missed
  //ChasingEnemy enemy1 = new ChasingEnemy(new Vector2(100, 500), 64, 5); 
  
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
    if(currentScreen == 1 && player.weapon != 0) {
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
    // For the mouse position when in menus
    Vector2 mousePosUI = new Vector2();


    private long lastTime = System.currentTimeMillis();
    private long time;
    private double timestep;

    // Game setup (-1)
    public void gameSetup() {
      System.out.println("GameSetup");
      // Key Bindings
      defineKeyBindings(deltaTime);

      // Gives enemies a reference to the player
      EnemyManager.chooseAllTargets(player);

      player.switchWeapon(2);
      

      UIManager.addButton(new Vector2(100, 100), new Vector2(500, 200), 0, 0, "Test Button");
      
      EnemyManager.createEnemies();

      // Change this to zero once a menu screen is added
      currentScreen = 1;

    }

    // Start screen (0)
    public void startScreen(Graphics2D g2D) {
      
      mousePosUI.x = mousePos.x + CANVAS_WIDTH/2 - player.getCentre().x;
      mousePosUI.y = mousePos.y + CANVAS_HEIGHT/2 - player.getCentre().y;

      // If this breaks after a game restart, reset the position of the player.
      UIManager.buttonCheck(currentScreen, mousePosUI, false);

      UIManager.drawButtons(g2D, currentScreen);

    }
    // Game screen (1)
    public void gameScreen(Graphics2D g2D) {
      // Updates the player/enemy positions
      if(!player.calcPos(deltaTime, mousePos)) {
        currentScreen = 3;
      }

      EnemyManager.calcPos(deltaTime);
      
      // Moves the camera to focus on player
      g2D.translate((int)-(player.getCentre().x - CANVAS_WIDTH/2), (int)-(player.getCentre().y - CANVAS_HEIGHT/2));
      
      // Draws the background
      setBackground(CANVAS_BACKGROUND); 
      
      // Testing floor
      Level.addFloor(g2D, -1000, -1000, 50, 50, i);

      // Draws the player/enemies
      player.drawPlayer(g2D, CANVAS_WIDTH, CANVAS_HEIGHT);

      EnemyManager.drawEnemies(g2D);

      //g2D.setColor(Color.RED); // Hitbox of enemy
      //g2D.drawOval((int)enemy1.p.x, (int)enemy1.p.y, (int)enemy1.size, (int)enemy1.size);
      // Manages player shooting (should try to move to Keybinding once I have a Start method)
      if(playerShooting) {
        // Player's shoot method
        player.shoot(g2D);
        playerShooting = false;
      }
    }

    // Pause screen (2)
    public void pauseScreen(Graphics2D g2D) {

      g2D.setColor(new Color(255, 0, 0));

      g2D.drawImage(pauseScreen, 0, 0, null);
      /*
      Font h = new Font("Helvetica", Font.PLAIN, 24);

      g2D.setFont(h);
      g2D.drawString("Font Test", 100, 100);
      */

    }

    public void loseScreen(Graphics2D g2D) {

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
      else if(currentScreen == 3) {
        loseScreen(g2D);
      }

      

    }    
  }

  public void defineKeyBindings(double deltaTime) {
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
    // Switch Weapons
    // Switch to pistol
    addKeyBinding(canvas, KeyEvent.VK_1, false, "Switch1", (evt) -> {
      if(player.weapon != 1 && player.pistolAcquired) {
        player.switchWeapon(1); 
      }
      else {
        player.switchWeapon(0); 
      }
    });
    addKeyBinding(canvas, KeyEvent.VK_2, false, "Switch2", (evt) -> {
      if(player.weapon != 2 && player.shotgunAcquired) {
        player.switchWeapon(2); 
      }
      else {
        player.switchWeapon(0); 
      }
    });

    addKeyBinding(canvas, KeyEvent.VK_G, false, "SpawnEnemies", (evt) -> {
      EnemyManager.respawnInProgress = true;
    });
  }
  
  // Main Method
  public static void main(String[] args)
  {    
    MainGame prog = new MainGame();
    prog.timer.start();
  }
  
  
}
