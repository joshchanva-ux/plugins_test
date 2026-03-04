package buttonstuff.buttons;

import buttonstuff.browser.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;


// Parent class: ShapeButton
public class ParentButton extends JButton implements ActionListener {
    protected String url;

    public ParentButton(String text, String url) {
        super(text);
        setPreferredSize(new Dimension(150, 100));
        addActionListener(this);
        this.url = url;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        BrowserOps.openBrowser(url);
    }

    protected void reNameButton(String name){
        this.setText(name);
    }

}


