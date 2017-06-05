package in.collectivegood.dbsibycgf.database;

public class CheckInRecord {

    private String uidOfCC;
    private String schoolCode;
    private long startTime;
    private long endTime;

    public CheckInRecord() {
    }

    public CheckInRecord(String uidOfCC, String schoolCode, long startTime, long endTime) {
        this.uidOfCC = uidOfCC;
        this.schoolCode = schoolCode;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getUidOfCC() {
        return uidOfCC;
    }

    public String getSchoolCode() {
        return schoolCode;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }
}
