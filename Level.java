
import java.awt.*;

public class Level {
  
  public Level() { 
    
  }
  
  public static void addFloor(Graphics2D g2D, int x, int y, int width, int height, Image tile) {
    
    for(int i = 0; i < width*height; i++) {
      
      g2D.drawImage(tile, x + (i%width)*tile.getWidth(null), y + (i/width)*tile.getHeight(null), null);
      
    }
  }
  
}
