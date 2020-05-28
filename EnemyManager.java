import java.awt.*;

public class EnemyManager {
    
    Player target = new Player();

    int maxChasingEnemies = 4;
    ChasingEnemy[] enemyList = new ChasingEnemy[maxChasingEnemies];
    Vector2[] spawnPointList = new Vector2[4];
    public EnemyManager() {
        // Top Left
        spawnPointList[0] = new Vector2(-900, -900);
        // Bottom Left
        spawnPointList[1] = new Vector2(-900, 900);
        // Top Right
        spawnPointList[2] = new Vector2(900, -900);
        // Bottom Right
        spawnPointList[3] = new Vector2(900, 900);


        for(int i = 0; i < maxChasingEnemies; i++) {
            enemyList[i] = new ChasingEnemy(spawnPointList[i % spawnPointList.length], 64, 5);
        }
    }

    public void calcPos(double deltaTime) {
        if(target.shootingPing == true) {
            //System.out.println("player shot detected");
            target.shootingPing = false;
            for(int i = 0; i < maxChasingEnemies; i++) {
                enemyList[i].enemyHitCheck();
            }
        }

        for(int i = 0; i < maxChasingEnemies; i++) {
            enemyList[i].calcPos(deltaTime);
        }
    }

    public void drawEnemies(Graphics2D g2D) {
        for(int i = 0; i < maxChasingEnemies; i++) {
            enemyList[i].drawEnemy(g2D);
        }
    }

    public void chooseAllTargets(Player player) {
        target = player;
        for(int i = 0; i < maxChasingEnemies; i++) {
            enemyList[i].chooseTarget(player);
        }
    }

}