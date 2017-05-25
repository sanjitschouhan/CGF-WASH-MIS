package in.collectivegood.dbsibycgf.common;

public class SchoolRecord {
    private String emailOfCC;
    private String uidOfCC;
    private String nameOfCC;
    private String nameOfBlock;
    private long schoolCode;
    private String nameOfSchool;
    private String state;
    private String district;
    private String nameOfZonalCoordinator;
    private String emailOfSchool;

    public SchoolRecord(String emailOfCC, String uidOfCC, String nameOfCC, String nameOfBlock,
                        long schoolCode, String nameOfSchool, String state, String district,
                        String nameOfZonalCoordinator, String emailOfSchool) {
        this.emailOfCC = emailOfCC;
        this.uidOfCC = uidOfCC;
        this.nameOfCC = nameOfCC;
        this.nameOfBlock = nameOfBlock;
        this.schoolCode = schoolCode;
        this.nameOfSchool = nameOfSchool;
        this.state = state;
        this.district = district;
        this.nameOfZonalCoordinator = nameOfZonalCoordinator;
        this.emailOfSchool = emailOfSchool;
    }

    public String getEmailOfCC() {
        return emailOfCC;
    }

    public void setEmailOfCC(String emailOfCC) {
        this.emailOfCC = emailOfCC;
    }

    public String getNameOfCC() {
        return nameOfCC;
    }

    public void setNameOfCC(String nameOfCC) {
        this.nameOfCC = nameOfCC;
    }

    public String getNameOfBlock() {
        return nameOfBlock;
    }

    public void setNameOfBlock(String nameOfBlock) {
        this.nameOfBlock = nameOfBlock;
    }

    public long getSchoolCode() {
        return schoolCode;
    }

    public void setSchoolCode(long schoolCode) {
        this.schoolCode = schoolCode;
    }

    public String getNameOfSchool() {
        return nameOfSchool;
    }

    public void setNameOfSchool(String nameOfSchool) {
        this.nameOfSchool = nameOfSchool;
    }

    public String getUidOfCC() {
        return uidOfCC;
    }

    public void setUidOfCC(String uidOfCC) {
        this.uidOfCC = uidOfCC;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getNameOfZonalCoordinator() {
        return nameOfZonalCoordinator;
    }

    public void setNameOfZonalCoordinator(String nameOfZonalCoordinator) {
        this.nameOfZonalCoordinator = nameOfZonalCoordinator;
    }

    public String getEmailOfSchool() {
        return emailOfSchool;
    }

    public void setEmailOfSchool(String emailOfSchool) {
        this.emailOfSchool = emailOfSchool;
    }
}
