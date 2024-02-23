package User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class MainGUI extends JFrame {
    private static JLabel questionLabel;
    private static JButton[] optionButtons;
    private static Vector currentQuestion;

    public MainGUI(String username, Vector question) {
        setTitle("Kahoot Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        currentQuestion = question;

        JPanel welcomePanel = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Welcome, " + username + "!");
        welcomePanel.add(welcomeLabel, BorderLayout.CENTER);
        add(welcomePanel, BorderLayout.NORTH);

        JPanel questionPanel = new JPanel(new GridLayout(5, 1));
        questionLabel = new JLabel();
        questionPanel.add(questionLabel);
        optionButtons = new JButton[4];
        for (int i = 0; i < 4; i++) {
            optionButtons[i] = new JButton();
            optionButtons[i].addActionListener(new OptionButtonListener());
            questionPanel.add(optionButtons[i]);
        }
        add(questionPanel, BorderLayout.CENTER);

       updateUI();

        setVisible(true);
    }


    public static  void updateUI() {
        questionLabel.setText((String) currentQuestion.get(0)); // Obtener la pregunta del Vector
        for (int i = 0; i < 4; i++) {
            optionButtons[i].setText((String) currentQuestion.get(i + 1)); // Obtener las opciones del Vector
        }
    }

    private class OptionButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();
            String selectedOption = source.getText();
            // Handle selected option
            updateUI();; // For now, just load the next question
        }
    }
}
