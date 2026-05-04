package diary.storage;

import diary.model.Entry;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class FileStorage {

    private static final String BASE = "data";

    // 📁 папка по дате
    private static Path getDir(LocalDate date) {
        return Paths.get(BASE, date.toString());
    }

    // 💾 SAVE ENTRY
    public static void saveEntry(Entry entry) {

        try {
            Path dir = getDir(entry.getDateTime().toLocalDate());
            Files.createDirectories(dir);

            String baseName = "entry_" + System.currentTimeMillis();

            // TXT
            File txt = dir.resolve(baseName + ".txt").toFile();

            try (BufferedWriter w = new BufferedWriter(new FileWriter(txt))) {
                w.write(entry.getTitle());
                w.newLine();
                w.write(entry.getDateTime().toString());
                w.newLine();
                w.write(entry.getContent());
            }

            // IMAGE
            if (entry.getImagePath() != null) {
                File img = new File(entry.getImagePath());

                if (img.exists()) {
                    Files.copy(
                            img.toPath(),
                            dir.resolve(baseName + ".png"),
                            StandardCopyOption.REPLACE_EXISTING
                    );
                }
            }

            System.out.println("SAVED → " + txt.getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 📖 READ ALL
    public static List<Entry> loadEntries() {

        List<Entry> list = new ArrayList<>();

        try {

            if (!Files.exists(Paths.get(BASE))) return list;

            Files.walk(Paths.get(BASE))
                    .filter(p -> p.toString().endsWith(".txt"))
                    .forEach(p -> {

                        try (BufferedReader r = new BufferedReader(new FileReader(p.toFile()))) {

                            String title = r.readLine();
                            String time = r.readLine();
                            String content = r.readLine();

                            LocalDateTime dt = LocalDateTime.parse(time);

                            String imgPath = p.toString().replace(".txt", ".png");
                            File img = new File(imgPath);

                            list.add(new Entry(
                                    title,
                                    content,
                                    dt,
                                    img.exists() ? imgPath : null
                            ));

                        } catch (Exception ignored) {
                        }
                    });

        } catch (IOException e) {
            e.printStackTrace();
        }

        list.sort(Comparator.comparing(Entry::getDateTime));
        return list;
    }

    public static String loadUserName() {
        try {
            Path path = Paths.get("data/user.txt");

            if (!Files.exists(path)) {
                return null;
            }

            return Files.readString(path);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 🗑 DELETE ENTRY
    public static void deleteEntry(Entry entry) {

        Path dir = getDir(entry.getDateTime().toLocalDate());

        File folder = dir.toFile();

        File[] files = folder.listFiles();
        if (files == null) return;

        for (File f : files) {

            if (f.getName().contains(entry.getTitle().replaceAll(" ", "_"))) {
                f.delete();
            }
        }
    }

    public static void saveUserName(String name) {
        try {
            Files.createDirectories(Paths.get("data"));
            Files.write(Paths.get("data/user.txt"), name.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}