

import User.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main extends JFrame {
    private JTextField usernameField;
    private JButton loginButton;
    private JButton hostButton;
    private static String userName;

    public Main() {
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 150);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());

        JLabel label = new JLabel("Enter your username:");
        panel.add(label, BorderLayout.NORTH);

        usernameField = new JTextField();
        panel.add(usernameField, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2)); // Panel para los botones
        loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userName = usernameField.getText();
                startGame(); // Iniciar el juego con el nombre de usuario ingresado
            }
        });
        buttonPanel.add(loginButton);

        hostButton = new JButton("Host");
        hostButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userName = "HOST"; // Establecer el nombre de usuario como "GAME"
                startGame(); // Iniciar el juego con el nombre de usuario "GAME"
            }
        });
        buttonPanel.add(hostButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel);
        setVisible(true);
    }

    private void startGame() {
        dispose(); // Cerrar la ventana de inicio de sesión
        User user = new User(userName);
        user.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}



