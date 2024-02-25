package User;

import javax.swing.*;
import java.awt.*;

public class ResultGUI extends JFrame {
    private JLabel resultLabel;

    public ResultGUI(String username, int score) {
        setTitle("Result");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 150);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout()); // Usar BorderLayout
        resultLabel = new JLabel(username + "'s Score: " + score);
        resultLabel.setHorizontalAlignment(JLabel.CENTER); // Centrar el texto en el JLabel
        panel.add(resultLabel, BorderLayout.CENTER); // Agregar el JLabel al centro del panel

        add(panel);
        setVisible(true);
    }
}
