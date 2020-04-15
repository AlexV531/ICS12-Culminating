import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

public class Mouse {
  
  public static Vector2 pos;
  
  public Mouse() { 
    pos = new Vector2();
  }
  public void updateMousePos(Point mousePoint, int fWidth, int fHeight, Player player) {
     
    if(mousePoint != null){
      pos.x = (mousePoint.x - fWidth/2) + player.p.x;
      pos.y = (mousePoint.y - fHeight/2) + player.p.y;
    }
  }
  
}
