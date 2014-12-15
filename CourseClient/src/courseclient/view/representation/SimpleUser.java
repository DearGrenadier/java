
package courseclient;

public class Performer {
    private int id;
    private String name;
    private String role;

    public Performer() {
    }

    public Performer(int id, String name, String role) {
        this.id = id;
        this.name = name;
        this.role = role;
    }
     
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }
    
}
