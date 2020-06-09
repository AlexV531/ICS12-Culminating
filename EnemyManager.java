import java.awt.*;

public class EnemyManager {
    
    static Player target = new Player();

    static int totalEnemiesDead = 0;
    static int waveIntensity = 0;
    static int maxEnemies = 12;
    static Enemy[] enemyList = new Enemy[maxEnemies];
    static Enemy[] enemyListSorted = new Enemy[maxEnemies]; // Used to keep the enemyList unscrambled
    static Vector2[] spawnPointList = new Vector2[4];
    // Used to draw bullet trails
    static double maxDistance;
    // Wave managment varibles
    static double currentTime = 0; // Used to have a delay between waves
    static boolean respawnInProgress = false;
    static int roundCount = 0; // Used to keep track of the rounds
    static int waveCount = 1; // Used to keep track of the waves
    static int targetTime = 3;

    public EnemyManager() {

    }

    public static void createEnemies() {
        // Top Left
        spawnPointList[0] = new Vector2(-500, -500);
        // Bottom Left
        spawnPointList[1] = new Vector2(-500, 500);
        // Top Right
        spawnPointList[2] = new Vector2(500, -500);
        // Bottom Right
        spawnPointList[3] = new Vector2(500, 500);
        // Creates the enemies according to the parameters
        for(int i = 0; i < maxEnemies; i++) {
            enemyList[i] = new Enemy(spawnPointList[i % spawnPointList.length], 64, 5);
            enemyListSorted[i] = enemyList[i];
        }
    }

    public static void calcPos(double deltaTime) {
        // Respawn in progress
        if(respawnInProgress) {
            // Spawns all enemies
            spawnAll(deltaTime);
        }
        if(target.shootingPing == true) {
            bubbleSort();
            // The way the pellets bullet trails are calculated means that they are treated as one bullet.
            // If the pellets are ever changed to be treated as multiple bullets that can hit multiple enemeies,
            // This will need to change as well
            double currentDistance = 0;
            maxDistance = 0;
            // If the player is using a shotgun
            // Checks every enemy with every pellet
            if(target.pelletCount > 0) {
                for(int i = 0; i < maxEnemies; i++) {
                    for(int j = 0; j < target.pelletCount; j++) {
                        currentDistance = enemyListSorted[i].collisionCheck(target.getCentre(), target.pellets[j]);
                        if(currentDistance > maxDistance) {
                            maxDistance = currentDistance;
                        }
                    }
                }
            }
              
            // If the player is using any other gun
            // Checks every enemy with the bullet
            else {
                for(int i = 0; i < maxEnemies; i++) {
                    currentDistance = enemyListSorted[i].collisionCheck(target.getCentre(), target.bullet);
                    if(currentDistance > maxDistance) {
                        maxDistance = currentDistance;
                    }
                }
            }
            // If the bullet doesn't hit an enemy, it's distance traveled is equal to it's range
            if(maxDistance == 0 || maxDistance > target.range) {
                maxDistance = target.range;
            }
            //System.out.println(maxDistance);

            // shootingPing is not reset until the draw phase to include the bullet trails
            
            // Resets the number of enemies shot this frame for next frame
            Enemy.enemiesShot = 0;
        }

        for(int i = 0; i < maxEnemies; i++) {
            enemyList[i].calcPos(deltaTime);
        }
    }

    public static void drawEnemies(Graphics2D g2D, int CANVAS_WIDTH, int CANVAS_HEIGHT) {
        for(int i = 0; i < maxEnemies; i++) {
            enemyList[i].drawEnemy(g2D);
        }

        if(target.shootingPing) {
            // Bullet Trails
            g2D.setColor(Color.WHITE);
            // Shotgun
            if(target.pelletCount > 0) {
                for(int i = 0; i < target.pelletCount; i++) {
                    target.pellets[i] = VMath.cartToPolar(target.pellets[i]);
                    target.pellets[i].x = maxDistance;
                    target.pellets[i] = VMath.polarToCart(target.pellets[i]);
                    g2D.drawLine((int)target.getCentre().x, (int)target.getCentre().y, (int)(target.pellets[i].x + target.getCentre().x), (int)(target.pellets[i].y + target.getCentre().y));
                }
            }
            // Other guns
            else {
                target.bullet = VMath.cartToPolar(target.bullet);
                target.bullet.x = maxDistance;
                target.bullet = VMath.polarToCart(target.bullet);
                g2D.drawLine((int)target.getCentre().x, (int)target.getCentre().y, (int)(target.bullet.x + target.getCentre().x), (int)(target.bullet.y + target.getCentre().y));
            }
            // Reset shootingPing
            target.shootingPing = false;

            
        }

        // Wave title
        if(currentTime > 5 && currentTime < 10) {
            g2D.setColor(Color.WHITE);
            g2D.drawString("Wave " + waveCount, (int)target.p.x, (int)target.p.y);
        }
    }
    // Change to a static in the enemy class
    public static void chooseAllTargets(Player player) {
        target = player;
        Enemy.target = player;
    }
    public static void spawnAll(double deltaTime) {
        currentTime += deltaTime;
        //System.out.println(currentTime);
        
        if(currentTime >= targetTime) {
            targetTime = 3;
            for(int i = 0; i < 4; i++) {
                enemyList[i + (roundCount * 4)].spawn();
            }
            if(roundCount >= maxEnemies/4 - 1) {
                respawnInProgress = false;
                roundCount = 0;
                targetTime = 10;
                waveCount++;
            } 
            else {
                roundCount++;
            }
            currentTime = 0;
        }
    }
    // Called in the enemy class to keep track of how many enemies are dead
    public static void enemyDead() {
        totalEnemiesDead++;
        if(totalEnemiesDead >= (maxEnemies/4)*4) {
            respawnInProgress = true;
            totalEnemiesDead = 0;
        }
    }

    // Sorts the enemies into a list based on how far they are from the player
    public static void bubbleSort() {
        boolean sorted = false;
        Enemy temp;
        while(!sorted) {
            sorted = true;
            for (int i = 0; i < enemyList.length - 1; i++) {
                if (VMath.getDistanceBetweenPoints(target.p, enemyListSorted[i].p) > VMath.getDistanceBetweenPoints(target.p, enemyListSorted[i+1].p)) {
                    temp = enemyListSorted[i];
                    enemyListSorted[i] = enemyListSorted[i+1];
                    enemyListSorted[i+1] = temp;
                    sorted = false;
                }
            }
        }
    }

    public static void reset() {
        currentTime = 0;
        respawnInProgress = false;
        roundCount = 0;
        waveCount = 1;
        targetTime = 3;
        totalEnemiesDead = 0;
        waveIntensity = 0;

        createEnemies();
    }

}