package in.collectivegood.dbsibycgf.database;

import java.util.ArrayList;

public class HEPSDataRecord {

    private String uidOfCC;
    private String schoolCode;
    private String schoolName;
    private String schoolAddress;
    private long maleTeachers;
    private long femaleTeachers;
    private ArrayList<Long> boys;
    private ArrayList<Long> girls;
    private long boysToilets;
    private long functioningBoysToilets;
    private long girlsToilets;
    private long functioningGirlsToilets;
    private long totalToilets;
    private long functioningTotalToilets;
    private long boysUrinals;
    private long functioningBoysUrinals;
    private long girlsUrinals;
    private long functioningGirlsUrinals;
    private long totalUrinals;
    private long functioningTotalUrinals;
    private long waterSource;
    private long noOfTaps;


    public HEPSDataRecord() {
        boys = new ArrayList<>();
        boys.add(0L);
        boys.add(0L);
        boys.add(0L);
        boys.add(0L);
        boys.add(0L);
        girls = new ArrayList<>();
        girls.add(0L);
        girls.add(0L);
        girls.add(0L);
        girls.add(0L);
        girls.add(0L);
    }

    public HEPSDataRecord(String uidOfCC, String schoolCode, String schoolName, String schoolAddress, long maleTeachers, long femaleTeachers, long[] boys, long[] girls, long boysToilets, long functioningBoysToilets, long girlsToilets, long functioningGirlsToilets, long totalToilets, long functioningTotalToilets, long boysUrinals, long functioningBoysUrinals, long girlsUrinals, long functioningGirlsUrinals, long totalUrinals, long functioningTotalUrinals, long waterSource, long noOfTaps) {
        this.uidOfCC = uidOfCC;
        this.schoolCode = schoolCode;
        this.schoolName = schoolName;
        this.schoolAddress = schoolAddress;
        this.maleTeachers = maleTeachers;
        this.femaleTeachers = femaleTeachers;
        this.boys = new ArrayList<>();
        for (long boy : boys) {
            this.boys.add(boy);
        }
        this.girls = new ArrayList<>();
        for (long girl : girls) {
            this.girls.add(girl);
        }
        this.boysToilets = boysToilets;
        this.functioningBoysToilets = functioningBoysToilets;
        this.girlsToilets = girlsToilets;
        this.functioningGirlsToilets = functioningGirlsToilets;
        this.totalToilets = totalToilets;
        this.functioningTotalToilets = functioningTotalToilets;
        this.boysUrinals = boysUrinals;
        this.functioningBoysUrinals = functioningBoysUrinals;
        this.girlsUrinals = girlsUrinals;
        this.functioningGirlsUrinals = functioningGirlsUrinals;
        this.totalUrinals = totalUrinals;
        this.functioningTotalUrinals = functioningTotalUrinals;
        this.waterSource = waterSource;
        this.noOfTaps = noOfTaps;
    }

    public long getTotalToilets() {
        return totalToilets;
    }

    public long getFunctioningTotalToilets() {
        return functioningTotalToilets;
    }

    public long getTotalUrinals() {
        return totalUrinals;
    }

    public long getFunctioningTotalUrinals() {
        return functioningTotalUrinals;
    }

    public String getUidOfCC() {
        return uidOfCC;
    }

    public String getSchoolCode() {
        return schoolCode;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public String getSchoolAddress() {
        return schoolAddress;
    }

    public long getMaleTeachers() {
        return maleTeachers;
    }

    public long getFemaleTeachers() {
        return femaleTeachers;
    }

    public long getBoys(int Class) {
        return boys.get(Class - 1);
    }

    public long getTotalClassStrength(int Class) {
        if (Class > 0 && Class <= 5)
            return boys.get(Class - 1) + girls.get(Class - 1);
        return 0;
    }

    public long getTotalBoys() {
        long total = 0;
        for (long boy : boys)
            total += boy;
        return total;
    }

    public long getTotalGirls() {
        long total = 0;
        for (long girl : girls)
            total += girl;
        return total;
    }

    public long getGirls(int Class) {
        return girls.get(Class - 1);
    }

    public long getBoysToilets() {
        return boysToilets;
    }

    public long getFunctioningBoysToilets() {
        return functioningBoysToilets;
    }

    public long getGirlsToilets() {
        return girlsToilets;
    }

    public long getFunctioningGirlsToilets() {
        return functioningGirlsToilets;
    }

    public long getBoysUrinals() {
        return boysUrinals;
    }

    public long getFunctioningBoysUrinals() {
        return functioningBoysUrinals;
    }

    public long getGirlsUrinals() {
        return girlsUrinals;
    }

    public long getFunctioningGirlsUrinals() {
        return functioningGirlsUrinals;
    }

    public long getWaterSource() {
        return waterSource;
    }

    public long getNoOfTaps() {
        return noOfTaps;
    }
}
