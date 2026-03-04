package buttonstuff.browser;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


public class BrowserOps {

    public static void openBrowser(String url) {

        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI(url));
            } catch (IOException | URISyntaxException ex) {
                ex.printStackTrace(System.out);
            }
        } else {
            System.out.println("Desktop is not supported on this platform.");
        }
    }
}
