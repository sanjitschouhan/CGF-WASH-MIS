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

    public static String header() {
        return "\"CC ID\"" +
                "," + "\"School Code\"" + "," + "\"School Name\"" + "," + "\"School Address\"" +
                "," + "\"Male Teachers\"" + "," + "\"Female Teachers\"" + "," + "\"Total Teachers\"" +
                "," + "\"Class 1 Boys\"" + "," + "\"Class 1 Girls\"" + "," + "\"Class 1 Total\"" +
                "," + "\"Class 2 Boys\"" + "," + "\"Class 2 Girls\"" + "," + "\"Class 2 Total\"" +
                "," + "\"Class 3 Boys\"" + "," + "\"Class 3 Girls\"" + "," + "\"Class 3 Total\"" +
                "," + "\"Class 4 Boys\"" + "," + "\"Class 4 Girls\"" + "," + "\"Class 4 Total\"" +
                "," + "\"Class 5 Boys\"" + "," + "\"Class 5 Girls\"" + "," + "\"Class 5 Total\"" +
                "," + "\"Total Boys\"" + "," + "\"Total Girls\"" + "," + "\"Total\"" +
                "," + "\"Boys Toilets\"" + "," + "\"Functioning Boys Toilets\"" +
                "," + "\"Girls Toilets\"" + "," + "\"Functioning Girls Toilets\"" +
                "," + "\"Total Toilets\"" + "," + "\"Functioning Toilets\"" +
                "," + "\"Boys Urinals\"" + "," + "\"Functioning Boys Urinals\"" +
                "," + "\"Girls Urinals\"" + "," + "\"Functioning Girls Urinals\"" +
                "," + "\"Total Urinals\"" + "," + "\"Functioning Urinals\"" +
                "," + "\"Water Source\"" +
                "," + "\"No Of Taps\"" + "\n";
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

    @Override
    public String toString() {
        return uidOfCC + "," +
                schoolCode + ",\"" + schoolName + "\",\"" + schoolAddress + "\"," +
                maleTeachers + "," + femaleTeachers + "," + (maleTeachers + femaleTeachers) +
                "," + getBoys(1) + "," + getGirls(1) + "," + (getTotalClassStrength(1)) +
                "," + getBoys(2) + "," + getGirls(2) + "," + (getTotalClassStrength(2)) +
                "," + getBoys(3) + "," + getGirls(3) + "," + (getTotalClassStrength(3)) +
                "," + getBoys(4) + "," + getGirls(4) + "," + (getTotalClassStrength(4)) +
                "," + getBoys(5) + "," + getGirls(5) + "," + (getTotalClassStrength(5)) +
                "," + getTotalBoys() + "," + getTotalGirls() + "," + (getTotalBoys() + getTotalGirls()) +
                "," + boysToilets + "," + functioningBoysToilets +
                "," + girlsToilets + "," + functioningGirlsToilets +
                "," + totalToilets + "," + functioningTotalToilets +
                "," + boysUrinals + "," + functioningBoysUrinals +
                "," + girlsUrinals + "," + functioningGirlsUrinals +
                "," + totalUrinals + "," + functioningTotalUrinals +
                ",\"" + getWaterSourceText() +
                "\"," + noOfTaps + "\n";
    }

    private String getWaterSourceText() {
        if (waterSource == 0)
            return "None";
        else if (waterSource == 1)
            return "Borewell";
        else if (waterSource == 2)
            return "Govt Supply";
        else
            return "Borewell and Govt Supply";
    }
}
