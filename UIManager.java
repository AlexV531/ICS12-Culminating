import java.util.ArrayList;
import java.awt.*;

public class UIManager {
    
    static ArrayList<Vector2> topLeftButton = new ArrayList<Vector2>();
    static ArrayList<Vector2> bottomRightButton = new ArrayList<Vector2>();
    static ArrayList<Integer> screenList = new ArrayList<Integer>();
    static ArrayList<Integer> functionList = new ArrayList<Integer>();
    static ArrayList<Color> colourList = new ArrayList<Color>();
    static ArrayList<String> titleList = new ArrayList<String>();

    static Font h = new Font("Helvetica", Font.PLAIN, 24);

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
                // Draws the button chassis
                g2D.setColor(colourList.get(i));
                g2D.fillRect((int)topLeftButton.get(i).x, (int)topLeftButton.get(i).y, (int)(bottomRightButton.get(i).x - topLeftButton.get(i).x), (int)(bottomRightButton.get(i).y - topLeftButton.get(i).y));
                // Adds the button title
                g2D.setFont(h);
                g2D.setColor(Color.BLACK);
                g2D.drawString(titleList.get(i), (int)topLeftButton.get(i).x, (int)bottomRightButton.get(i).y);
            }
        }
    }



}