package in.collectivegood.dbsibycgf.database;

public class SchoolRecord {

    private String code;
    private String block;
    private String village;
    private String name;
    private String email;
    private String state;
    private String district;
    private String uidOfCC;

    public SchoolRecord() {
    }

    public SchoolRecord(String code, String block, String village, String name, String email,
                        String state, String district, String uidOfCC) {
        this.code = code;
        this.block = block;
        this.village = village;
        this.name = name;
        this.email = email;
        this.state = state;
        this.district = district;
        this.uidOfCC = uidOfCC;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }


    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
