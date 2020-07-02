import java.awt.*;

public class ParticleEffect {
    
    Vector2 p = new Vector2();
    int size;
    //boolean active = false;

    Image img;

    public ParticleEffect(Vector2 pos) {
        
        size = (int)(Math.random() * 100) + 50;
        p.x = pos.x - (size - 64)/2;
        p.y = pos.y - (size - 64)/2;

    }


    public void drawParticleEffect(Graphics2D g2D) {

        g2D.setColor(new Color(120, 10, 10));
        g2D.fillOval((int)p.x, (int)p.y, size, size);
        
    }

}