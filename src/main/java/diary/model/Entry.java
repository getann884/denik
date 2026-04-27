package diary.model;

import java.time.LocalDateTime;

public class Entry {

    private String title;
    private String content;
    private LocalDateTime dateTime;
    private String imagePath;

    public Entry(String title, String content, LocalDateTime dateTime, String imagePath) {

        this.title = title;
        this.content = content;

        // 💥 защита от null
        this.dateTime = (dateTime == null)
                ? LocalDateTime.now()
                : dateTime;

        this.imagePath = imagePath;
    }

    public String getTitle() { return title; }
    public String getContent() { return content; }
    public LocalDateTime getDateTime() { return dateTime; }
    public String getImagePath() { return imagePath; }
}