import java.util.ArrayList;

public class UIManager {
    
    static ArrayList<Vector2> topRightButton = new ArrayList<Vector2>();
    static ArrayList<Vector2> bottomLeftButton = new ArrayList<Vector2>();
    static ArrayList<Integer> screenList = new ArrayList<Integer>();
    static ArrayList<Integer> functionList = new ArrayList<Integer>();

    public static void addButton(Vector2 topRight, Vector2 bottomLeft, int screen, int function) {
        topRightButton.add(topRight);
        bottomLeftButton.add(bottomLeft);
        screenList.add(screen);
        functionList.add(function);
    }
    // This method checks if a mouse click has hit a button and then returns the resulting function number.
    // The function number is dealt with in the MainGame class
    public static int buttonCheck(int x, int y) {
        return 0;
    }


}