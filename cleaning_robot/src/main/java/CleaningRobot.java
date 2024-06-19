import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.IntStream;

public class CleaningRobot {
    public static final int TL_BATTERY_COST = 1;
    public static final int TR_BATTERY_COST = 1;
    public static final int A_BATTERY_COST = 2;
    public static final int B_BATTERY_COST = 3;
    public static final int C_BATTERY_COST = 5;
    public static final List<List<Command>> BACK_OFF_STRATEGY = List.of(
            List.of(Command.TR, Command.A, Command.TL),
            List.of(Command.TR, Command.A, Command.TR),
            List.of(Command.TR, Command.A, Command.TR),
            List.of(Command.TR, Command.B, Command.TR, Command.A),
            List.of(Command.TL, Command.TL, Command.A)
    );

    public void run(String inputFileName, String outputFileName) {
        String inputAbsolutePath = getAbsolutePath(inputFileName);
        String outputAbsolutePath = getAbsolutePath(outputFileName);
        Gson gson = new Gson();
        InputConfiguration input;
        try (FileReader reader = new FileReader(inputAbsolutePath)) {
            input = gson.fromJson(reader, InputConfiguration.class);
        } catch (IOException e) {
            throw new RuntimeException("Unable to read file: " + inputAbsolutePath, e);
        }
        CleaningRobotState state = new CleaningRobotState(new Coordinates(
                input.getStart().getX(),
                input.getStart().getY()),
                input.getStart().getFacing(),
                input.getBattery(),
                true,
                0,
                new TreeSet<>(),
                new TreeSet<>()
        );
        Map<Coordinates, String> transferedMap = transferInputMap(input.getMap());
        try {
            input.getCommands().forEach(command -> {
                performCommand(command, state, transferedMap);
            });
        } catch (StuckException ignored) {
            System.out.println("Cleaning robot is stuck.");
        } catch (OutOfBatteryException ignored) {
            System.out.println("Cleaning robot is out of battery.");
        }
        writeResultsToFile(state, outputAbsolutePath);
    }

    private static String getAbsolutePath(String fileName) {
        String inputRelativePath = "src/main/files/" + fileName;
        return Paths.get(inputRelativePath).toAbsolutePath().toString().replace("/src/main/java", "");
    }

    private static void performCommand(Command command, CleaningRobotState state, Map<Coordinates, String> transferedMap) {
        if (state.getBatteryEnough()) {
            switch (command) {
                case TL -> {
                    if (isBatteryCapacityEnough(state.getCurrentBattery(), TL_BATTERY_COST)) {
                        turnLeftOperation(state);
                    } else {
                        state.setBatteryEnough(false);
                    }
                }
                case TR -> {
                    if (isBatteryCapacityEnough(state.getCurrentBattery(), TR_BATTERY_COST)) {
                        turnRightOperation(state);
                    } else {
                        state.setBatteryEnough(false);
                    }
                }
                case A -> {
                    if (isBatteryCapacityEnough(state.getCurrentBattery(), A_BATTERY_COST)) {
                        advanceOperation(state, transferedMap);
                    } else {
                        state.setBatteryEnough(false);
                    }
                }
                case B -> {
                    if (isBatteryCapacityEnough(state.getCurrentBattery(), B_BATTERY_COST)) {
                        backOperation(state, transferedMap);
                    } else {
                        state.setBatteryEnough(false);
                    }
                }
                case C -> {
                    if (isBatteryCapacityEnough(state.getCurrentBattery(), C_BATTERY_COST)) {
                        cleanOperation(state);
                    } else {
                        state.setBatteryEnough(false);
                    }
                }
            }
        } else {
            throw new OutOfBatteryException();
        }
    }

    private static Map<Coordinates, String> transferInputMap(List<List<String>> map) {
        Map<Coordinates, String> resultMap = new HashMap<>();
        IntStream.range(0, map.size()).forEach(i -> {
            List<String> list = map.get(i);
            IntStream.range(0, list.size()).forEach(j -> {
                Coordinates coordinates = new Coordinates(j, i);
                resultMap.put(coordinates, list.get(j));
            });
        });
        return resultMap;
    }

    private static void turnLeftOperation(CleaningRobotState state) {
        System.out.println("Performing TL operation.");
        state.setCurrentDirection(switch (state.getCurrentDirection()) {
            case N -> Direction.W;
            case E -> Direction.N;
            case S -> Direction.E;
            case W -> Direction.S;
        });
        state.setCurrentBattery(state.getCurrentBattery() - TL_BATTERY_COST);
    }

    private static void turnRightOperation(CleaningRobotState state) {
        System.out.println("Performing TR operation.");
        state.setCurrentBattery(state.getCurrentBattery() - TR_BATTERY_COST);
        state.setCurrentDirection(switch (state.getCurrentDirection()) {
            case N -> Direction.E;
            case E -> Direction.S;
            case S -> Direction.W;
            case W -> Direction.N;
        });
    }

    private static void advanceOperation(CleaningRobotState state, Map<Coordinates, String> transferedMap) {
        TreeSet<Coordinates> newVisitedSet = addElementToSet(state.getVisited(), state.getCurrentCoordinates());
        state.setVisited(newVisitedSet);
        state.setCurrentBattery(state.getCurrentBattery() - A_BATTERY_COST);
        Coordinates possibleCoordinates = getPossibleAdvanceCoordinates(state);
        if (moveCanBePerformed(transferedMap.get(possibleCoordinates))) {
            System.out.println("Performing A operation.");
            state.setCurrentCoordinates(possibleCoordinates);
        } else {
            initializeBackOffStrategy(state, transferedMap);
        }
    }

    private static Coordinates getPossibleAdvanceCoordinates(CleaningRobotState state) {
        Coordinates possibleCoordinates = new Coordinates(state.getCurrentCoordinates().getX(), state.getCurrentCoordinates().getY());
        switch (state.getCurrentDirection()) {
            case N -> possibleCoordinates.setY(possibleCoordinates.getY() - 1);
            case E -> possibleCoordinates.setX(possibleCoordinates.getX() + 1);
            case S -> possibleCoordinates.setY(possibleCoordinates.getY() + 1);
            case W -> possibleCoordinates.setX(possibleCoordinates.getX() - 1);
        }
        return possibleCoordinates;
    }

    private static void backOperation(CleaningRobotState state, Map<Coordinates, String> transferedMap) {
        TreeSet<Coordinates> newVisitedSet = addElementToSet(state.getVisited(), state.getCurrentCoordinates());
        state.setVisited(newVisitedSet);
        state.setCurrentBattery(state.getCurrentBattery() - B_BATTERY_COST);
        Coordinates possibleCoordinates = getPossibleBackCoordinates(state);
        if (moveCanBePerformed(transferedMap.get(possibleCoordinates))) {
            System.out.println("Performing B operation.");
            state.setCurrentCoordinates(possibleCoordinates);
        } else {
            initializeBackOffStrategy(state, transferedMap);
        }
    }

    private static Coordinates getPossibleBackCoordinates(CleaningRobotState state) {
        Coordinates possibleCoordinates = new Coordinates(state.getCurrentCoordinates().getX(), state.getCurrentCoordinates().getY());
        switch (state.getCurrentDirection()) {
            case N -> possibleCoordinates.setY(possibleCoordinates.getY() + 1);
            case E -> possibleCoordinates.setX(possibleCoordinates.getX() - 1);
            case S -> possibleCoordinates.setY(possibleCoordinates.getY() - 1);
            case W -> possibleCoordinates.setX(possibleCoordinates.getX() + 1);
        }
        return possibleCoordinates;
    }

    private static void initializeBackOffStrategy(CleaningRobotState state, Map<Coordinates, String> transferedMap) throws StuckException {
        System.out.println("Initializing back off strategy.");
        state.setBackOffInitializedCount(state.getBackOffInitializedCount() + 1);
        int currentCount = state.getBackOffInitializedCount();
        if (currentCount <= BACK_OFF_STRATEGY.size()) {
            List<Command> currentBackOffCommands = BACK_OFF_STRATEGY.get(currentCount - 1);
            IntStream.range(0, currentBackOffCommands.size()).forEach(index -> {
                        Command command = currentBackOffCommands.get(index);
                        performCommand(command, state, transferedMap);
                    }
            );
            state.setBackOffInitializedCount(0);
        } else {
            throw new StuckException();
        }
    }

    private static void cleanOperation(CleaningRobotState state) {
        System.out.println("Performing C operation.");
        TreeSet<Coordinates> newCleanedSet = addElementToSet(state.getCleaned(), state.getCurrentCoordinates());
        state.setCleaned(newCleanedSet);
        state.setCurrentBattery(state.getCurrentBattery() - C_BATTERY_COST);
    }

    private static Boolean isBatteryCapacityEnough(int batteryState, int operationCost) {
        return operationCost <= batteryState;
    }

    private static Boolean moveCanBePerformed(String mapCell) {
        return mapCell != null && mapCell.equals("S");
    }

    private static TreeSet<Coordinates> addElementToSet(Set<Coordinates> set, Coordinates element) {
        TreeSet<Coordinates> newSet = new TreeSet<>(set);
        newSet.add(element);
        return newSet;
    }

    private static void writeResultsToFile(CleaningRobotState state, String outputAbsolutePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputAbsolutePath))) {
            writer.write(state.toString());
            System.out.println("Result written to file successfully.");
        } catch (IOException e) {
            throw new RuntimeException("Unable to write file: " + outputAbsolutePath, e);
        }
    }
}
