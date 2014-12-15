package by.bsuir.server.nazarchuk.model;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Router {
    private DataBaseHandler dataBaseHandler;
    private PreparedStatement ps;
    private BufferedReader in;
    private PrintWriter out;
    
    public Router() {
        
    }
    
    public Router(BufferedReader in, PrintWriter out) {
        this.in = in;
        this.out = out;
        dataBaseHandler = new DataBaseHandler();
    }
    
    public void entry(JSONObject jsonObj) {
        ps = dataBaseHandler.getPreparedStatement(
                Query.ENTЕRY_QUERY.getValue());
        JSONObject answer = new JSONObject();
        if (dataBaseHandler.entry(ps, answer,
                jsonObj.get("login").toString(),
                jsonObj.get("pass").toString())) {
            answer.put("find", true);
            out.println(answer);
            out.flush();
        } else {
            answer.put("find", false);
            out.println(answer);
            out.flush();
        }
        dataBaseHandler.closeStatement(ps);
    }

    public void entryView(JSONObject jsonObj) {
        String login = jsonObj.get("login").toString();
        String role = jsonObj.get("role").toString();
        int id;
        ps = dataBaseHandler.getPreparedStatement(
                Query.GET_ID_FROM_LOGIN.getValue());
        id = dataBaseHandler.getIdFromLogin(ps, login);

        dataBaseHandler.closeStatement(ps);
        if (role.equals("manager")) {
            ps = dataBaseHandler.getPreparedStatement(
                    Query.PROJECTS_FOR_MANAGER.getValue());
            List<Integer> projectsId = new ArrayList<>();
            dataBaseHandler.getProjectsForManager(ps, projectsId, id);
            dataBaseHandler.closeStatement(ps);
            JSONArray projectsArray = new JSONArray();
            for (Integer i : projectsId) {
                JSONArray managers = new JSONArray();
                JSONArray performers = new JSONArray();
                JSONObject project = new JSONObject();

                ps = dataBaseHandler.getPreparedStatement(
                        Query.GET_MANAGERS_FROM_ID.getValue());
                dataBaseHandler.getManagers(ps, i, managers);
                dataBaseHandler.closeStatement(ps);

                ps = dataBaseHandler.getPreparedStatement(
                        Query.GET_PERFORMERS.getValue());
                dataBaseHandler.getPerformers(ps, i, performers);
                dataBaseHandler.closeStatement(ps);

                ps = dataBaseHandler.getPreparedStatement(
                        Query.GET_PROJECT.getValue());
                dataBaseHandler.getProject(ps, i, project);
                dataBaseHandler.closeStatement(ps);
                project.put("id", i);
                project.put("managers", managers);
                project.put("performers", performers);
                projectsArray.add(project);
            }
            out.println(projectsArray);
            out.flush();
        } else {

        }
    }

    public void getManagers(String login) {
        JSONArray managers = new JSONArray();
        ps = dataBaseHandler.getPreparedStatement(
                Query.GET_MANAGERS_WITHOUT_USER.getValue());
        dataBaseHandler.getMangersWithoutUser(ps, login, managers);
        dataBaseHandler.closeStatement(ps);
        out.println(managers);
        out.flush();
    }

    public void getPerformers() {
        JSONArray performers = new JSONArray();
        ps = dataBaseHandler.getPreparedStatement(
                Query.GET_ALL_PERFORMERS_WITHOUT_ADMIN.getValue());
        dataBaseHandler.getAllPerformers(ps, performers);
        dataBaseHandler.closeStatement(ps);
        out.println(performers);
        out.flush();
    }

    public void deleteRow(int id) {
        ps = dataBaseHandler.getPreparedStatement(
                Query.DELETE_PROJECT_BY_ID.getValue());
        dataBaseHandler.deleteProjectById(ps, id);
        dataBaseHandler.closeStatement(ps);
        ps = dataBaseHandler.getPreparedStatement(
                Query.DELETE_PROJECT_MANAGER_BY_ID.getValue());
        dataBaseHandler.deleteProjectManagerById(ps, id);
        dataBaseHandler.closeStatement(ps);
        ps = dataBaseHandler.getPreparedStatement(
                Query.DELETE_PROJECT_PERFORMER_BY_ID.getValue());
        dataBaseHandler.deleteProjectPerformerById(ps, id);
        dataBaseHandler.closeStatement(ps);
    }

    public void addProject(JSONObject jsonObj) {
        JSONArray managers = (JSONArray) jsonObj.get("managers");
        JSONArray performers = (JSONArray) jsonObj.get("performers");
        String name = jsonObj.get("name").toString();
        String startDate = jsonObj.get("startDate").toString();
        String endDate = jsonObj.get("endDate").toString();
        String user = jsonObj.get("user").toString();
        int percent = Integer.parseInt(jsonObj.get("percent").toString());
        ps = dataBaseHandler.getPreparedStatement(
                Query.INSERT_PROJECT.getValue());
        dataBaseHandler.addProject(ps, name, startDate, endDate,
                percent);
        dataBaseHandler.closeStatement(ps);
        ps = dataBaseHandler.getPreparedStatement(
                Query.GET_LAST_PROJECT_ID.getValue());
        int projectId = dataBaseHandler.getLastProjectId(ps);
        dataBaseHandler.closeStatement(ps);
        ps = dataBaseHandler.getPreparedStatement(
                Query.GET_ID_FROM_LOGIN.getValue());
        int userId = dataBaseHandler.getIdFromLogin(ps, user);
        dataBaseHandler.closeStatement(ps);
        managers.add(userId);

        for (Object manager : managers) {
            int managerId = Integer.parseInt(manager.toString());
            ps = dataBaseHandler.getPreparedStatement(
                    Query.INSERT_PROJECT_MANGER.getValue());
            dataBaseHandler.insertPorojectManager(ps, managerId, projectId);
            dataBaseHandler.closeStatement(ps);
        }

        for (Object performer : performers) {
            int performerId = Integer.parseInt(performer.toString());
            ps = dataBaseHandler.getPreparedStatement(
                    Query.INSERT_PROJECT_PERFORMER.getValue());
            dataBaseHandler.insertPorojectPerformer(ps, performerId, projectId);
            dataBaseHandler.closeStatement(ps);
        }
    }

    public void initProject(JSONObject jsonObj) {
        JSONArray managers = new JSONArray();
        JSONArray performers = new JSONArray();
        JSONObject project = new JSONObject();

        ps = dataBaseHandler.getPreparedStatement(
                Query.GET_MANAGERS_FROM_ID.getValue());
        dataBaseHandler.getManagers(ps,
                Integer.parseInt(jsonObj.get("projectId").toString()), managers);
        dataBaseHandler.closeStatement(ps);

        ps = dataBaseHandler.getPreparedStatement(
                Query.GET_PERFORMERS.getValue());
        dataBaseHandler.getPerformers(ps,
                Integer.parseInt(jsonObj.get("projectId").toString()), performers);
        dataBaseHandler.closeStatement(ps);

        ps = dataBaseHandler.getPreparedStatement(
                Query.GET_PROJECT.getValue());
        dataBaseHandler.getProject(ps,
                Integer.parseInt(jsonObj.get("projectId").toString()), project);
        dataBaseHandler.closeStatement(ps);
        project.put("managers", managers);
        project.put("performers", performers);
        out.println(project);
        out.flush();
    }

    public void edit(JSONObject jsonObj) {
        JSONArray managers = (JSONArray) jsonObj.get("managers");
        JSONArray performers = (JSONArray) jsonObj.get("performers");
        String name = jsonObj.get("name").toString();
        String startDate = jsonObj.get("startDate").toString();
        String endDate = jsonObj.get("endDate").toString();
        String user = jsonObj.get("user").toString();
        int percent = Integer.parseInt(jsonObj.get("percent").toString());
        int id = Integer.parseInt(jsonObj.get("id").toString());
        ps = dataBaseHandler.getPreparedStatement(
                Query.UPDATE_PROJECT.getValue());
        dataBaseHandler.updateProject(ps, name, startDate, endDate,
                percent, id);
        dataBaseHandler.closeStatement(ps);
        ps = dataBaseHandler.getPreparedStatement(
                Query.GET_ID_FROM_LOGIN.getValue());
        int userId = dataBaseHandler.getIdFromLogin(ps, user);
        dataBaseHandler.closeStatement(ps);
        managers.add(userId);
        //drop managers for project
        ps = dataBaseHandler.getPreparedStatement(
                Query.DROP_ALL_PROJECT_MANAGERS.getValue());
        dataBaseHandler.dropAllProjectManagers(ps, id);
        dataBaseHandler.closeStatement(ps);
        ps = dataBaseHandler.getPreparedStatement(
                Query.DROP_ALL_PROJECT_PERFORMERS.getValue());
        dataBaseHandler.dropAllProjectPerformers(ps, id);
        dataBaseHandler.closeStatement(ps);
        //add managers for project
        for (Object manager : managers) {
            int managerId = Integer.parseInt(manager.toString());
            ps = dataBaseHandler.getPreparedStatement(
                    Query.INSERT_PROJECT_MANGER.getValue());
            dataBaseHandler.insertPorojectManager(ps, managerId, id);
            dataBaseHandler.closeStatement(ps);
        }
        //add performers for project
        for (Object performer : performers) {
            int performerId = Integer.parseInt(performer.toString());
            ps = dataBaseHandler.getPreparedStatement(
                    Query.INSERT_PROJECT_PERFORMER.getValue());
            dataBaseHandler.insertPorojectPerformer(ps, performerId, id);
            dataBaseHandler.closeStatement(ps);
        }
    }

    public void search(JSONObject jsonObj) {
        List<Integer> resArray = new ArrayList();

        if (jsonObj.get("name") != null) {
            String name = jsonObj.get("name").toString();
            List<Integer> tmpArray = new ArrayList();
            ps = dataBaseHandler.getPreparedStatement(
                    Query.SELECT_BY_NAME.getValue());
            dataBaseHandler.selectByName(ps, name, tmpArray);
            dataBaseHandler.closeStatement(ps);
            System.out.println(tmpArray.toString());
            if (!tmpArray.isEmpty()) {
                if (resArray.isEmpty()) {
                    resArray = tmpArray;
                } else {
                    resArray = mergeArray(tmpArray, resArray);
                }
            }
        }
        if (jsonObj.get("startDate") != null) {
            String startDate = jsonObj.get("startDate").toString();
            List<Integer> tmpArray = new ArrayList();
            ps = dataBaseHandler.getPreparedStatement(
                    Query.SELECT_BY_START_DATE.getValue());
            dataBaseHandler.selectByStartDate(ps, startDate, tmpArray);
            dataBaseHandler.closeStatement(ps);
            System.out.println(tmpArray.toString());
            if (!tmpArray.isEmpty()) {
                if (resArray.isEmpty()) {
                    resArray = tmpArray;
                } else {
                    resArray = mergeArray(tmpArray, resArray);
                }
            }
        }
        if (jsonObj.get("endDate") != null) {
            String endDate = jsonObj.get("endDate").toString();
            List<Integer> tmpArray = new ArrayList();
            ps = dataBaseHandler.getPreparedStatement(
                    Query.SELECT_BY_END_DATE.getValue());
            dataBaseHandler.selectByEndDate(ps, endDate, tmpArray);
            dataBaseHandler.closeStatement(ps);
            System.out.println(tmpArray.toString());
            if (!tmpArray.isEmpty()) {
                if (resArray.isEmpty()) {
                    resArray = tmpArray;
                } else {
                    resArray = mergeArray(tmpArray, resArray);
                }
            }
        }

        if (jsonObj.get("manager") != null) {
            String manager = jsonObj.get("manager").toString();
            List<Integer> tmpArray = new ArrayList();
            ps = dataBaseHandler.getPreparedStatement(
                    Query.GET_ID_FROM_LOGIN.getValue());
            int managerId = dataBaseHandler.getIdFromLogin(ps, manager);
            dataBaseHandler.closeStatement(ps);
            ps = dataBaseHandler.getPreparedStatement(
                    Query.SELECT_BY_MANAGER.getValue());
            dataBaseHandler.selectByManagerId(ps, managerId, tmpArray);
            dataBaseHandler.closeStatement(ps);
            if (!tmpArray.isEmpty()) {
                if (resArray.isEmpty()) {
                    resArray = tmpArray;
                } else {
                    resArray = mergeArray(tmpArray, resArray);
                }
            }
        }

        if (jsonObj.get("performer") != null) {
            String performer = jsonObj.get("performer").toString();
            List<Integer> tmpArray = new ArrayList();
            ps = dataBaseHandler.getPreparedStatement(
                    Query.GET_ID_FROM_LOGIN.getValue());
            int performerId = dataBaseHandler.getIdFromLogin(ps, performer);
            dataBaseHandler.closeStatement(ps);
            ps = dataBaseHandler.getPreparedStatement(
                    Query.SELECT_BY_PERFORMER.getValue());
            dataBaseHandler.selectByPerformerId(ps, performerId, tmpArray);
            dataBaseHandler.closeStatement(ps);
            if (!tmpArray.isEmpty()) {
                if (resArray.isEmpty()) {
                    resArray = tmpArray;
                } else {
                    resArray = mergeArray(tmpArray, resArray);
                }
            }
        }

        JSONArray projectsArray = new JSONArray();
        for (Integer i : resArray) {
            JSONArray managers = new JSONArray();
            JSONArray performers = new JSONArray();
            JSONObject project = new JSONObject();

            ps = dataBaseHandler.getPreparedStatement(
                    Query.GET_MANAGERS_FROM_ID.getValue());
            dataBaseHandler.getManagers(ps, i, managers);
            dataBaseHandler.closeStatement(ps);

            ps = dataBaseHandler.getPreparedStatement(
                    Query.GET_PERFORMERS.getValue());
            dataBaseHandler.getPerformers(ps, i, performers);
            dataBaseHandler.closeStatement(ps);

            ps = dataBaseHandler.getPreparedStatement(
                    Query.GET_PROJECT.getValue());
            dataBaseHandler.getProject(ps, i, project);
            dataBaseHandler.closeStatement(ps);

            project.put("id", i);
            project.put("managers", managers);
            project.put("performers", performers);
            projectsArray.add(project);
        }
        out.println(projectsArray);
        out.flush();
    }

    public List<Integer> mergeArray(List<Integer> lArray, List<Integer> rArray) {
        List<Integer> resArray = new ArrayList();
        for (int a : lArray) {
            for (int b : rArray) {
                if (a == b) {
                    resArray.add(b);
                }
            }
        }
        return resArray;
    }

    public void loadAllUsersWithoutAdmin() {

        JSONObject response = new JSONObject();
        JSONArray usersWithoutAdmin = new JSONArray();

        ps = dataBaseHandler.getPreparedStatement(
                Query.SELECT_ALL_USERS_WITHOUT_ADMIN.getValue());
        boolean flag = dataBaseHandler.selectAllUsersWithoutAdmin(ps,
                usersWithoutAdmin);
        dataBaseHandler.closeStatement(ps);

        response.put("flag", flag);
        response.put("usersWithoutAdmin", usersWithoutAdmin);
        out.println(response);
        out.flush();
    }
    
    public void deleteUser(JSONObject jsonObj) {
        JSONObject response = new JSONObject();
        ps = dataBaseHandler.getPreparedStatement(
                Query.DELETE_USER_BY_ID.getValue());
        boolean flag = dataBaseHandler.deleteUserById(ps,
                Integer.parseInt(jsonObj.get("id").toString()));
        dataBaseHandler.closeStatement(ps);
        response.put("flag", flag);
        out.println(response);
        out.flush();
    }

    public void addUser(JSONObject jsonObj) {
        JSONObject response = new JSONObject();
        ps = dataBaseHandler.getPreparedStatement(
                Query.INSERT_INTO_USERS.getValue());
        boolean flag = dataBaseHandler.insertIntoUsers(ps,
                jsonObj.get("login").toString(),
                jsonObj.get("password").toString(),
                jsonObj.get("role").toString());
        response.put("flag", flag);
        out.println(response);
        out.flush();
    }
    
}
