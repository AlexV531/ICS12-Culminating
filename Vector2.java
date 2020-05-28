
public class Vector2 {
  
  double x;
  double y;
  
  public Vector2() { 
    x = 0;
    y = 0;
  }
  
  public Vector2(double x, double y) {
    
    this.x = x;
    this.y = y;
    
  }

  public void setToZero() {
    x = 0;
    y = 0;
  }
  
  public String toString() {
    
    return "[" + x + ", " + y + "]";
    
  }
  
}
