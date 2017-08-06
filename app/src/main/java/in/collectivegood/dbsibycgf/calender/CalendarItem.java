package in.collectivegood.dbsibycgf.calender;

public class CalendarItem {
    private long date;
    private String detail;
    private String title;

    public CalendarItem() {
    }

    public CalendarItem(long date, String title, String detail) {

        this.date = date;
        this.detail = detail;
        this.title = title;
    }

    public long getDate() {
        return date;
    }

    public String getDetail() {
        return detail;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return "CalendarItem{" +
                "date=" + date +
                ", detail='" + detail + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
