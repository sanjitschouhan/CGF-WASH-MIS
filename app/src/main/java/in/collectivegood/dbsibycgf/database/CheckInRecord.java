package in.collectivegood.dbsibycgf.database;

public class CheckInRecord {

    private String uidOfCC;
    private String schoolCode;
    private long startTime;
    private long endTime;
    private float checkInDistance;
    private float checkOutDistance;

    public CheckInRecord(String uidOfCC, String schoolCode, long startTime, long endTime, float checkInDistance, float checkOutDistance) {
        this.uidOfCC = uidOfCC;
        this.schoolCode = schoolCode;
        this.startTime = startTime;
        this.endTime = endTime;
        this.checkInDistance = checkInDistance;
        this.checkOutDistance = checkOutDistance;
    }

    public CheckInRecord() {
    }

    public float getCheckInDistance() {
        return checkInDistance;
    }

    public float getCheckOutDistance() {
        return checkOutDistance;
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
