public class Lasagna {
    public int expectedMinutesInOven() {
        return 40;
    }

    public int remainingMinutesInOven(int actualMinutesInOven) {
        return expectedMinutesInOven() - actualMinutesInOven;
    }

    public int preparationTimeInMinutes(int layersAddedToLasagna) {
        return 2 * layersAddedToLasagna;
    }

    public int totalTimeInMinutes(int layersAddedToLasagna, int actualMinutesInOven) {
        return actualMinutesInOven + preparationTimeInMinutes(layersAddedToLasagna);
    }

}
