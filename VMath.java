

public class VMath {
  
  public static Vector2 addVectors(Vector2 v1, Vector2 v2) {
    
    Vector2 sum = new Vector2();
    
    sum.x = v1.x + v2.x;
    sum.y = v1.y + v2.y;
    
    return sum;
    
  }
  
  public static Vector2 multiplyByScalar(Vector2 v, double scalar) {
    
    Vector2 product = new Vector2();
    
    product.x = v.x * scalar;
    product.y = v.y * scalar;
    
    return product;
    
  }
  
  public static Vector2 polarToCart(Vector2 v) {
    
    Vector2 ans = new Vector2();
    
    ans.x = v.x * Math.cos(v.y);
    ans.y = v.x * Math.sin(v.y);
    
    return ans; 
    
  }
  
  public static Vector2 cartToPolar(Vector2 v) {
    
    Vector2 ans = new Vector2();
    
    ans.x = Math.sqrt(v.x * v.x + v.y * v.y);
    ans.y = Math.atan2(v.y, v.x);
    
    return ans;
    
  }
  
  public static double getAngleBetweenPoints(Vector2 origin, Vector2 arm) {
    
    double angle = 0;
    
    angle = Math.atan2(arm.y - origin.y, arm.x - origin.x);
    
    return angle;
  }
  
  public static double getDistanceBetweenPoints(Vector2 origin, Vector2 arm) {
    
    double distance = Math.sqrt((arm.y - origin.y)*(arm.y - origin.y) + (arm.x - origin.x)*(arm.x - origin.x));
    
    return distance;
  }
  // (for point circle make one of the r2 = 0)
  public static boolean circleCircle(Vector2 c1, Vector2 c2, double r1, double r2) {
    
    double distance = getDistanceBetweenPoints(c1, c2);
    
    if(distance > r1 + r2) {
      
      return false;
    }
    else {
      
      return true;
    }    
  }
  
  public static boolean linePoint(Vector2 line1, Vector2 line2, Vector2 p) {
    
    double d1 = getDistanceBetweenPoints(p, line1);
    double d2 = getDistanceBetweenPoints(p, line2);
    
    double len = getDistanceBetweenPoints(line1, line2);
    
    double buffer = 0.1;
    
    if (d1 + d2 >= len-buffer && d1 + d2 <= len+buffer) {
      return true;
    }
    else {
      return false;
    }
  }
  
  public static boolean lineCircle(Vector2 line1, Vector2 line2, Vector2 c, double r) {
    
    // is either end INSIDE the circle?
    // if so, return true immediately
    boolean inside1 = circleCircle(line1, c, r, 0);
    boolean inside2 = circleCircle(line2, c, r, 0);
    if(inside1 || inside2) {
      return true;
    }
    
    // get length of the line
    double len = getDistanceBetweenPoints(line1, line2);
    
    // get dot product of the line and circle
    double dot = ( ((c.x-line1.x)*(line2.x-line1.x)) + ((c.y-line1.y)*(line2.y-line1.y)) ) / Math.pow(len, 2);
    
    Vector2 closest = new Vector2();
    
    // find the closest point on the line
    closest.x = line1.x + (dot * (line2.x - line1.x));
    closest.y = line1.y + (dot * (line2.y - line1.y));
    
    // is this point actually on the line segment?
    // if so keep going, but if not, return false
    boolean onSegment = linePoint(line1, line2, closest);
    if (!onSegment) {
      return false;
    }
    
    // get distance to closest point from circle centre
    double distance = getDistanceBetweenPoints(c, closest);
    
    // if the distance is less than or equal to the radius of the circle:
    if (distance <= r) {
      return true;
    }
    else {
      return false;
    }
    
  }
  

  
  
  
}
