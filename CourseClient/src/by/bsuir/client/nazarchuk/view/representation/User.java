package by.bsuir.client.nazarchuk.view.representation;

public abstract class User {
    
    private int id;
    private String login;
    private String role;
    
    public User () {
        
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getRole() {
        return role;
    }
    
}
