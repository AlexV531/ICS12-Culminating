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

  boolean mousePressed = false;
  
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
    /*
    if(currentScreen == 1 && player.weapon != 0) {
      if(player.shootTimer == player.rateOfFire) {
        playerShooting = true;
      }
    }
    */
    if(currentScreen != -1) {
      mousePressed = true;
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

    Font title = new Font("Impact", Font.PLAIN, 200);
    Font subTitle = new Font("Impact", Font.PLAIN, 80);
    Font text = new Font("Helvetia", Font.PLAIN, 40);

    Vector2 mousePos = new Vector2();
    // For the mouse position when in menus
    Vector2 mousePosUI = new Vector2();
    // For recording the result of mouse clicks in the menus
    int function = -1;


    private long lastTime = System.currentTimeMillis();
    private long time;
    private double timestep;

    // Game setup (-1)
    public void gameSetup(Graphics2D g2D) {

      g2D.setColor(Color.RED);
      g2D.drawString("Loading...", 100, 100);

      // Key Bindings
      defineKeyBindings(deltaTime);

      // Gives enemies a reference to the player
      EnemyManager.chooseAllTargets(player);

      player.switchWeapon(2);

      // Start Screen buttons
      UIManager.addButton(new Vector2(100, 600), new Vector2(500, 700), 0, 0, "Play Game");
      UIManager.addButton(new Vector2(700, 600), new Vector2(1100, 700), 0, 1, "Quit Game");
      UIManager.addButton(new Vector2(450, 775), new Vector2(750, 825), 0, 2, "How To Play");
      // Pause Screen buttons
      UIManager.addButton(new Vector2(100, 600), new Vector2(500, 700), 2, 0, "Return to Game");
      UIManager.addButton(new Vector2(700, 600), new Vector2(1100, 700), 2, 1, "Quit Game");
      // Lose Screem buttons
      UIManager.addButton(new Vector2(100, 600), new Vector2(500, 700), 3, 0, "Play Again");
      UIManager.addButton(new Vector2(700, 600), new Vector2(1100, 700), 3, 1, "Quit Game");
      // Win Screen buttons
      UIManager.addButton(new Vector2(100, 600), new Vector2(500, 700), 4, 0, "Play Again");
      UIManager.addButton(new Vector2(700, 600), new Vector2(1100, 700), 4, 1, "Quit Game");
      // Instruction Screen button
      UIManager.addButton(new Vector2(400, 600), new Vector2(800, 700), 5, 0, "Return To Menu");
      
      EnemyManager.createEnemies();

      // Change this to zero once a menu screen is added
      currentScreen = 0;

      //EnemyManager.startSpawning();

    }

    // Start screen (0)
    public void startScreen(Graphics2D g2D) {

      // If this breaks after a game restart, reset the position of the player.
      function = UIManager.buttonCheck(currentScreen, mousePosUI, mousePressed);
      // Reset the mouse detector
      mousePressed = false;
      // If the button's function is zero, it will start the game.
      if(function == 0) {
        currentScreen = 1;
      }
      else if(function == 1) {
        System.exit(0);
      }
      else if(function == 2) {
        currentScreen = 5;
      }
      function = -1;

      UIManager.drawButtons(g2D, currentScreen);
      
      g2D.setFont(title);
      g2D.setColor(Color.WHITE);
      g2D.drawString("ARMAGEDDON", 50, 300);

    }
    // Game screen (1)
    public void gameScreen(Graphics2D g2D) {
      // If the player has past wave 3, they win
      if(EnemyManager.getWaveCount() > 3 && EnemyManager.getCurrentTimeWave() > 4) {
        currentScreen = 4;
      }
      
      // Updates the player/enemy positions
      if(!player.calcPos(deltaTime, mousePos)) {
        currentScreen = 3;
      }

      EnemyManager.calcPos(deltaTime);
      
      // Moves the camera to focus on player
      g2D.translate((int)-(player.getCentre().x - CANVAS_WIDTH/2), (int)-(player.getCentre().y - CANVAS_HEIGHT/2));
      
      // Testing floor
      Level.addFloor(g2D, -1000, -1000, 50, 50, i);

      // Draws the player/enemies
      player.drawPlayer(g2D, CANVAS_WIDTH, CANVAS_HEIGHT);

      EnemyManager.drawEnemies(g2D, CANVAS_WIDTH, CANVAS_HEIGHT);

      UIManager.headsUpDisplay(g2D, player);

      //g2D.setColor(Color.RED); // Hitbox of enemy
      //g2D.drawOval((int)enemy1.p.x, (int)enemy1.p.y, (int)enemy1.size, (int)enemy1.size);
      // Manages player shooting (should try to move to Keybinding once I have a Start method)
      if(mousePressed && player.weapon != 0) {
        // Player's shoot method
        if(player.shootTimer == player.rateOfFire) {
          player.shoot(g2D);
        }
        mousePressed = false;
      }
    }

    // Pause screen (2)
    public void pauseScreen(Graphics2D g2D) {

      function = UIManager.buttonCheck(currentScreen, mousePosUI, mousePressed);
      // Reset the mouse detector
      mousePressed = false;

      if(function == 0) {
        currentScreen = 1;
      }
      else if(function == 1) {
        System.exit(0);
      }
      function = -1;

      UIManager.drawButtons(g2D, currentScreen);

      g2D.setFont(title);
      g2D.setColor(Color.WHITE);
      g2D.drawString("PAUSED", 290, 300);

    }

    // Lose Screen (3)
    public void loseScreen(Graphics2D g2D) {

      function = UIManager.buttonCheck(currentScreen, mousePosUI, mousePressed);
      // Reset the mouse detector
      mousePressed = false;

      if(function == 0) {
        resetGame();
      }
      else if(function == 1) {
        System.exit(0);
      }
      function = -1;

      UIManager.drawButtons(g2D, currentScreen);

      g2D.setFont(title);
      g2D.setColor(Color.WHITE);
      g2D.drawString("GAME OVER", 160, 300);

    }
    // Win Screen (4)
    public void winScreen(Graphics2D g2D) {

      function = UIManager.buttonCheck(currentScreen, mousePosUI, mousePressed);
      // Reset the mouse detector
      mousePressed = false;

      if(function == 0) {
        resetGame();
      }
      else if(function == 1) {
        System.exit(0);
      }
      function = -1;

      UIManager.drawButtons(g2D, currentScreen);

      g2D.setFont(title);
      g2D.setColor(Color.WHITE);
      g2D.drawString("YOU WIN", 280, 300);

    }
    // Instructions Screen (5)
    public void instructionsScreen(Graphics2D g2D) {

      function = UIManager.buttonCheck(currentScreen, mousePosUI, mousePressed);
      // Reset the mouse detector
      mousePressed = false;

      if(function == 0) {
        currentScreen = 0;
      }
      function = -1;

      UIManager.drawButtons(g2D, currentScreen);

      g2D.setFont(subTitle);
      g2D.setColor(Color.WHITE);
      g2D.drawString("HOW TO PLAY", 400, 120);

      g2D.setFont(text);
      g2D.drawString("The objective of the game is to survive three waves of enemies.", 50, 230);
      g2D.drawString("Move with WASD and shoot with left click.", 240, 280);
      g2D.drawString("Switch weapon with the number keys", 280, 330);
      g2D.drawString("If you lose all of your health, you lose the game.", 200, 380);

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

      mousePosUI.x = mousePos.x + CANVAS_WIDTH/2 - player.getCentre().x;
      mousePosUI.y = mousePos.y + CANVAS_HEIGHT/2 - player.getCentre().y;
    } 

    public void resetGame() {
      EnemyManager.reset();
      player.reset();
      currentScreen = 0;
    }

    public void paintComponent(Graphics g) {
      // Erase the screen 
      super.paintComponent(g);

      Graphics2D g2D = (Graphics2D)g;

      // Draws the background
      setBackground(CANVAS_BACKGROUND); 
      
      // Gets the mouse position
      calcMousePos();
      
      // Calculate the timestep
      deltaTime = calcTimestep();
      // Sets up keybindings etc.
      if(currentScreen == -1) {
        gameSetup(g2D);
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
      else if(currentScreen == 4) {
        winScreen(g2D);
      }
      else if(currentScreen == 5) {
        instructionsScreen(g2D);
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
      EnemyManager.startSpawning();
    });
  }
  
  // Main Method
  public static void main(String[] args)
  {    
    MainGame prog = new MainGame();
    prog.timer.start();
  }
  
  
}
