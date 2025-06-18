package model;

public class Member {
    private String id;
    private String name;
    private String major;
    private String email;
    private String password;

    public Member(String id, String name, String major, String email, String password) {
        this.id = id;
        this.name = name;
        this.major = major;
        this.email = email;
        this.password = password;
    }

    // ── Getters ───────────────────────────────────────────────────────
    public String getId()    { return id;    }
    public String getName()  { return name;  }
    public String getMajor() { return major; }
    public String getEmail() { return email; }
    public String getPassword(){return password;}

    // ── CSV ───────────────────────────────────────────────────────────
    public String toCSV() {
        return String.join(",", id, name, major, email, password);
    }
}
