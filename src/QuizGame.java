import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class QuizGame {
    private static final String CATEGORY1 = "Category 1";
    private static final String CATEGORY2 = "Category 2";
    private static final String CATEGORY3 = "Category 3";

    private static final Map<String, List<String[]>> questionsMap = new HashMap<>();

    public static void main(String[] args) {
        initializeQuestions();
        displayCategories();

        Scanner scanner = new Scanner(System.in);
        String category = scanner.nextLine().toLowerCase();

        while (!questionsMap.containsKey(category)) {
            System.out.println("Invalid category selection. Please try again:");
            category = scanner.nextLine().toLowerCase(); // Get new category input
        }

        playQuiz(category);
    }

    private static void initializeQuestions() {
        try {
            String category1File = "category1.csv";
            List<String[]> category1Questions = readQuestionsFromCSV(category1File);
            questionsMap.put(CATEGORY1.toLowerCase(), category1Questions);

            String category2File = "category2.csv";
            List<String[]> category2Questions = readQuestionsFromCSV(category2File);
            questionsMap.put(CATEGORY2.toLowerCase(), category2Questions);

            String category3File = "category3.csv";
            List<String[]> category3Questions = readQuestionsFromCSV(category3File);
            questionsMap.put(CATEGORY3.toLowerCase(), category3Questions);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<String[]> readQuestionsFromCSV(String filename) throws IOException {
        List<String[]> questions = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("src/" + filename))) {
            String line;
            List<String> questionData = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Question")) {
                    if (!questionData.isEmpty()) {
                        questions.add(questionData.toArray(new String[0]));
                        questionData.clear();
                    }
                    questionData.add(line.substring(line.indexOf(":") + 2)); // Extract the question
                } else if (line.startsWith("Correct Answer") || line.startsWith("Wrong Answer")) {
                    questionData.add(line.substring(line.indexOf(":") + 2)); // Extract the answer option
                }
            }

            if (!questionData.isEmpty()) {
                questions.add(questionData.toArray(new String[0]));
            }
        }

        return questions;
    }


    private static void displayCategories() {
        System.out.println("Select a category:(Type whole word + number):");
        System.out.println("1. " + CATEGORY1);
        System.out.println("2. " + CATEGORY2);
        System.out.println("3. " + CATEGORY3);
    }

    private static void playQuiz(String category) {
        List<String[]> questions = questionsMap.get(category);
        Collections.shuffle(questions);

        int score = 0;
        Scanner scanner = new Scanner(System.in);

        System.out.println("Let's start the quiz!\n");

        for (String[] question : questions) {
            displayQuestion(question);

            int answerIndex = 0;
            boolean validInput = false;

            while (!validInput) {
                System.out.print("Enter your answer (1-4): ");
                String input = scanner.nextLine();

                try {
                    answerIndex = Integer.parseInt(input);
                    if (answerIndex >= 1 && answerIndex <= 4) {
                        validInput = true;
                    } else {
                        System.out.println("Invalid answer index. Please enter a number between 1 to 4.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number between 1 to 4.");
                }
            }

            if (answerIndex == 1) {
                System.out.println("Correct!\n");
                score++;
            } else {
                System.out.println("Incorrect! The correct answer was: " + question[1] + "\n");
                break;
            }
        }

        System.out.println("Quiz ended. Your score: " + score + "/" + questions.size());
    }

    private static void displayQuestion(String[] question) {
        System.out.println("Question: " + question[0]);
        System.out.println("1. " + question[1]);
        System.out.println("2. " + question[2]);
        System.out.println("3. " + question[3]);
        System.out.println("4. " + question[4]);
        System.out.print("Enter your answer (1-4): "); // Prompt for answer
    }
}
