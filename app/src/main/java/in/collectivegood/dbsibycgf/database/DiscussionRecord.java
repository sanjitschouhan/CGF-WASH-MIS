package in.collectivegood.dbsibycgf.database;

public class DiscussionRecord {

    private long time;
    private String name;
    private String message;

    public DiscussionRecord() {
    }

    public DiscussionRecord(long time, String name, String message) {
        this.time = time;
        this.name = name;
        this.message = message;
    }

    public long getTime() {
        return time;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return name + ':' + message;
    }
}
