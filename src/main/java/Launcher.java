import javafx.application.Application;

/** Entry point for IDE launches with the Maven dependencies on the classpath. */
public class Launcher {
    public static void main(String[] args) {
        // If you want to start with custom board size args, it requires Maven 3.9+ on PATH and JDK 21.
        // Set JAVA_HOME to the JDK 21 installation directory.
        // Verify with: mvn -v (should report Maven 3.9+ and Java 21).
        // From the project root, launch with custom board dimensions:
        // mvn javafx:run "-Djavafx.args=x y"
        // Replace x and y with the number of rows and columns, each at least 4.
        Application.launch(ConnectFourGUI.class, args);
    }
}
