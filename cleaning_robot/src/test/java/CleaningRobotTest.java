import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class CleaningRobotTest {

    private static final String OUTPUT_TEST_FILE_NAME = "testResultFile.json";
    private static final String OUTPUT_ABSOLUTE_PATH = getAbsolutePath(OUTPUT_TEST_FILE_NAME);
    private CleaningRobot cleaningRobot;

    @BeforeEach
    public void setUp() {
        cleaningRobot = new CleaningRobot();
    }

    @AfterEach
    public void tearDown() {
        File file = new File(getAbsolutePath(OUTPUT_TEST_FILE_NAME));
        if (file.exists()) {
            file.delete();
        }
    }


    @Test
    void validScenarioTest1() {
        String INPUT_TEST_FILE_NAME = "test1.json";
        String EXPECTED_RESULT_TEST_FILE_NAME = "test1_result.json";

        cleaningRobot.run(INPUT_TEST_FILE_NAME, OUTPUT_TEST_FILE_NAME);
        File file = new File(OUTPUT_ABSOLUTE_PATH);
        //check if file exists
        assertTrue(file.exists());
        String actualResult = readStringFromFile(OUTPUT_ABSOLUTE_PATH);
        String expectedResult = readStringFromFile( getAbsolutePath(EXPECTED_RESULT_TEST_FILE_NAME));
        //check if file data are valid
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void validScenarioTest2() {
        String INPUT_TEST_FILE_NAME = "test2.json";
        String EXPECTED_RESULT_TEST_FILE_NAME = "test2_result.json";

        cleaningRobot.run(INPUT_TEST_FILE_NAME, OUTPUT_TEST_FILE_NAME);
        File file = new File(OUTPUT_ABSOLUTE_PATH);
        //check if file exists
        assertTrue(file.exists());
        String actualResult = readStringFromFile(OUTPUT_ABSOLUTE_PATH);
        String expectedResult = readStringFromFile( getAbsolutePath(EXPECTED_RESULT_TEST_FILE_NAME));
        //check if file data are valid
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void stuckScenarioTest() {
        String INPUT_TEST_FILE_NAME = "testStuck.json";
        String EXPECTED_RESULT_TEST_FILE_NAME = "testStuck_result.json";

        cleaningRobot.run(INPUT_TEST_FILE_NAME, OUTPUT_TEST_FILE_NAME);
        File file = new File(OUTPUT_ABSOLUTE_PATH);
        //check if file exists
        assertTrue(file.exists());
        String actualResult = readStringFromFile(OUTPUT_ABSOLUTE_PATH);
        String expectedResult = readStringFromFile( getAbsolutePath(EXPECTED_RESULT_TEST_FILE_NAME));
        //check if file data are valid
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void outOfBatteryScenarioTest() {
        String INPUT_TEST_FILE_NAME = "testOutOfBattery.json";
        String EXPECTED_RESULT_TEST_FILE_NAME = "testOutOfBattery_result.json";

        cleaningRobot.run(INPUT_TEST_FILE_NAME, OUTPUT_TEST_FILE_NAME);
        File file = new File(OUTPUT_ABSOLUTE_PATH);
        //check if file exists
        assertTrue(file.exists());
        String actualResult = readStringFromFile(OUTPUT_ABSOLUTE_PATH);
        String expectedResult = readStringFromFile( getAbsolutePath(EXPECTED_RESULT_TEST_FILE_NAME));
        //check if file data are valid
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void inputFileDoesNotExist() {
        String INPUT_TEST_FILE_NAME = "nonExisting.json";
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cleaningRobot.run(INPUT_TEST_FILE_NAME, OUTPUT_TEST_FILE_NAME);
        });
        String expectedMessage = "Unable to read file: " + getAbsolutePath(INPUT_TEST_FILE_NAME);
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    private static String readStringFromFile(String path) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            StringBuilder content = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            return content.toString();
        } catch (IOException e) {
            throw new RuntimeException("Unable to read file.");
        }
    }


    private static String getAbsolutePath(String fileName) {
        String inputRelativePath = "src/main/files/" + fileName;
        return Paths.get(inputRelativePath).toAbsolutePath().toString();
    }
}
