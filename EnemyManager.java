import java.awt.*;

public class EnemyManager {
    
    static Player target = new Player();

    static int waveIntensity = 0;
    static int maxChasingEnemies = 4;
    static Enemy[] enemyList = new Enemy[maxChasingEnemies];
    static Vector2[] spawnPointList = new Vector2[4];
    // Used to draw bullet trails
    static double maxDistance;

    public EnemyManager() {
        // Top Left
        spawnPointList[0] = new Vector2(-500, -500);
        // Bottom Left
        spawnPointList[1] = new Vector2(-500, 500);
        // Top Right
        spawnPointList[2] = new Vector2(500, -500);
        // Bottom Right
        spawnPointList[3] = new Vector2(500, 500);


        for(int i = 0; i < maxChasingEnemies; i++) {
            enemyList[i] = new Enemy(spawnPointList[i % spawnPointList.length], 64, 5);
        }
    }

    public void calcPos(double deltaTime) {
        if(target.shootingPing == true) {
            bubbleSort();
            //for(int i = 0; i < maxChasingEnemies; i++) {
            //    System.out.println(enemyList[i].toString());
            //}
            //System.out.println("player shot detected");
            
            // The way the pellets bullet trails are calculated means that they are treated as one bullet.
            // If the pellets are ever changed to be treated as multiple bullets that can hit multiple enemeies,
            // This will need to change as well

            double currentDistance = 0;
            // If the player is using a shotgun
            // Checks every enemy with every pellet
            if(target.pelletCount > 0) {
                for(int i = 0; i < maxChasingEnemies; i++) {
                    for(int j = 0; j < target.pelletCount; j++) {
                        currentDistance = enemyList[i].collisionCheck(target.getCentre(), target.pellets[j]);
                        if(currentDistance > maxDistance) {
                            maxDistance = currentDistance;
                        }
                    }
                }
            }
              
            // If the player is using any other gun
            // Checks every enemy with the bullet
            else {
                for(int i = 0; i < maxChasingEnemies; i++) {
                    currentDistance = enemyList[i].collisionCheck(target.getCentre(), target.bullet);
                    if(currentDistance > maxDistance) {
                        maxDistance = currentDistance;
                    }
                }
            }
            // If the bullet doesn't hit an enemy, it's distance traveled is equal to it's range
            if(maxDistance == 0) {
                maxDistance = target.range;
            }
            System.out.println(maxDistance);

            // shootingPing is not reset until the draw phase to include the bullet trails
            
            // Resets the number of enemies shot this frame for next frame
            Enemy.enemiesShot = 0;
        }

        for(int i = 0; i < maxChasingEnemies; i++) {
            enemyList[i].calcPos(deltaTime);
        }
    }

    public void drawEnemies(Graphics2D g2D) {
        for(int i = 0; i < maxChasingEnemies; i++) {
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
    }
    // Change to a static in the enemy class
    public void chooseAllTargets(Player player) {
        target = player;
        for(int i = 0; i < maxChasingEnemies; i++) {
            enemyList[i].chooseTarget(player);
        }
    }

    public void spawnAll() {
        for(int i = 0; i < enemyList.length; i++) {
            enemyList[i].spawn();
        }
    }
    // Sorts the enemies into a list based on how far they are from the player
    public static void bubbleSort() {
        boolean sorted = false;
        Enemy temp;
        while(!sorted) {
            sorted = true;
            for (int i = 0; i < enemyList.length - 1; i++) {
                if (VMath.getDistanceBetweenPoints(target.p, enemyList[i].p) > VMath.getDistanceBetweenPoints(target.p, enemyList[i+1].p)) {
                    temp = enemyList[i];
                    enemyList[i] = enemyList[i+1];
                    enemyList[i+1] = temp;
                    sorted = false;
                }
            }
        }
    }

}