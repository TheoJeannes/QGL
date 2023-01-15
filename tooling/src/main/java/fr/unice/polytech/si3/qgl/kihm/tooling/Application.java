package fr.unice.polytech.si3.qgl.kihm.tooling;

import fr.unice.polytech.si3.qgl.kihm.logger.Printer;
import fr.unice.polytech.si3.qgl.kihm.tooling.GUI.MainApp;

import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.stream.Stream;

public class Application {

    public static void main(String[] args) {
        runGame(true);
    }

    @SuppressWarnings("all")
    private static void runGame(boolean gui) {
        Printer.get().setLogLevel(Level.ALL);
        Simulator s = new Simulator(fileToString("tooling/src/main/resources/Games/week9/initGame.json"));
        if (gui) new MainApp(s);
        else s.run();
    }

    /**
     * Take the path of a file, and return a string of the content
     *
     * @param path path of the file
     * @return content of the file
     */
    public static String fileToString(String path) {
        return fileToString(path, 0, Integer.MAX_VALUE);
    }

    @SuppressWarnings("all")
    private static String fileToString(String path, int firstLine, int lastLine) {
        StringBuilder sb = new StringBuilder();
        try (Stream<String> in = Files.lines(Paths.get(path))) {
            final int[] i = {1};
            in.forEach(e -> {
                if (i[0] >= firstLine && i[0] <= lastLine) sb.append(e);
                i[0]++;
            });
        } catch (NoSuchFileException e) {
            Printer.get().info("Le fichier n'est pas au bon endroit i.e. chemin depuis la racine du projet nécessaire");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
