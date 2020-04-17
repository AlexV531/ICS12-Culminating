
public class Time {
  
  private static long lastTime = System.currentTimeMillis();
  private static long time;
  private static double timestep;
  
  
  public static double calcTime() {
    
    time = System.currentTimeMillis();
    timestep = 0.001 * (time - lastTime);
    lastTime = time;
    return timestep;
  }
  
  public static double deltaTime() {
    return timestep;
  }
}
