package buttonstuff.loader;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.awt.event.ActionListener;

import buttonstuff.buttons.IButton;
import buttonstuff.buttons.DButton;

/**
 * PluginLoader - Dynamically loads button plugins from JAR files
 * 
 * This class discovers and instantiates IButton and DButton implementations from JAR files
 * in the plugins directory. It uses reflection and custom class loading to
 * support runtime plugin discovery without requiring recompilation.
 * 
 * USAGE:
 * List<PluginInfo> plugins = PluginLoader.loadPlugins("./plugins");
 * for (PluginInfo info : plugins) {
 *     if (info.isLoaded()) {
 *         add(info.getButton());
 *     } else {
 *         System.err.println("Failed to load: " + info.getError());
 *     }
 * }
 */
public class PluginLoader {
    
    /**
     * Information about a plugin load attempt
     */
    public static class PluginInfo {
        private final String pluginName;
        private final ActionListener button;
        private final String error;
        
        public PluginInfo(String pluginName, ActionListener button) {
            this.pluginName = pluginName;
            this.button = button;
            this.error = null;
        }
        
        public PluginInfo(String pluginName, String error) {
            this.pluginName = pluginName;
            this.button = null;
            this.error = error;
        }
        
        public boolean isLoaded() {
            return button != null;
        }
        
        public String getPluginName() {
            return pluginName;
        }
        
        public ActionListener getButton() {
            return button;
        }
        
        public String getError() {
            return error;
        }
        
        @Override
        public String toString() {
            return isLoaded() 
                ? "✓ " + pluginName 
                : "✗ " + pluginName + " (" + error + ")";
        }
    }
    
    /**
     * Loads all button plugins from JAR files in the specified directory
     * 
     * @param pluginDirectory path to directory containing plugin JAR files
     * @return List of PluginInfo objects (successfully loaded and failed attempts)
     */
    public static List<PluginInfo> loadPlugins(String pluginDirectory) {
        List<PluginInfo> pluginInfos = new ArrayList<>();
        
        File pluginDir = new File(pluginDirectory);
        
        // Create plugins directory if it doesn't exist
        if (!pluginDir.exists()) {
            pluginDir.mkdirs();
            System.out.println("Created plugins directory at: " + pluginDir.getAbsolutePath());
            return pluginInfos; // Return empty list if newly created
        }
        
        if (!pluginDir.isDirectory()) {
            System.err.println("Plugin path is not a directory: " + pluginDirectory);
            return pluginInfos;
        }
        
        // Find all JAR files in the plugins directory.  This uses an anonymous FilenameFilter to only accept .jar files.
        // The filter is implemented as a lambda expression that checks if the file name ends with ".jar".
        // The lambda expression variable name is dynamically typed and generated
        File[] jarFiles = pluginDir.listFiles((dir, name) -> name.endsWith(".jar"));
        
        // If no JAR files are found, return empty list and print a message
        if (jarFiles == null || jarFiles.length == 0) {
            System.out.println("No plugin JARs found in: " + pluginDir.getAbsolutePath());
            return pluginInfos;
        }
        
        // Process each JAR file
        for (File jarFile : jarFiles) {
            loadFromJar(jarFile, pluginInfos);
        }
        
        return pluginInfos;
    }
    
    /**
     * Loads all IButton and DButton implementations from a single JAR file.
     * NOTE: This method will not handle interfaces other than IButton and DButton, and it will skip abstract classes.
     * This method uses a URLClassLoader to load classes from the JAR and checks each class
     * to see if it implements the IButton or DButton interface. It attempts to instantiate 
     * each valid plugin class using reflection, trying both (String, String) and (String) constructors. 
     * 
     * @param jarFile the JAR file to scan
     * @param pluginInfos list to add results to
     */
    private static void loadFromJar(File jarFile, List<PluginInfo> pluginInfos) {
        try (JarFile jar = new JarFile(jarFile);
             URLClassLoader loader = createPluginClassLoader(jarFile)) {
            
            // Iterate through entries in the JAR
            // entry is a JarEntry object representing a file or directory in the JAR.  
            // jar files contain multiple entries, which can be class files, directories, or other resources.
            // The entries() method returns an Enumeration of all the entries in the JAR, and asIterator() converts it 
            // to an Iterator for easier processing with forEachRemaining.
            jar.entries().asIterator().forEachRemaining(entry -> {
                if (isClassFile(entry)) {
                    String className = getClassName(entry);
                    try {
                        // Load the class
                        Class<?> clazz = loader.loadClass(className);
                        
                        // Check if it implements IButton or DButton (but skip interfaces and abstract classes)
                        boolean isIButton = IButton.class.isAssignableFrom(clazz) && clazz != IButton.class;
                        boolean isDButton = DButton.class.isAssignableFrom(clazz) && clazz != DButton.class;
                        
                        // Only attempt to instantiate if it implements one of either IButton or DButton interfaces 
                        // and is a concrete class (i.e., not an interface or abstract class)
                        if ((isIButton || isDButton)
                            && !clazz.isInterface()
                            && !java.lang.reflect.Modifier.isAbstract(clazz.getModifiers())) {
                            
                            // Try to instantiate with various constructors
                            ActionListener instance = null;
                            String errorMsg = null;
                            
                            // Try constructor with (String, String) first - in this application this might be the preferred constructor for plugins, 
                            // as it allows both button text and URL to be set and the hardcoded buttons in the app support this constructor.  
                            // We also want to support plugins that only have a (String) constructor for backward compatibility and flexibility.
                            try {
                                var constructor = clazz.getConstructor(String.class, String.class);
                                instance = (ActionListener) constructor.newInstance(
                                    clazz.getSimpleName(), 
                                    "https://example.com"
                                );
                            } catch (NoSuchMethodException e) {
                                // Try constructor with (String) instead
                                try {
                                    var constructor = clazz.getConstructor(String.class);
                                    instance = (ActionListener) constructor.newInstance(
                                        clazz.getSimpleName()
                                    );
                                } catch (NoSuchMethodException e2) {
                                    errorMsg = "Missing constructor (String text, String url) or (String text)";
                                } catch (Exception e2) {
                                    errorMsg = "Instantiation error with (String): " + e2.getMessage();
                                }
                            } catch (Exception e) {
                                errorMsg = "Instantiation error with (String, String): " + e.getMessage();
                            }
                            
                            if (instance != null) {
                                pluginInfos.add(new PluginInfo(
                                    clazz.getSimpleName() + " (" + jarFile.getName() + ")",
                                    instance
                                ));
                                System.out.println("Loaded plugin: " + clazz.getName());
                            } else if (errorMsg != null) {
                                pluginInfos.add(new PluginInfo(
                                    className + " (" + jarFile.getName() + ")",
                                    errorMsg
                                ));
                            }
                        }
                    } catch (ClassNotFoundException e) {
                        // Class not found, skip silently
                    } catch (Exception e) {
                        System.err.println("Error loading class " + className + ": " + e.getMessage());
                    }
                }
            });
            
        } catch (Exception e) {
            pluginInfos.add(new PluginInfo(
                jarFile.getName(),
                "JAR loading error: " + e.getMessage()
            ));
            System.err.println("Error loading JAR " + jarFile.getName() + ": " + e.getMessage());
        }
    }
    
    /**
     * Check if a JAR entry is a class file
     */
    private static boolean isClassFile(JarEntry entry) {
        return entry.getName().endsWith(".class") && !entry.isDirectory();
    }
    
    /**
     * Create a URLClassLoader for a plugin that includes both the plugin JAR
     * and the main application's bin directory
     */
    private static URLClassLoader createPluginClassLoader(File jarFile) throws Exception {
        List<URL> urls = new ArrayList<>();
        
        // Add the plugin JAR itself
        urls.add(jarFile.toURI().toURL());
        
        // Add the bin directory to allow access to ParentButton and other app classes
        File binDir = new File("bin");
        if (binDir.exists() && binDir.isDirectory()) {
            urls.add(binDir.toURI().toURL());
        }
        
        return new URLClassLoader(
            urls.toArray(new URL[0]),
            Thread.currentThread().getContextClassLoader()
        );
    }
    
    /**
     * Convert JAR entry path to fully qualified class name
     * e.g., "com/example/MyClass.class" -> "com.example.MyClass"
     */
    private static String getClassName(JarEntry entry) {
        String name = entry.getName();
        name = name.substring(0, name.length() - 6); // Remove .class
        return name.replace('/', '.');
    }
}
