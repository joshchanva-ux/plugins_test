package buttonstuff.app.utilities;

import buttonstuff.buttons.*;
import buttonstuff.loader.PluginLoader;
import java.util.List;
import javax.swing.*;
import java.awt.*;

public class ButtonAdder {
    
    public static void addButtons(JFrame frame) {
        addHardCodedButtons(frame);
        addPluginButtons(frame);
    }

    /**
     * Add hard-coded buttons to the frame.
    **/
    private static void addHardCodedButtons(JFrame frame) {
        // Built-in buttons - these demonstrate the original functionality
        ParentButton parentButton = new ParentButton("Parent","https://www.txst.edu/");
        BruceSpringsteenButton bruceSpringsteenButton = new BruceSpringsteenButton("Bruce","https://www.youtube.com/watch?v=tRx212PUa4g&list=PLiN-7mukU_REYdA_UUaejpJvf5nitRok9");
        TaylorSwiftButton taylorSwiftButton = new TaylorSwiftButton("Taylor Swift","https://www.youtube.com/watch?v=nfWlot6h_JM");
        DeepPurpleButton deepPurpleButton = new DeepPurpleButton("Deep Purple","https://www.youtube.com/watch?v=Rfirxs_NUcE");
        ChildMethodVisibilityButton childMethodVisibilityButton = new ChildMethodVisibilityButton("ChildMethodVisibility","https://discuss.codecademy.com/t/faq-inheritance-and-polymorphism-using-a-child-class-as-its-parent-class/447825/1");

        // Customize the ChildMethodVisibilityButton to demonstrate overloading
        childMethodVisibilityButton.reNameButton("Renamed Child Method Visibility Button");
        childMethodVisibilityButton.reNameButton("Renamed Child Method Visibility Button", Color.BLUE);
        childMethodVisibilityButton.setBackground(Color.MAGENTA);
        childMethodVisibilityButton.setOpaque(true);

        // Add built-in buttons to the frame
        frame.add(parentButton);
        frame.add(taylorSwiftButton);
        frame.add(bruceSpringsteenButton);
        frame.add(deepPurpleButton);
        frame.add(childMethodVisibilityButton);
    }

    /**
     * Load and add plugin buttons from the plugins directory
     */
    private static void addPluginButtons(JFrame frame) {
        // Determine the plugins directory (relative to project root)
        String pluginPath = "./buttonstuff/plugins";
        
        // Load plugins using the PluginLoader
        List<PluginLoader.PluginInfo> plugins = PluginLoader.loadPlugins(pluginPath);
        
        // Display plugin loading status
        System.out.println("\n========== PLUGIN LOADING REPORT ==========");
        System.out.println("Plugins directory: " + pluginPath);
        System.out.println("Total plugins found: " + plugins.size());
        
        int successCount = 0;
        int failureCount = 0;
        
        // Add successfully loaded plugins to the frame
        for (PluginLoader.PluginInfo info : plugins) {
            System.out.println(info);
            if (info.isLoaded()) {
                frame.add((JButton) info.getButton());
                successCount++;
            } else {
                failureCount++;
            }
        }
        
        System.out.println("Successfully loaded: " + successCount);
        System.out.println("Failed to load: " + failureCount);
        System.out.println("==========================================\n");
    }
}
