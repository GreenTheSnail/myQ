import java.util.TreeSet;

public class CleaningRobotState {
    private Coordinates currentCoordinates;
    private Direction currentDirection;
    private int currentBattery;
    private Boolean isBatteryEnough;
    private int backOffInitializedCount;
    private TreeSet<Coordinates> visited;
    private TreeSet<Coordinates> cleaned;

    public CleaningRobotState(Coordinates currentCoordinates, Direction currentDirection, int currentBattery, Boolean isBatteryEnough, int backOffInitializedCount, TreeSet<Coordinates> visited, TreeSet<Coordinates> cleaned) {
        this.currentCoordinates = currentCoordinates;
        this.currentDirection = currentDirection;
        this.currentBattery = currentBattery;
        this.isBatteryEnough = isBatteryEnough;
        this.backOffInitializedCount = backOffInitializedCount;
        this.visited = visited;
        this.cleaned = cleaned;
    }


    public Coordinates getCurrentCoordinates() {
        return currentCoordinates;
    }

    public void setCurrentCoordinates(Coordinates currentCoordinates) {
        this.currentCoordinates = currentCoordinates;
    }

    public Direction getCurrentDirection() {
        return currentDirection;
    }

    public void setCurrentDirection(Direction currentDirection) {
        this.currentDirection = currentDirection;
    }

    public int getCurrentBattery() {
        return currentBattery;
    }

    public void setCurrentBattery(int currentBattery) {
        this.currentBattery = currentBattery;
    }

    public Boolean getBatteryEnough() {
        return isBatteryEnough;
    }

    public void setBatteryEnough(Boolean batteryEnough) {
        isBatteryEnough = batteryEnough;
    }

    public int getBackOffInitializedCount() {
        return backOffInitializedCount;
    }

    public void setBackOffInitializedCount(int backOffInitializedCount) {
        this.backOffInitializedCount = backOffInitializedCount;
    }

    public TreeSet<Coordinates> getVisited() {
        return visited;
    }

    public void setVisited(TreeSet<Coordinates> visited) {
        this.visited = visited;
    }

    public TreeSet<Coordinates> getCleaned() {
        return cleaned;
    }

    public void setCleaned(TreeSet<Coordinates> cleaned) {
        this.cleaned = cleaned;
    }
    @Override
    public String toString() {
        return "{" + "\n" +
                "\"visited\" : " + visited.toString().replace("}, ", "}") + ",\n" +
                "\"cleaned\" : " + cleaned.toString().replace("}, ", "}") + ",\n" +
                "\"final\" : " + "{ \"X\" : " + currentCoordinates.getX() + ", \"Y\" : " + currentCoordinates.getY() + ", \"facing\" : \"" + currentDirection + "\"}" + ",\n" +
                "\"battery\" : " + currentBattery + "\n" +
                '}';
    }
}
