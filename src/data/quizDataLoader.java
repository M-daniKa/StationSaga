package data;
import entities.quizQuestion;
import java.io.*;
import java.util.*;
public class quizDataLoader {
    public static List<quizQuestion> loadQuizQuestions() {
        List<quizQuestion> quiz = new ArrayList<>();

        try (InputStream is = quizDataLoader.class.getResourceAsStream("/final_Quiz,txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");

                String questionText = parts[1];
                Map<String, String> choices = new LinkedHashMap<>();
                choices.put(parts[2], parts[3]);
                choices.put(parts[4], parts[5]);
                choices.put(parts[6], parts[7]);
                choices.put(parts[8], parts[9]);

                String correctAnswer = parts[10];
                quiz.add(new quizQuestion(questionText, choices, correctAnswer));

            }
        } catch (Exception e) {
            System.out.println("Error loading quiz data: " + e.getMessage());
        }
        return quiz;
    }
}
