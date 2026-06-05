import javax.swing.*;
import com.formdev.flatlaf.FlatLightLaf;
import java.awt.*;

public class main {
    public static void main(String[] args) {
        
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            System.err.println("Failed to initialize FlatLaf.");
        }

        DatabaseManager db = new DatabaseManager();
        db.initDatabase();

        SchoolGUI schoolGUI = new SchoolGUI();


            schoolGUI.showClassesWindow();



    }
}
