package by.bsuir.server.nazarchuk.model;

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
    GET_ALL_PERFORMERS_WITHOUT_ADMIN("SELECT users.id, users.login, users.role FROM users"
            + " WHERE role!='manager' AND role!='admin'"),
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
            + "VALUES (?,?)"),
    UPDATE_PROJECT("UPDATE projects SET name=?, startDate=?, endDate=?, "
            + "percent=? WHERE id=?;"),
    DROP_ALL_PROJECT_MANAGERS("DELETE FROM projectManagers WHERE "
            + "projectManagers.projectId=?;"),
    DROP_ALL_PROJECT_PERFORMERS("DELETE FROM projectPerformers WHERE "
            + "projectPerformers.projectId=?;"),
    SELECT_BY_NAME("SELECT projects.id FROM projects WHERE projects.name=?;"),
    SELECT_BY_START_DATE("SELECT projects.id FROM projects WHERE "
            + "projects.startDate=?;"),
    SELECT_BY_END_DATE("SELECT projects.id FROM projects WHERE "
            + "projects.endDate=?;"),
    SELECT_BY_MANAGER("SELECT projectManagers.projectId FROM"
            + " projectManagers WHERE projectManagers.userId=?;"),
    SELECT_BY_PERFORMER("SELECT projectPerformers.projectId FROM"
            + " projectPerformers WHERE projectPerformers.userId=?;"),
    SELECT_ALL_USERS_WITHOUT_ADMIN("SELECT * FROM users WHERE "
            + "users.role!='admin'"),
    DELETE_USER_BY_ID("DELETE FROM users WHERE users.id=?;"),
    INSERT_INTO_USERS("INSERT INTO users(login,password,role) VALUES (?,?,?)");
    
    
    private final String value;
    
    private Query(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
}