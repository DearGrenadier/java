package by.bsuir.client.nazarchuk.view.representation;

public class Manager extends User {
    
    public Manager(int id, String login) {
        this.setId(id);
        this.setLogin(login);
        this.setRole("manager");
    }
    
}
