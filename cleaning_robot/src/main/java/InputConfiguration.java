import java.util.List;

public class InputConfiguration {
    private List<List<String>> map;
    private StartConfiguration start;
    private List<Command> commands;
    private int battery;

    public List<List<String>> getMap() {
        return map;
    }

    public void setMap(List<List<String>> map) {
        this.map = map;
    }

    public StartConfiguration getStart() {
        return start;
    }

    public void setStart(StartConfiguration start) {
        this.start = start;
    }

    public List<Command> getCommands() {
        return commands;
    }

    public void setCommands(List<Command> commands) {
        this.commands = commands;
    }

    public int getBattery() {
        return battery;
    }

    public void setBattery(int battery) {
        this.battery = battery;
    }
}

