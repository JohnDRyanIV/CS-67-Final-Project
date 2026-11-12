A Connect 4 Game

Requires JDK 21 and Maven 3.9 or newer. Import `pom.xml` as a Maven project
in your IDE. Java sources live in `src/main/java`; future FXML, CSS, and
other classpath resources belong in `src/main/resources`.

Build:

```sh
mvn clean package
```

Run the JavaFX shapes example:

```sh
mvn javafx:run
```

JavaFX controls and FXML dependencies are included using the
[OpenJFX Maven setup](https://openjfx.io/openjfx-docs/#maven).
The default entry point is `Launcher`, in `src/main/java/Launcher.java`.
It starts `javafx_example`, which opens a window with
a blue ellipse and "My Shapes" text. The Connect Four UI is still to be implemented.
Run the console board demo with `mvn javafx:run -Dapp.mainClass=Runner`.
The existing Javalin server remains in `Main`; run it with
`mvn javafx:run -Dapp.mainClass=Main` (port 7070).

If your IDE reports "JavaFX runtime components are missing", reload the Maven
project so its JavaFX dependencies are included, then run `Launcher.main()`.
The separate launcher avoids Java's special JavaFX startup check when running
an `Application` subclass directly from the classpath. You can also use
`mvn javafx:run`, which configures the JavaFX module path automatically.
If `mvn` is not found, install Maven or use your IDE's bundled Maven runner.

Example output:

```text
⚪⚪⚪⚪⚪⚪⚪
🔴⚫🔴⚫🔴⚫🔴
⚫🔴⚫🔴⚫🔴⚫
🔴⚫🔴⚫🔴⚫🔴
⚫🔴⚫🔴⚫🔴⚫
🔴⚫🔴⚫🔴⚫🔴
```
