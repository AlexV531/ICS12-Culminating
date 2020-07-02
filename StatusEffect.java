public class StatusEffect {
    
    int function = 0; // 0 is the null effect, it does nothing, 1: rate of fire
    boolean active = false;
    double effectTimer;
    double effectDelay;
    static Player target;

    public boolean isActive() {
        return active;
    }

    public void calcTimer(double deltaTime) {

        if(active) {
            //System.out.println(effectTimer);
            effectTimer += deltaTime;
            if(effectTimer >= effectDelay) {
                //System.out.println("deactivate");
                deactivate();
            }
        }
    }

    public void activate(int func, double modifier, double delay) {

        active = true;
        function = func;
        if(function == 1) {
            target.setRateOfFireMod(modifier);
        }
        effectDelay = delay;
    }

    public void deactivate() {

        active = false;
        effectTimer = 0;
        if(function == 1) {
            target.setRateOfFireMod(1);
        }
    }

}