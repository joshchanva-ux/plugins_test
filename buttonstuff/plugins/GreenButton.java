package buttonstuff.plugins;

import buttonstuff.button_interfaces.IButton;
import buttonstuff.buttons.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class GreenButton extends ParentButton implements IButton {
    
    public GreenButton(String text, String url) {
        super(text,"https://claude.ai/");
        setBackground(Color.GREEN);
        setOpaque(true);
        setFont(getFont().deriveFont(Font.BOLD));
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        setText("Claude is the best!");
        super.actionPerformed(e);
    }

    @Override
    public String getURL() {
        return url;
    }
    
    @Override
    public void setURL(String newUrl) {
        this.url = newUrl;
    }
}
