package buttonstuff.plugins;

import buttonstuff.button_interfaces.DButton;
import buttonstuff.buttons.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

/**
 * STUDENT TEMPLATE: Copy this file, rename it, and customize it to create your own button plugin!
 * 
 * Steps to create a plugin:
 * 1. Copy this file and rename it (e.g., "MyCustomButton.java")
 * 2. Change the class name to match the filename
 * 3. Customize the button behavior by overriding methods
 * 4. Build into a JAR file and place in the plugins/ folder
 * 5. Run URLButtonDemo to see your button loaded!
 * 
 * IMPORTANT: Keep the constructor signature: public MyButton(String text, String url)
 */
public class DrawButton extends ParentButton implements DButton {
    
    // Constructor - REQUIRED signature for plugin loading
    public DrawButton(String text, String url) {
        super(text, url);
        // You can customize the button here
        setForeground(Color.BLUE);
        setFont(getFont().deriveFont(Font.BOLD));
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        JFrame frame = createFrame();
        JPanel panel = createPanel();
        frame.add(panel);
        frame.setVisible(true);
    }

    @Override
    public JFrame createFrame() {
        JFrame frame = new JFrame("Draw Panel");
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        return frame;
    }

    @Override
    public JPanel createPanel() {
        ArrayList<Point> dots = new ArrayList<>();

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.BLACK);
                for (Point dot : dots) {
                    g.fillOval(dot.x - 5, dot.y - 5, 10, 10);
                }
            }
        };

        panel.setBackground(Color.WHITE);
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dots.add(e.getPoint());
                panel.repaint();
            }
        });

        return panel;
    }
}
