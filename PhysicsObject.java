import java.util.*;

public class PhysicsObject {
  // Position, Velocity, Acceleration
  Vector2 p = new Vector2();
  Vector2 v = new Vector2();
  Vector2 a = new Vector2();
  // Force Variables
  Vector2 fNet = new Vector2();
  Vector2 fFriction = new Vector2();
  ArrayList<Force> forceList = new ArrayList<Force>();
  // Wall Collisions
  boolean collisionX = false;
  boolean collisionY = false;
  // Friction Variables
  double mu = 0.15;
  static final double G = 9.8;
  // Personal stats
  int size;
  double m;
  
  public PhysicsObject(Vector2 startingPos, int size, double mass) { 
    this.size = size;
    p = startingPos;
    m = mass;
  }
  
  public void calcPos() {
    // Calculates net force
    addForces();
    addFriction();
    // Calculates acceleration
    a.x = fNet.x / m;
    a.y = fNet.y / m;
    // Calculates velocity and position
    v = VMath.addVectors(a, v);
    p = VMath.addVectors(v, p);
    
    // Clears the net force
    fNet.x = 0;
    fNet.y = 0;
  }
  
  // Adds a force to the force list
  public void addForce(Vector2 force, double t) {
    // Adds the force to the list of active forces
    Force forceToAdd = new Force(force, t);
    forceList.add(forceToAdd);
  }
  
  // Adds the list of active forces to a net force
  private void addForces() {
    for(int i = 0;  i < forceList.size(); i++) {
      
      // Adds the force vector to the net force
      fNet = VMath.addVectors(fNet, VMath.polarToCart(forceList.get(i).vector));
      
      // Subtracts the frame's time from the force's timer
      forceList.get(i).time = forceList.get(i).time - Time.deltaTime();
      if(forceList.get(i).time <= 0) { // If the timer runs out, remove force
        forceList.remove(i);
      }
    }
  }
  
  // Adds friction to the net force
  private void addFriction() {
    // Calculates friction magnitude and direction
    fFriction.x = m * G * mu;
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
}












