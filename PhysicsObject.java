import java.util.*;

public class PhysicsObject {
  // Position, Velocity, Acceleration
  Vector2 p = new Vector2();
  Vector2 v = new Vector2();
  Vector2 a = new Vector2();
  // Force Variables
  Vector2 fNet = new Vector2();
  Vector2 fFriction = new Vector2();
  ArrayList<Vector2> forceList = new ArrayList<Vector2>();
  // Wall Collisions
  boolean collisionX = false;
  boolean collisionY = false;
  // Friction Variables
  double mu = 0.15;
  static final double G = 9.8;
  // Personal stats
  int size;
  double m;
  // Boundarys
  int topBound = -1000;
  int bottomBound = 1000;
  int leftBound = -1000;
  int rightBound = 1000;
  
  public PhysicsObject(Vector2 startingPos, int size, double mass) { 
    this.size = size;
    p = startingPos;
    m = mass;
  }
  
  public void calcPos(double deltaTime) {
    // Calculates net force
    addForces();
    addFriction();
    // Calculates acceleration
    a.x = fNet.x; // / m;
    a.y = fNet.y; // / m;
    // Calculates velocity and position
    v = VMath.addVectors(VMath.multiplyByScalar(a, deltaTime), v);
    p = VMath.addVectors(VMath.multiplyByScalar(v, deltaTime), p);
    
    boundaryCheck();

    // Clears the net force
    fNet.x = 0;
    fNet.y = 0;
  }
  
  // Adds a force to the force list
  public void addForce(Vector2 force) {
    // Adds the force to the list of active forces
    forceList.add(force);
  }
  
  // Adds the list of active forces to a net force
  private void addForces() {
    for(int i = 0;  i < forceList.size(); i++) {
      
      // Adds the force vector to the net force
      fNet = VMath.addVectors(fNet, VMath.polarToCart(forceList.get(i)));
    }
    forceList.clear();
  }
  
  // Adds friction to the net force
  private void addFriction() {
    // Calculates friction magnitude and direction
    fFriction.x = m * G * mu * 50;
    fFriction.y = VMath.cartToPolar(v).y - Math.PI;

    // Accounts for the friction changing direction of motion
    if(fFriction.x/m > VMath.cartToPolar(v).x) {
      v.x = 0;
      v.y = 0;
    }

    // Adds friction to the net force
    else if(VMath.cartToPolar(v).x > 0) {
      fNet = VMath.addVectors(fNet, VMath.polarToCart(fFriction));
    }
  }

  private void boundaryCheck() {
    if(p.y < topBound) {
      a.y = 0;
      v.y = 0;
      p.y = topBound;
    }
    else if(p.y > bottomBound) {
      a.y = 0;
      v.y = 0;
      p.y = bottomBound;
    }
    if(p.x < leftBound) {
      a.x = 0;
      v.x = 0;
      p.x = leftBound;
    }
    else if(p.x > rightBound) {
      a.x = 0;
      v.x = 0;
      p.x = rightBound;
    }
  }
}












