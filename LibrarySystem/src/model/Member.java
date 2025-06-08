package model;

public class Member {
    private String id;
    private String name;
    private String major;
    private String email;

    public Member(String id, String name, String major, String email) {
        this.id = id;
        this.name = name;
        this.major = major;
        this.email = email;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getMajor() { return major; }
    public String getEmail() { return email; }

    public String toString() {
        return name + " (" + id + ")";
    }
}
