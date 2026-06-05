import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class SchoolGUI extends JFrame {

    public void showClassesWindow() {
        // 1. Set a modern Look and Feel (FlatLaf)
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }

        SchoolDAO dao = new SchoolDAO();

        JFrame frame = new JFrame("School Management");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        // Main layout with clean padding around the edges
        JPanel mainPanel = new JPanel(new BorderLayout(0, 15));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        // Modern styled button
        JButton addClassButton = new JButton("Add Class");
        addClassButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        addClassButton.setBackground(new Color(0, 122, 255)); // Modern Accent Blue
        addClassButton.setForeground(Color.WHITE);
        addClassButton.setFocusPainted(false);
        addClassButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        // Optional: Add padding inside the button
        addClassButton.setMargin(new Insets(8, 16, 8, 16));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        topPanel.setBackground(Color.WHITE);
        topPanel.add(addClassButton);
        addClassButton.addActionListener(e -> showAddClassDialog());

        mainPanel.add(topPanel, BorderLayout.NORTH);

        DefaultListModel<SchoolClass> listModel = new DefaultListModel<>();
        for (SchoolClass schoolClass : dao.getAllClasses()) {
            listModel.addElement(schoolClass);
        }

        // Modern styled JList
        JList<SchoolClass> classList = new JList<>(listModel);
        classList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        classList.setSelectionBackground(new Color(230, 242, 255)); // Subtle blue selection
        classList.setSelectionForeground(new Color(0, 122, 255));
        classList.setFixedCellHeight(40); // Gives items room to breathe

        // Add internal padding to list items via a custom renderer wrapper if needed,
        // but FlatLaf handles basic JList padding beautifully out of the box.

        JScrollPane scrollPane = new JScrollPane(classList);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(225, 225, 225), 1));

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    public void showAddClassDialog() {

        JTextField classNameField = new JTextField(15);

        // FlatLaf placeholder
        classNameField.putClientProperty(
                "JTextField.placeholderText",
                "Example: 1A"
        );

        JPanel panel = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Class Name:"), gbc);

        gbc.gridx = 1;
        panel.add(classNameField, gbc);

        int result = JOptionPane.showConfirmDialog(
                null,
                panel,
                "Add New Class",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {

            String className = classNameField.getText().trim();

            // Validation
            if (className.isEmpty()) {

                JOptionPane.showMessageDialog(
                        null,
                        "Class name cannot be empty.",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            SchoolDAO dao = new SchoolDAO();

            if (dao.addClass(className)) {

                showClassesWindow();// refresh JList

                JOptionPane.showMessageDialog(
                        null,
                        "Class added successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

            } else {

                JOptionPane.showMessageDialog(
                        null,
                        "Failed to add class.",
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }



}