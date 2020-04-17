
public class ChasingEnemy extends PhysicsObject {
  
  Player target = new Player();
  
  Vector2 velo = new Vector2();
  static double maxSpeed = 100;
  double hp;
  
  public ChasingEnemy(Vector2 startPos, int s, double mass) { 
    super(startPos, s, mass);
    
    hp = 10;
    
  }
  
  public void chooseTarget(Player player) {
    
    target = player;
  }
  
  public Vector2 getCentre() {
    
    return new Vector2(p.x + size/2, p.y + size/2);
  }
  
  public void calcPos(double deltaTime) {
    
    velo.x = maxSpeed * deltaTime;
    velo.y = VMath.getAngleBetweenPoints(getCentre(), target.getCentre());
    
    p = VMath.addVectors(VMath.polarToCart(velo), p);
    
    super.calcPos();
    
  }
  
  public void collisionCheck(Vector2 playerPos, Vector2 bullet) {
    
    Vector2 bulletPos = new Vector2();
    
    bulletPos.x = playerPos.x + bullet.x;
    bulletPos.y = playerPos.y + bullet.y;
    
    if(VMath.lineCircle(playerPos, bulletPos, getCentre(), size/2)) {
      // subtracts health
      hp -= target.damage;
      // adds a force to the enemy
      addForce(new Vector2(target.power, VMath.getAngleBetweenPoints(playerPos, bulletPos)));
    }
  }
  
  
  
  
}
