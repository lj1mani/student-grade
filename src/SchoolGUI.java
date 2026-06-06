import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class SchoolGUI extends JFrame {


    private DefaultListModel<SchoolClass> classListModel;
    private JList<SchoolClass> classList;
    private SchoolClass lastSelectedClass = null;
    private SchoolDAO dao;

    /* ***************************************************************** */
    /* ************ CLASS ********************************************** */

    public void showClassesWindow() {

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

        // MAIN PANEL
        JPanel mainPanel = new JPanel(new BorderLayout(0, 15));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        // TOP BUTTONS
        JButton addClassButton = new JButton("Add Class");
        addClassButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        addClassButton.setBackground(new Color(0, 122, 255));
        addClassButton.setForeground(Color.WHITE);
        addClassButton.setFocusPainted(false);
        addClassButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addClassButton.setMargin(new Insets(8, 16, 8, 16));

        JButton deleteClassButton = new JButton("Delete Class");
        deleteClassButton.setBackground(new Color(220, 53, 69));
        deleteClassButton.setForeground(Color.WHITE);
        deleteClassButton.setFocusPainted(false);
        deleteClassButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deleteClassButton.setMargin(new Insets(8, 16, 8, 16));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        topPanel.setBackground(Color.WHITE);
        topPanel.add(addClassButton);
        topPanel.add(deleteClassButton);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // LIST + MODEL
        DefaultListModel<SchoolClass> listModel = new DefaultListModel<>();
        JList<SchoolClass> classList = new JList<>(listModel);

        classList.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        classList.setSelectionBackground(new Color(230, 242, 255));
        classList.setSelectionForeground(new Color(0, 122, 255));
        classList.setFixedCellHeight(35);

        classList.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
            JLabel label = new JLabel(value.getClassName());
            label.setOpaque(true);

            if (isSelected) {
                label.setBackground(new Color(230, 242, 255));
                label.setForeground(new Color(0, 122, 255));
            } else {
                label.setBackground(Color.WHITE);
                label.setForeground(Color.BLACK);
            }

            return label;
        });

        JScrollPane scrollPane = new JScrollPane(classList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);


        // TITLE + CENTER PANEL
        JLabel titleLabel = new JLabel("All Classes");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JPanel centerPanel = new JPanel(new BorderLayout(5, 10));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.add(titleLabel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // LOAD DATA
        Runnable loadClasses = () -> {
            listModel.clear();
            for (SchoolClass sc : dao.getAllClasses()) {
                listModel.addElement(sc);
            }
        };

        loadClasses.run();

        // DOUBLE CLICK -> STUDENTS
        classList.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {

                    SchoolClass selected = classList.getSelectedValue();

                    if (selected != null) {
                        showStudentsWindow(selected);
                    }
                }
            }
        });

        // ADD CLASS
        addClassButton.addActionListener(e -> {
            showAddClassDialog();
            loadClasses.run();
        });

        // DELETE CLASS
        deleteClassButton.addActionListener(e -> {

            SchoolClass selected = classList.getSelectedValue();

            if (selected == null) {
                JOptionPane.showMessageDialog(frame, "Select a class to delete!");
                return;
            }

            dao.deleteClass(selected.getId());
            loadClasses.run();
        });

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

            dao = new SchoolDAO();

            if (dao.addClass(className)) {

                refreshClassesList();// refresh JList

                JOptionPane.showMessageDialog(
                        null,
                        "Class added successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

            } else {
                JOptionPane.showMessageDialog(null,
                        "Class already exists!",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE);

            }
        }
    }

    public void refreshClassesList() {

        dao = new SchoolDAO();

        classListModel.clear();

        for (SchoolClass schoolClass : dao.getAllClasses()) {
            classListModel.addElement(schoolClass);
        }
    }

    public void showDeleteClassDialog() {

        dao = new SchoolDAO();

        List<SchoolClass> classes = dao.getAllClasses();

        DefaultListModel<SchoolClass> model = new DefaultListModel<>();

        for (SchoolClass c : classes) {
            model.addElement(c);
        }

        JList<SchoolClass> list = new JList<>(model);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setPreferredSize(new Dimension(300, 200));

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.add(new JLabel("Select class to delete:"), BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        int result = JOptionPane.showConfirmDialog(
                null,
                panel,
                "Delete Class",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {

            SchoolClass selected = list.getSelectedValue();

            if (selected == null) {
                JOptionPane.showMessageDialog(null,
                        "Please select a class!",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean deleted = dao.deleteClass(selected.getId());

            if (deleted) {
                refreshClassesList();

                JOptionPane.showMessageDialog(null,
                        "Class deleted successfully!");
            } else {
                JOptionPane.showMessageDialog(null,
                        "Error deleting class!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /* ***************************************************************** */
    /* ************ STUDENST ******************************************* */

    public void showStudentsWindow(SchoolClass schoolClass) {

        SchoolDAO dao = new SchoolDAO();

        JFrame frame = new JFrame("Students - " + schoolClass.getClassName());
        frame.setSize(600, 500);
        frame.setLocationRelativeTo(null);
        
        // MODEL + LIST
        DefaultListModel<Student> model = new DefaultListModel<>();
        JList<Student> list = new JList<>(model);

        list.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        list.setSelectionBackground(new Color(230, 242, 255));
        list.setSelectionForeground(new Color(0, 122, 255));
        list.setFixedCellHeight(30);

        // LOAD DATA
        refreshStudentsList(model, schoolClass.getId(), dao);

        // SCROLL PANE
        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        // TITLE
        JLabel titleLabel = new JLabel("Students in " + schoolClass.getClassName());
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.add(titleLabel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        // BUTTONS
        JButton addStudentButton = new JButton("Add Student");
        addStudentButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        addStudentButton.setBackground(new Color(0, 122, 255));
        addStudentButton.setForeground(Color.WHITE);
        addStudentButton.setFocusPainted(false);
        addStudentButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addStudentButton.setMargin(new Insets(8, 16, 8, 16));


        JButton deleteStudentButton = new JButton("Delete Student");
        deleteStudentButton.setBackground(new Color(220, 53, 69));
        deleteStudentButton.setForeground(Color.WHITE);
        deleteStudentButton.setFocusPainted(false);
        deleteStudentButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deleteStudentButton.setMargin(new Insets(8, 16, 8, 16));
        deleteStudentButton.setEnabled(false);

        // enable delete only when selected
        list.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                deleteStudentButton.setEnabled(list.getSelectedValue() != null);
            }
        });

        // ACTIONS
        addStudentButton.addActionListener(e ->
                showAddStudentDialog(schoolClass, model)
        );

        deleteStudentButton.addActionListener(e -> {

            Student selected = list.getSelectedValue();

            if (selected == null) {
                JOptionPane.showMessageDialog(frame,
                        "Please select a student!",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                    frame,
                    "Delete student " + selected.getFirstName() + " " + selected.getLastName() + "?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {

                dao.deleteStudent(selected.getId());
                refreshStudentsList(model, schoolClass.getId(), dao);
            }
        });

        // TOP PANEL
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        top.setBackground(Color.WHITE);
        top.add(addStudentButton);
        top.add(deleteStudentButton);

        // MAIN LAYOUT
        JPanel mainPanel = new JPanel(new BorderLayout(0, 15));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        mainPanel.add(top, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private void refreshStudentsList(DefaultListModel<Student> model, int classId, SchoolDAO dao) {

        model.clear();

        for (Student s : dao.getStudentsByClass(classId)) {
            model.addElement(s);
        }
    }

    public void showAddStudentDialog(SchoolClass schoolClass, DefaultListModel<Student> model) {

        JTextField firstNameField = new JTextField(15);
        JTextField lastNameField = new JTextField(15);

        while (true) {

            JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));

            panel.add(new JLabel("First Name:"));
            panel.add(firstNameField);

            panel.add(new JLabel("Last Name:"));
            panel.add(lastNameField);

            int result = JOptionPane.showConfirmDialog(
                    null,
                    panel,
                    "Add Student",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            // User clicked Cancel or closed the dialog
            if (result != JOptionPane.OK_OPTION) {
                return;
            }

            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();

            // Validate First Name
            if (firstName.isEmpty()) {
                JOptionPane.showMessageDialog(
                        null,
                        "First Name cannot be empty.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE
                );
                continue;
            }

            // Validate Last Name
            if (lastName.isEmpty()) {
                JOptionPane.showMessageDialog(
                        null,
                        "Last Name cannot be empty.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE
                );
                continue;
            }

            // Optional: minimum length validation
            if (firstName.length() < 2) {
                JOptionPane.showMessageDialog(
                        null,
                        "First Name must contain at least 2 characters.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE
                );
                continue;
            }

            if (lastName.length() < 2) {
                JOptionPane.showMessageDialog(
                        null,
                        "Last Name must contain at least 2 characters.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE
                );
                continue;
            }

            try {
                dao = new SchoolDAO();

                dao.addStudent(
                        firstName,
                        lastName,
                        schoolClass.getId()
                );

                refreshStudentsList(model, schoolClass.getId(), dao);

                JOptionPane.showMessageDialog(
                        null,
                        "Student added successfully.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

                return; // close method after successful add

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        null,
                        "Failed to add student.\n\n" + ex.getMessage(),
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }



}