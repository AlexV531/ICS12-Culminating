import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

public class Level {
  
  public Level() { 
    
  }
  
  public static void addFloor(Graphics g, int x, int y, int width, int height, Image tile) {
    
    for(int i = 0; i < width*height; i++) {
      
      g.drawImage(tile, x + (i%width)*tile.getWidth(null), y + (i/width)*tile.getHeight(null), null);
      
    }
  }
  
}
