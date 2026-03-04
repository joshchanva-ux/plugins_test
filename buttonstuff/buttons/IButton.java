package buttonstuff.buttons;

import java.awt.event.ActionListener;
/**
 * IButton Interface - Plugin Contract
 * 
 * Button plugins that want to use a URL implement this interface.
 * The plugin is loaded by  PluginLoader.
 * 
 * This interface defines a contract that corresponding pluggable buttons must follow.
 * The IButton interface extends ActionListener, so any implementing class must also implement 
 * the actionPerformed method to define the button's behavior when clicked.
 * 
 * Example:
 * public class MyButton extends ParentButton {
 *     public MyButton(String text, String url) {
 *         super(text, url);
 *     }
 * }
 */
public interface IButton extends ActionListener {
    
    /**
     * Get the button's display text
     * @return the button text
     */
    String getText();
    
    /**
     * Get the URL string associated with this button
     * @return the URL string
     */
    String getURL();
    
    /**
     * Set the URL string for this button
     * @param url the URL string to set
     */
    void setURL(String url);
}
