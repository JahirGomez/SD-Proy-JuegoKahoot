package User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class MainGUI extends JFrame {
    private static JLabel questionLabel;
    private static JButton[] optionButtons;
    private static Vector<String> currentQuestion;
    private static JLabel timerLabel;
    private static Timer timer;
    private static int timeLeft = 15; // Tiempo inicial en segundos
    private static int score;
    private static String userAnswer;

    public MainGUI(String username, Vector question) {
        setTitle("Kahoot Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        this.score = 0;
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

        timerLabel = new JLabel("Time left: " + timeLeft);
        add(timerLabel, BorderLayout.SOUTH);

        timer = new Timer(1000, new TimerListener());
        timer.start();

        if (currentQuestion.size()==6) {
            updateUI();
        }else{
            dispose();
        }        
       

        setVisible(true);
    }


    public int getScore() {
        return score; // Método para obtener el puntaje
    }

    public String getUserAnswer(){
        return userAnswer;
    }

    public static void updateUI() {
        questionLabel.setText((String) currentQuestion.get(0)); // Obtener la pregunta del Vector
        for (int i = 0; i < 4; i++) {
            optionButtons[i].setText((String) currentQuestion.get(i + 1)); // Obtener las opciones del Vector
        }
    }

    private static class OptionButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();
            String selectedOption = source.getText();
            userAnswer = selectedOption;

            // Desactivar todos los botones después de que se haya presionado uno
            for (JButton button : optionButtons) {
                button.setEnabled(false);
            }

            if(selectedOption.trim().equals(currentQuestion.get(5).trim())){
                System.out.println("Es Correcto");
               score++;
            }else{
                System.out.println("No es correcto");
            }
            
            // Handle selected option
            updateUI(); // For now, just load the next question
        }
    }

    private class TimerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            timeLeft = timeLeft -1; // Disminuir el tiempo restante
            timerLabel.setText("Time left: " + timeLeft);
            if (timeLeft < 0) {
                timer.stop(); // Detener el temporizador cuando llegue a cero
                restartTimer();
            }
        }
    }

    // Método para reiniciar el temporizador
    public void restartTimer() {
        timer.stop(); // Detener el temporizador actual
        timeLeft = 15; // Reiniciar el tiempo restante
        timer = new Timer(1000, new TimerListener());
        timerLabel.setText("Time left: " + timeLeft);
        if (timeLeft < 0) {
            timer.stop(); // Detener el temporizador cuando llegue a cero
            restartTimer();
        }
    }
    
}
