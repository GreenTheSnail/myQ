public class Main {

    public static void main(String[] args) {
        //You can replace args[0] with file name you want
        String inputFileName = args[0];
        //You can replace args[1] with file name you want
        String outputFileName = args[1];
        CleaningRobot cleaningRobot = new CleaningRobot();
        cleaningRobot.run(inputFileName, outputFileName);
    }
}
