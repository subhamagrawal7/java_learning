public class JedliksToyCar {
    private int distCovered = 0;
    private int remainingBattery = 100;

    public static JedliksToyCar buy() {
        JedliksToyCar newCar = new JedliksToyCar();
        return newCar;
    }

    public String distanceDisplay() {
        return "Driven " + distCovered + " meters";
    }

    public String batteryDisplay() {
        return "Battery " + (remainingBattery == 0 ? "empty" : "at " + remainingBattery + "%");
    }

    public void drive() {
        if (remainingBattery == 0) {
            return;
        }

        distCovered += 20;
        remainingBattery -= 1;
    }
}
