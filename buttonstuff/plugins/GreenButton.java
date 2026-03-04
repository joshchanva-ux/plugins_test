package buttonstuff.plugins;

import buttonstuff.buttons.*;
import java.awt.*;

public class GreenButton extends ParentButton implements IButton {
    
    public GreenButton(String text, String url) {
        super(text, url);
        setBackground(Color.GREEN);
        setForeground(Color.WHITE);
        setOpaque(true);
        setFont(getFont().deriveFont(Font.BOLD));
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
