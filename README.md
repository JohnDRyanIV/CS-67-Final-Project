A Connect 4 Game

Requires JDK 21 and Maven 3.9 or newer. Import `pom.xml` as a Maven project
in your IDE. Java sources live in `src/main/java`; future FXML, CSS, and
other classpath resources belong in `src/main/resources`.

Build:

```sh
mvn clean package
```

Run the JavaFX Connect Four board:

```sh
mvn javafx:run
```

JavaFX controls and FXML dependencies are included using the
[OpenJFX Maven setup](https://openjfx.io/openjfx-docs/#maven).
The default entry point is `Launcher`, in `src/main/java/Launcher.java`.
It starts `ConnectFourGUI`, which displays a 6-by-7 board. Hover over a column
to preview the current player's token above it, then click to drop it into the
lowest empty cell. The preview updates to the next player's color after a move. Turns
alternate between red and black; full columns do not accept tokens. Win detection
is not implemented yet. Use **New board** to clear the board.

The original shapes example is still available with
`mvn javafx:run -Dapp.mainClass=javafx_example`.
Run the console board demo with `mvn javafx:run -Dapp.mainClass=Runner`.
`Main` contains a commented-out Javalin server stub; it does not currently start
a web server.

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
