package in.collectivegood.dbsibycgf.calender;

public class CalendarItem {
    private long date;
    private long detail;

    public CalendarItem() {
    }

    public CalendarItem(long date, long detail) {
        this.date = date;
        this.detail = detail;
    }

    public long getDate() {
        return date;
    }

    public long getDetail() {
        return detail;
    }
}
