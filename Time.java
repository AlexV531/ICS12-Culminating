
public class Time {
  
  private static long lastTime = System.currentTimeMillis();
  private static long time;
  private static double timestep;
  
  
  public static void calcTime() {
    
    time = System.currentTimeMillis();
    timestep = 0.001 * (time - lastTime);
    /*if (timestep <= 0 || timestep > 1.0) {
        timestep = 0.001;  // avoid absurd time steps
    }*/
    lastTime = time;
  }
  
  public static double deltaTime() {
    return timestep;
  }
}
