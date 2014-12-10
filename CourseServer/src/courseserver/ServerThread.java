package courseserver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ServerThread extends Thread {

    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;
    private final DataBaseHandler dataBaseHandler;
    private PreparedStatement ps;

    public ServerThread(Socket s) throws IOException {
        socket = s;
        dataBaseHandler = new DataBaseHandler();
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket
                .getOutputStream())), true);
        start();
    }

    public void run() {
        try {
            while (true) {
                String str;
                if ((str = in.readLine()) != null) {
                    JSONParser parser = new JSONParser();
                    Object obj = parser.parse(str);
                    JSONObject jsonObj = (JSONObject) obj;
                    String method = jsonObj.get("method").toString();
                    switch (method) {
                        case "entry":
                            entry(jsonObj);
                            break;
                        case "entryView":
                            entryView(jsonObj);
                            break;
                        case "getManagers" :
                            getManagers(jsonObj.get("login").toString());
                            break;
                        case "getPerformers" :
                            getPerformers();
                            break;
                        case "deleteRow" :
                            deleteRow(Integer.parseInt(
                                    jsonObj.get("projectId").toString()));
                            break;
                        case "add" :
                            addProject(jsonObj);
                            break;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("IO Exception");
        } catch (ParseException ex) {
            System.err.println("Parse Exception");
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Socket not closed");
            }
        }
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
                Query.GET_ALL_PERFORMERS.getValue());
        dataBaseHandler.getAllPerformers(ps, performers);
        dataBaseHandler.closeStatement(ps);
        System.out.println(performers);
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
        JSONArray managers  = (JSONArray) jsonObj.get("managers");
        JSONArray performers  = (JSONArray) jsonObj.get("performers");
        String name  = jsonObj.get("name").toString();
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
}
