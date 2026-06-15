import java.util.*;

public class Question {
    public String questionText;
    public List<String> correctAnswers;
    public List<String> wrongAnswers;

    public Question(String q, List<String> correct, List<String> wrong) {
        this.questionText = q;
        this.correctAnswers = correct != null ? correct : new ArrayList<>();
        this.wrongAnswers = wrong != null ? wrong : new ArrayList<>();
    }

    public List<String> getAllAnswers() {
        List<String> all = new ArrayList<>();

        all.addAll(correctAnswers);
        all.addAll(wrongAnswers);

        Collections.shuffle(all);
        return all;
    }

    public boolean isCorrect(String ans) {
        return correctAnswers.contains(ans);
    }
}