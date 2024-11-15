import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

class Question {
    private String questionText;
    private String[] options;
    private String correctAnswer;

    public Question(String questionText, String[] options, String correctAnswer) {
        this.questionText = questionText;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String[] getOptions() {
        return options;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }
}

public class QuizApplication extends JFrame {
    private List<Question> questions;
    private int currentQuestionIndex;
    private int score;
    private Timer timer;
    private int remainingTime;
    private TimerTask timerTask;
    private JLabel questionLabel, timerLabel, scoreLabel;
    private JRadioButton option1, option2, option3, option4;
    private ButtonGroup optionGroup;
    private JButton submitButton, nextButton;
    private List<String> userAnswers;  // To store the user's answers

    public QuizApplication() {
        setTitle("Quiz Application with Timer");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Set dark theme colors (soft, easy on the eyes)
        getContentPane().setBackground(new Color(45, 45, 45)); // Dark Gray

        // Initialize questions
        questions = new ArrayList<>();
        questions.add(new Question("What is the capital of France?", new String[]{"Berlin", "Madrid", "Paris", "Rome"}, "Paris"));
        questions.add(new Question("Which planet is known as the Red Planet?", new String[]{"Earth", "Mars", "Jupiter", "Saturn"}, "Mars"));
        questions.add(new Question("Who wrote 'Hamlet'?", new String[]{"Shakespeare", "Dickens", "Austen", "Hemingway"}, "Shakespeare"));
        questions.add(new Question("What is 2 + 2?", new String[]{"3", "4", "5", "6"}, "4"));

        currentQuestionIndex = 0;
        score = 0;
        userAnswers = new ArrayList<>();

        // Panel for question and options
        JPanel questionPanel = new JPanel();
        questionPanel.setLayout(new GridLayout(7, 1, 10, 10));
        questionPanel.setBackground(new Color(45, 45, 45)); // Dark Gray

        questionLabel = new JLabel();
        questionLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        questionLabel.setForeground(new Color(224, 224, 224)); // Light Gray
        questionPanel.add(questionLabel);

        option1 = new JRadioButton();
        option1.setForeground(new Color(224, 224, 224)); // Light Gray
        option1.setBackground(new Color(45, 45, 45)); // Dark Gray
        option2 = new JRadioButton();
        option2.setForeground(new Color(224, 224, 224)); // Light Gray
        option2.setBackground(new Color(45, 45, 45)); // Dark Gray
        option3 = new JRadioButton();
        option3.setForeground(new Color(224, 224, 224)); // Light Gray
        option3.setBackground(new Color(45, 45, 45)); // Dark Gray
        option4 = new JRadioButton();
        option4.setForeground(new Color(224, 224, 224)); // Light Gray
        option4.setBackground(new Color(45, 45, 45)); // Dark Gray

        optionGroup = new ButtonGroup();
        optionGroup.add(option1);
        optionGroup.add(option2);
        optionGroup.add(option3);
        optionGroup.add(option4);

        questionPanel.add(option1);
        questionPanel.add(option2);
        questionPanel.add(option3);
        questionPanel.add(option4);

        submitButton = new JButton("Submit Answer");
        submitButton.setBackground(new Color(76, 175, 80)); // Soft Green
        submitButton.setForeground(new Color(255, 255, 255)); // White
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkAnswer();
            }
        });
        questionPanel.add(submitButton);

        nextButton = new JButton("Next Question");
        nextButton.setBackground(new Color(33, 150, 243)); // Soft Blue
        nextButton.setForeground(new Color(255, 255, 255)); // White
        nextButton.setEnabled(false);
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showNextQuestion();
            }
        });
        questionPanel.add(nextButton);

        add(questionPanel, BorderLayout.CENTER);

        // Timer label and score display
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout());
        bottomPanel.setBackground(new Color(45, 45, 45)); // Dark Gray

        timerLabel = new JLabel("Time Left: 30");
        timerLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        timerLabel.setForeground(new Color(176, 176, 176)); // Light Gray
        bottomPanel.add(timerLabel);

        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        scoreLabel.setForeground(new Color(176, 176, 176)); // Light Gray
        bottomPanel.add(scoreLabel);

        add(bottomPanel, BorderLayout.SOUTH);

        // Start quiz
        startQuiz();
    }

    private void startQuiz() {
        showQuestion();
        startTimer();
    }

    private void showQuestion() {
        Question currentQuestion = questions.get(currentQuestionIndex);
        questionLabel.setText(currentQuestion.getQuestionText());
        String[] options = currentQuestion.getOptions();
        option1.setText(options[0]);
        option2.setText(options[1]);
        option3.setText(options[2]);
        option4.setText(options[3]);
        optionGroup.clearSelection();
        nextButton.setEnabled(false);
        submitButton.setEnabled(true);
    }

    private void startTimer() {
        remainingTime = 30; // 30 seconds for each question
        timerLabel.setText("Time Left: " + remainingTime);
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                remainingTime--;
                timerLabel.setText("Time Left: " + remainingTime);
                if (remainingTime <= 0) {
                    timer.cancel();
                    checkAnswer();
                }
            }
        };
        timer.scheduleAtFixedRate(timerTask, 1000, 1000);
    }

    private void checkAnswer() {
        timer.cancel(); // Stop the timer once the user answers
        String selectedAnswer = getSelectedAnswer();
        String correctAnswer = questions.get(currentQuestionIndex).getCorrectAnswer();
        if (selectedAnswer != null && selectedAnswer.equals(correctAnswer)) {
            score++;
        }
        userAnswers.add(selectedAnswer);
        scoreLabel.setText("Score: " + score);
        submitButton.setEnabled(false);
        nextButton.setEnabled(true);
    }

    private String getSelectedAnswer() {
        if (option1.isSelected()) {
            return option1.getText();
        } else if (option2.isSelected()) {
            return option2.getText();
        } else if (option3.isSelected()) {
            return option3.getText();
        } else if (option4.isSelected()) {
            return option4.getText();
        }
        return null;
    }

    private void showNextQuestion() {
        if (currentQuestionIndex < questions.size() - 1) {
            currentQuestionIndex++;
            showQuestion();
            startTimer();
        } else {
            showResult();
        }
    }

    private void showResult() {
        StringBuilder summary = new StringBuilder();
        int correctCount = 0;
        for (int i = 0; i < questions.size(); i++) {
            String userAnswer = userAnswers.get(i);
            String correctAnswer = questions.get(i).getCorrectAnswer();
            if (userAnswer.equals(correctAnswer)) {
                correctCount++;
                summary.append("Q" + (i + 1) + ": Correct\n");
            } else {
                summary.append("Q" + (i + 1) + ": Incorrect (Correct: " + correctAnswer + ")\n");
            }
        }
        JOptionPane.showMessageDialog(this, "Quiz Completed!\n\nYour Score: " + correctCount + "/" + questions.size() + "\n\n" + summary.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new QuizApplication().setVisible(true);
            }
        });
    }
}
