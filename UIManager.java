import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;

public class UIManager {
    
    static ArrayList<Vector2> topLeftButton = new ArrayList<Vector2>();
    static ArrayList<Vector2> bottomRightButton = new ArrayList<Vector2>();
    static ArrayList<Integer> screenList = new ArrayList<Integer>();
    static ArrayList<Integer> functionList = new ArrayList<Integer>();
    static ArrayList<Color> colourList = new ArrayList<Color>();
    static ArrayList<String> titleList = new ArrayList<String>();

    static Font h = new Font("Helvetica", Font.PLAIN, 24);

    static ImageIcon hb = new ImageIcon("images/HealthBar2.png");
    static Image healthBar = hb.getImage();

    public static void addButton(Vector2 topLeft, Vector2 bottomRight, int screen, int function, String title) {
        topLeftButton.add(topLeft);
        bottomRightButton.add(bottomRight);
        screenList.add(screen);
        functionList.add(function);
        colourList.add(Color.GRAY);
        titleList.add(title);

    }
    // This method checks if a mouse click has hit a button and then returns the resulting function number.
    // The function number is dealt with in the MainGame class
    public static int buttonCheck(int currentScreen, Vector2 mousePos, boolean clicked) {

        for(int i = 0; i < screenList.size(); i++) {
            // If the button is on the correct screen
            if(currentScreen == screenList.get(i)) {
                // If the mouse position is within the parameters of the button
                if(mousePos.x >= topLeftButton.get(i).x && mousePos.y >= topLeftButton.get(i).y && mousePos.x <= bottomRightButton.get(i).x && mousePos.y <= bottomRightButton.get(i).y) {
                    
                    if(clicked) {
                        return functionList.get(i);
                    }
                    else {
                        colourList.set(i, Color.RED);
                    }
                }
                else {
                    colourList.set(i, Color.GRAY);
                }
            }
        }
        return -1;
    }

    public static void drawButtons(Graphics2D g2D, int currentScreen) {
        for(int i = 0; i < screenList.size(); i++) {
            // If the button is on the correct screen
            if(currentScreen == screenList.get(i)) {
                g2D.setFont(h);

                int width = (int)(bottomRightButton.get(i).x - topLeftButton.get(i).x);
                int height = (int)(bottomRightButton.get(i).y - topLeftButton.get(i).y);

                int stringWidth = g2D.getFontMetrics().stringWidth(titleList.get(i));
                
                // Draws the button chassis
                g2D.setColor(colourList.get(i));
                g2D.fillRect((int)topLeftButton.get(i).x, (int)topLeftButton.get(i).y, width, height);
                // Draws the button border
                g2D.setColor(Color.BLACK);
                g2D.drawRect((int)topLeftButton.get(i).x, (int)topLeftButton.get(i).y, width, height);
                // Adds the button title
                g2D.drawString(titleList.get(i), (int)topLeftButton.get(i).x + (width/2 - stringWidth/2), (int)bottomRightButton.get(i).y - (height/2 - 24/2));
            }
        }
    }

    public static void headsUpDisplay(Graphics2D g2D, Player player) {

        g2D.setColor(Color.RED);
        // 384 pixels = full health
        g2D.fillRect((int)player.p.x - 472, (int)player.p.y + 416, (int)(player.hp * 3.84), 44);

        g2D.drawImage(healthBar, (int)player.p.x - 568, (int)player.p.y + 404, null);


    }



}