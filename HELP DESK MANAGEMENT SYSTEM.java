import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

class HelpdeskSystem {
    // Shared Data Storage
    private static HashMap<String, String> users = new HashMap<>();
    private static HashMap<String, Integer> tickets = new HashMap<>();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            createMenu();
        });
    }

    // **System Module**
    private static void createMenu() {
        JFrame frame = new JFrame("Helpdesk Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        JMenuItem loginItem = new JMenuItem("Login");
        JMenuItem registerItem = new JMenuItem("Register");
        JMenuItem exitItem = new JMenuItem("Exit");

        menu.add(loginItem);
        menu.add(registerItem);
        menu.addSeparator();
        menu.add(exitItem);
        menuBar.add(menu);

        loginItem.addActionListener(e -> showLoginForm(frame));
        registerItem.addActionListener(e -> showRegisterForm(frame));
        exitItem.addActionListener(e -> System.exit(0));

        frame.setJMenuBar(menuBar);
        frame.setVisible(true);
    }

    private static void showLoginForm(JFrame frame) {
        JFrame loginFrame = new JFrame("Login");
        loginFrame.setSize(300, 200);
        loginFrame.setLayout(new GridLayout(4, 2));

        JLabel nameLabel = new JLabel("Name: ");
        JTextField nameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password: ");
        JPasswordField passwordField = new JPasswordField();
        JLabel roleLabel = new JLabel("Role (user/admin): ");
        JTextField roleField = new JTextField();
        JButton loginButton = new JButton("Login");

        loginFrame.add(nameLabel);
        loginFrame.add(nameField);
        loginFrame.add(passwordLabel);
        loginFrame.add(passwordField);
        loginFrame.add(roleLabel);
        loginFrame.add(roleField);
        loginFrame.add(loginButton);

        loginButton.addActionListener(e -> {
            String name = nameField.getText();
            String password = new String(passwordField.getPassword());
            String role = roleField.getText();

            if (users.containsKey(name) && users.get(name).equals(password)) {
                if ("admin".equalsIgnoreCase(role)) {
                    AdminModule.showAdminOptions(frame);
                } else {
                    UserModule.showUserOptions(frame);
                }
                loginFrame.dispose();
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid login.");
            }
        });

        loginFrame.setVisible(true);
    }

    private static void showRegisterForm(JFrame frame) {
        JFrame registerFrame = new JFrame("Register");
        registerFrame.setSize(300, 200);
        registerFrame.setLayout(new GridLayout(3, 2));

        JLabel nameLabel = new JLabel("Name: ");
        JTextField nameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password: ");
        JPasswordField passwordField = new JPasswordField();
        JButton registerButton = new JButton("Register");

        registerFrame.add(nameLabel);
        registerFrame.add(nameField);
        registerFrame.add(passwordLabel);
        registerFrame.add(passwordField);
        registerFrame.add(registerButton);

        registerButton.addActionListener(e -> {
            String name = nameField.getText();
            String password = new String(passwordField.getPassword());

            if (!users.containsKey(name)) {
                users.put(name, password);
                JOptionPane.showMessageDialog(frame, "Registration successful.");
                registerFrame.dispose();
            } else {
                JOptionPane.showMessageDialog(frame, "User already exists.");
            }
        });

        registerFrame.setVisible(true);
    }

    // **User Module**
    static class UserModule {
        static void showUserOptions(JFrame frame) {
            JFrame userFrame = new JFrame("User Options");
            userFrame.setSize(300, 200);
            userFrame.setLayout(new FlowLayout());

            JButton addTicketButton = new JButton("Add Ticket");
            JButton viewTicketButton = new JButton("View Tickets");
            JButton exitButton = new JButton("Exit");

            userFrame.add(addTicketButton);
            userFrame.add(viewTicketButton);
            userFrame.add(exitButton);

            addTicketButton.addActionListener(e -> addTicket(frame));
            viewTicketButton.addActionListener(e -> viewTickets(frame));
            exitButton.addActionListener(e -> userFrame.dispose());

            userFrame.setVisible(true);
        }

        private static void addTicket(JFrame frame) {
            JFrame ticketFrame = new JFrame("Add Ticket");
            ticketFrame.setSize(300, 200);
            ticketFrame.setLayout(new GridLayout(2, 2));

            JLabel ticketLabel = new JLabel("Ticket Issue: ");
            JTextField ticketField = new JTextField();
            JButton submitButton = new JButton("Submit");

            ticketFrame.add(ticketLabel);
            ticketFrame.add(ticketField);
            ticketFrame.add(submitButton);

            submitButton.addActionListener(e -> {
                String issue = ticketField.getText();
                int priority = SystemModule.getPriority(issue);
                tickets.put(issue, priority);
                JOptionPane.showMessageDialog(frame, "Ticket added with priority: " + priority);
                ticketFrame.dispose();
            });

            ticketFrame.setVisible(true);
        }

        private static void viewTickets(JFrame frame) {
            JFrame ticketViewFrame = new JFrame("View Tickets");
            ticketViewFrame.setSize(300, 200);

            StringBuilder ticketDetails = new StringBuilder();
            tickets.forEach((issue, priority) -> ticketDetails.append("Issue: ").append(issue).append(", Priority: ").append(priority).append("\n"));

            JTextArea ticketArea = new JTextArea(ticketDetails.toString());
            ticketArea.setEditable(false);

            ticketViewFrame.add(new JScrollPane(ticketArea));
            ticketViewFrame.setVisible(true);
        }
    }

    // **Admin Module**
    static class AdminModule {
        static void showAdminOptions(JFrame frame) {
            JFrame adminFrame = new JFrame("Admin Options");
            adminFrame.setSize(300, 200);
            adminFrame.setLayout(new FlowLayout());

            JButton viewTicketsButton = new JButton("View All Tickets");
            JButton exitButton = new JButton("Exit");

            adminFrame.add(viewTicketsButton);
            adminFrame.add(exitButton);

            viewTicketsButton.addActionListener(e -> viewAllTickets(frame));
            exitButton.addActionListener(e -> adminFrame.dispose());

            adminFrame.setVisible(true);
        }

        private static void viewAllTickets(JFrame frame) {
            JFrame ticketViewFrame = new JFrame("View All Tickets");
            ticketViewFrame.setSize(300, 200);

            StringBuilder ticketDetails = new StringBuilder();
            tickets.forEach((issue, priority) -> ticketDetails.append("Issue: ").append(issue).append(", Priority: ").append(priority).append("\n"));

            JTextArea ticketArea = new JTextArea(ticketDetails.toString());
            ticketArea.setEditable(false);

            ticketViewFrame.add(new JScrollPane(ticketArea));
            ticketViewFrame.setVisible(true);
        }
    }

    // **System Utility Methods**
    static class SystemModule {
        static int getPriority(String issue) {
            switch (issue.toLowerCase()) {
                case "login issues":
                    return 1;
                case "system issues":
                    return 2;
                case "mail correction":
                    return 3;
                default:
                    return 4; // Default priority for other issues
            }
        }
    }
}
