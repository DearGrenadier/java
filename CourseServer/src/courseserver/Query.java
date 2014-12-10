package courseserver;

public enum Query {
    ENTЕRY_QUERY("SELECT * FROM users WHERE login=? AND password=?;"),
    GET_ID_FROM_LOGIN("SELECT users.id FROM users WHERE users.login=?"),
    PROJECTS_FOR_MANAGER("SELECT projectId FROM projectManagers"
            + " WHERE userId=?;"),
    GET_PROJECT("SELECT name, startDate, endDate, percent FROM projects "
            + "WHERE id=?;"),
    GET_MANAGERS_FROM_ID("SELECT login FROM users INNER JOIN projectManagers ON "
            + "users.id=projectManagers.userId WHERE projectId=?;"),
    GET_PERFORMERS("SELECT login FROM users INNER JOIN projectPerformers ON "
            + "users.id=projectPerformers.userId WHERE projectId=?;"),
    GET_MANAGERS_WITHOUT_USER("SELECT users.id, users.login FROM users WHERE "
            + "login!=? AND role='manager';"),
    GET_ALL_PERFORMERS("SELECT users.id, users.login, users.role FROM users"
            + " WHERE role!='manager'"),
    DELETE_PROJECT_BY_ID("DELETE FROM projects WHERE projects.id=?;"),
    DELETE_PROJECT_PERFORMER_BY_ID("DELETE FROM projectPerformers WHERE "
            + "projectPerformers.projectId=?;"),
    DELETE_PROJECT_MANAGER_BY_ID("DELETE FROM projectManagers WHERE "
            + "projectManagers.projectId=?;"),
    INSERT_PROJECT("INSERT INTO projects(name,startDate,endDate,percent) "
            + "VALUES(?,?,?,?)"),
    GET_LAST_PROJECT_ID("SELECT projects.id FROM projects WHERE id = (SELECT "
            + "MAX( id )FROM projects);"),
    INSERT_PROJECT_MANGER("INSERT INTO projectManagers(userId,projectId) "
            + "VALUES (?,?)"),
    INSERT_PROJECT_PERFORMER("INSERT INTO projectPerformers(userId,projectId) "
            + "VALUES (?,?)");
    private final String value;
    
    private Query(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
}