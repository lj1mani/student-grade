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


    /* ******************************************* */
    /* ************ CLASS ********************* */

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
        deleteClassButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
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

        // TABLE MODEL
        String[] columns = {"ID","Class Name"};

        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // LOAD DATA
        for (SchoolClass sc : dao.getAllClasses()) {
            model.addRow(new Object[]{
                   sc.getId(),
                    sc.getClassName()
            });
        }

        // TABLE
        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        table.setRowHeight(35);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(230, 242, 255));
        table.setSelectionForeground(new Color(0, 122, 255));
        table.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(245, 245, 245));
        header.setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // DOUBLE CLICK -> OPEN STUDENTS
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {

                    int row = table.getSelectedRow();

                    if (row != -1) {
                        int id = (int) table.getValueAt(row, 0);
                        String name = (String) table.getValueAt(row, 1);

                        SchoolClass selected = new SchoolClass(id, name);
                        showStudentsWindow(selected);
                    }
                }
            }
        });

        // BUTTON ACTIONS
        addClassButton.addActionListener(e -> {
            showAddClassDialog();
            refreshClassesList();
        });

        deleteClassButton.addActionListener(e -> {
            int row = table.getSelectedRow();

            if (row == -1) {
                JOptionPane.showMessageDialog(frame, "Select a class to delete!");
                return;
            }

            int id = (int) table.getValueAt(row, 0);

            dao.deleteClass(id);

            model.removeRow(row);
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

            SchoolDAO dao = new SchoolDAO();

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

        SchoolDAO dao = new SchoolDAO();

        classListModel.clear();

        for (SchoolClass schoolClass : dao.getAllClasses()) {
            classListModel.addElement(schoolClass);
        }
    }

    public void showDeleteClassDialog() {

        SchoolDAO dao = new SchoolDAO();

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

    /* ******************************************* */
    /* ************ STUDENST ********************* */

    public void showStudentsWindow(SchoolClass schoolClass) {

        SchoolDAO dao = new SchoolDAO();

        JFrame frame = new JFrame("Students - " + schoolClass.getClassName());
        frame.setSize(600, 500);
        frame.setLocationRelativeTo(null);

        DefaultListModel<Student> model = new DefaultListModel<>();

        JList<Student> list = new JList<>(model);

        refreshStudentsList(model, schoolClass.getId(), dao);

        JButton addStudentButton = new JButton("Add Student");
        addStudentButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        addStudentButton.setBackground(new Color(0, 122, 255));
        addStudentButton.setForeground(Color.WHITE);
        addStudentButton.setFocusPainted(false);
        addStudentButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addStudentButton.setMargin(new Insets(8, 16, 8, 16));

        addStudentButton.addActionListener(e -> showAddStudentDialog(schoolClass, model));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(addStudentButton);

        frame.setLayout(new BorderLayout());
        frame.add(top, BorderLayout.NORTH);
        frame.add(new JScrollPane(list), BorderLayout.CENTER);

        frame.setVisible(true);
    }

    private void refreshStudentsList(DefaultListModel<Student> model, int classId, SchoolDAO dao) {

        model.clear();

        for (Student s : dao.getStudentsByClass(classId)) {
            model.addElement(s);
        }
    }

    public void showAddStudentDialog(SchoolClass schoolClass, DefaultListModel<Student> model) {

        JTextField firstName = new JTextField(10);
        JTextField lastName = new JTextField(10);

        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));

        panel.add(new JLabel("First Name:"));
        panel.add(firstName);

        panel.add(new JLabel("Last Name:"));
        panel.add(lastName);

        int result = JOptionPane.showConfirmDialog(
                null,
                panel,
                "Add Student",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (result == JOptionPane.OK_OPTION) {

            SchoolDAO dao = new SchoolDAO();

            dao.addStudent(
                    firstName.getText(),
                    lastName.getText(),
                    schoolClass.getId()
            );

            refreshStudentsList(model, schoolClass.getId(), dao);
        }
    }



}