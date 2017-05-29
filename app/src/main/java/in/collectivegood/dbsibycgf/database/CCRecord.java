package in.collectivegood.dbsibycgf.database;

public class CCRecord {
    private String uid;
    private String name;
    private String phone;
    private String email;
    private String projectCoordinator;

    public CCRecord() {
    }

    public CCRecord(String uid, String name, String phone, String email, String projectCoordinator) {
        this.uid = uid;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.projectCoordinator = projectCoordinator;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProjectCoordinator() {
        return projectCoordinator;
    }

    public void setProjectCoordinator(String projectCoordinator) {
        this.projectCoordinator = projectCoordinator;
    }
}
