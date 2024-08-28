import org.json.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Random;

public class DestinationHashGenerator {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Correct Pattern: java -jar DestinationHashGenerator.jar <PRN Number> <JSON file path>");
            System.exit(1);
        }
        String prnNumber = args[0].toLowerCase();
        String jsonFilePath = args[1];
        String destinationValue = getDestinationValue(jsonFilePath);
        if (destinationValue == null) {
            System.err.println("Key 'destination' not found in JSON file.");
            System.exit(1);
        }
        String randomString = generateRandomString();
        String concatenatedString = prnNumber + destinationValue + randomString;
        String md5Hash = DigestUtils.md5Hex(concatenatedString);
        System.out.println(md5Hash + ";" + randomString);
    }
    private static String getDestinationValue(String jsonFilePath) {
        try (Scanner scanner = new Scanner(new File(jsonFilePath))) {
            StringBuilder jsonContent = new StringBuilder();
            while (scanner.hasNextLine()) {
                jsonContent.append(scanner.nextLine());
            }
            JSONObject jsonObject = new JSONObject(jsonContent.toString());
            return traverseJsonObject(jsonObject, "destination");
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + jsonFilePath);
            System.exit(1);
        }
        return null;
    }
    private static String traverseJsonObject(JSONObject jsonObject, String key) {
        if (jsonObject.has(key)) {
            return jsonObject.getString(key);
        }
        for (String currentKey : jsonObject.keySet()) {
            Object value = jsonObject.get(currentKey);
            if (value instanceof JSONObject) {
                String result = traverseJsonObject((JSONObject) value, key);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }
    private static String generateRandomString() {
        Random random = new Random();
        StringBuilder randomString = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {
            char randomChar;
            if (random.nextBoolean()) {
                randomChar = (char) (random.nextInt(26) + 'a');
            } else {
                randomChar = (char) (random.nextInt(26) + 'A');
            }
            randomString.append(randomChar);
        }
        return randomString.toString();
    }
}
