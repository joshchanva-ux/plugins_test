package buttonstuff.buttons;

import java.awt.event.ActionListener;
import javax.swing.*;
/**
 * DButton Interface - Plugin Contract
 * Button plugins that want to create a new JFrame with custom content when clicked implement this interface.
 * This interface defines a contract that pluggable buttons must follow to create and display a new JFrame with custom content when clicked.
 */

public interface DButton extends ActionListener{

    /**
     * Create and return a JFrame that will be displayed when the button is clicked.
     * @return the JFrame to display when the button is clicked
     */
    JFrame createFrame();

    /**
     * Create and return a JPanel that will be added to the JFrame created by createFrame().
     * @return the JPanel to add to the JFrame
     */
    JPanel createPanel();
  
}
