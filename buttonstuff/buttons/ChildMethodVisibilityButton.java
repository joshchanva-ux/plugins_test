package buttonstuff.buttons;
import java.awt.*;
import java.awt.event.ActionEvent;


public class ChildMethodVisibilityButton extends ParentButton {
    
    public ChildMethodVisibilityButton(String text, String url) {
        super(text,url);
    }

    public int return_1_Method(){
        return(1);
    }

    public int return_2_Method(){
        return(2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.reNameButton("Change Text and Color", Color.RED);
    }

    @Override
    public void reNameButton(String newText) {
        this.setText(newText);
        this.setBackground(Color.CYAN);
        this.setOpaque(true);
    }

    // Overloaded method with different parameters
    public void reNameButton(String newText, Color newColor) {
        this.setText(newText);
        this.setBackground(newColor);
        this.setOpaque(true);
    }

}
