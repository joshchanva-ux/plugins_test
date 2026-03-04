package buttonstuff.app;
import buttonstuff.app.utilities.*;
import java.awt.*;
import javax.swing.*;



// Main class to demonstrate the UI with Plugin Support
public class URLButtonDemo extends JFrame {
    public URLButtonDemo() {
        setTitle("Button Plugin Demo - Built-in + Dynamic Plugins");
        setSize(800, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        // Add hard-coded and plug-in buttons 
        ButtonAdder.addButtons(this);
    }


    public static void main(String[] args) {
        // Create and show the UI
        SwingUtilities.invokeLater(() -> {
            URLButtonDemo demo = new URLButtonDemo();
            demo.setVisible(true);
        });
    }
}

