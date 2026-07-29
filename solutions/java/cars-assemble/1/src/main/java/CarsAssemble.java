public class CarsAssemble {
    public static final int PRODUCTION_PER_HOUR = 221;

    public double productionRatePerHour(int speed) {
        double baseRate = PRODUCTION_PER_HOUR * speed;
        if (speed <= 4) {
            return baseRate;
        } else if (speed <= 8) {
            return baseRate * 0.9;
        } else if (speed == 9) {
            return baseRate * 0.8;
        } else {
            return baseRate * 0.77;
        }
    }

    public int workingItemsPerMinute(int speed) {
        return (int) (productionRatePerHour(speed) / 60);
    }
}
