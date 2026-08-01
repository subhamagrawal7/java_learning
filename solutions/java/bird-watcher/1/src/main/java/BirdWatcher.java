
class BirdWatcher {
    private final int[] birdsPerDay;

    public BirdWatcher(int[] birdsPerDay) {
        this.birdsPerDay = birdsPerDay.clone();
    }

    public static int[] getLastWeek() {
        int[] lastWeekBirds = { 0, 2, 5, 3, 7, 8, 4 };
        return lastWeekBirds;
    }

    public int getToday() {
        return birdsPerDay[birdsPerDay.length - 1];
    }

    public void incrementTodaysCount() {
        birdsPerDay[birdsPerDay.length - 1] = birdsPerDay[birdsPerDay.length - 1] + 1;
    }

    public boolean hasDayWithoutBirds() {
        for (int dayCount : birdsPerDay) {
            if (dayCount == 0) {
                return true;
            }
        }
        return false;
    }

    public int getCountForFirstDays(int numberOfDays) {
        int totalBirds = 0;
        int maxDays = Math.min(numberOfDays, birdsPerDay.length);
        for (int i = 0; i < maxDays; i++) {
            totalBirds += birdsPerDay[i];
        }
        return totalBirds;
    }

    public int getBusyDays() {
        int cnt = 0;
        for (int birdCount : birdsPerDay) {
            if (birdCount >= 5) {
                cnt += 1;
            }
        }
        return cnt;
    }
}
