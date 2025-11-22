import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import org.jsoup.Jsoup;         // JSoup library
import org.jsoup.nodes.Document;

public class Main {

    // Read lines from file
    public static List<String> readLines(String filePath) throws IOException {
        return Files.readAllLines(Paths.get(filePath));
    }

    // Check if text contains any keyword from file
    public static boolean containsKeyword(String content, List<String> words) {
        for (String w : words) {
            if (content.toLowerCase().contains(w.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    // Read website text using JSoup
    public static String fetchWebsiteText(String url) {
        try {
            Document doc = Jsoup.connect(url).get();
            return doc.text();
        } catch (Exception e) {
            return "ERROR_FETCHING";
        }
    }

    public static void main(String[] args) {

        try {
            String base = System.getProperty("user.dir") + "\\data\\";

            // Load files
            List<String> fake = readLines(base + "fake.txt");
            List<String> real = readLines(base + "real.txt");
            List<String> scam = readLines(base + "scam.txt");
            List<String> rumor = readLines(base + "rumor.txt");
            List<String> fraud = readLines(base + "fraud.txt");
            List<String> official = readLines(base + "official.txt");

            Scanner sc = new Scanner(System.in);

            System.out.println("Choose Option:");
            System.out.println("1 - Check Text");
            System.out.println("2 - Check Website URL");
            int choice = sc.nextInt();
            sc.nextLine(); // flush

            String input = "";

            if (choice == 1) {
                System.out.println("Enter text to check:");
                input = sc.nextLine();
            } else if (choice == 2) {
                System.out.println("Enter website URL:");
                String url = sc.nextLine();
                input = fetchWebsiteText(url);

                if (input.equals("ERROR_FETCHING")) {
                    System.out.println("❌ Unable to fetch website.");
                    return;
                }
            } else {
                System.out.println("Invalid choice.");
                return;
            }

            // Classification
            if (containsKeyword(input, fake)) {
                System.out.println("⚠️ FAKE CONTENT DETECTED");
            } else if (containsKeyword(input, real)) {
                System.out.println("✅ REAL CONTENT DETECTED");
            } else {
                System.out.println("❓ NOT ENOUGH DATA TO CLASSIFY");
            }

            // Category detection
            System.out.println("\nCategory Prediction:");
            if (containsKeyword(input, scam)) System.out.println(" - Scam Alert");
            if (containsKeyword(input, fraud)) System.out.println(" - Fraud Related");
            if (containsKeyword(input, rumor)) System.out.println(" - Rumor");
            if (containsKeyword(input, official)) System.out.println(" - Official Verified");

            sc.close();

        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }
}
