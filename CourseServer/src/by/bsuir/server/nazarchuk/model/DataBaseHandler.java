package by.bsuir.server.nazarchuk.model;

import by.bsuir.server.nazarchuk.model.DataBaseConnector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class DataBaseHandler {

    private Connection connection;

    public DataBaseHandler() {
        try {
            connection = DataBaseConnector.getConnection(
                    "jdbc:mysql://localhost:3306/course",
                    "root", "root");
        } catch (SQLException e) {
            System.err.println("Connection error:" + e);
        }
    }

    public PreparedStatement getPreparedStatement(String query) {
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(query);
        } catch (SQLException e) {
            System.err.println("Prepare statement error:" + e);
        }
        return ps;
    }

    public boolean entry(PreparedStatement ps,
            JSONObject answer, String login, String pass) {
        boolean flag = false;
        try {
            ps.setString(1, login);
            ps.setString(2, pass);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("login");
                String role = rs.getString("role");
                answer.put("id", id);
                answer.put("name", name);
                answer.put("role", role);
                flag = true;
            }
        } catch (SQLException e) {
            System.err.println("Execute query error:" + e);
        }
        return flag;
    }

    public boolean getProjectsForManager(PreparedStatement ps,
            List<Integer> projects, int id) {
        boolean flag = false;
        try {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                projects.add(rs.getInt("projectId"));
                flag = true;
            }
        } catch (SQLException e) {
            System.err.println("Execute query error:" + e);
        }
        return flag;
    }

    public int getIdFromLogin(PreparedStatement ps, String login) {
        try {
            ps.setString(1, login);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            System.err.println("Execute query error:" + e);
        }
        return 0;
    }

    public boolean getManagers(PreparedStatement ps, Integer id,
            JSONArray managers) {
        boolean flag = false;
        try {
            ps.setInt(1, id.intValue());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                managers.add(rs.getString("login"));
                flag = true;
            }
        } catch (SQLException e) {
            System.err.println("Execute query error:" + e);
        }
        return flag;
    }

    public boolean getPerformers(PreparedStatement ps, Integer id,
            JSONArray performers) {
        boolean flag = false;
        try {
            ps.setInt(1, id.intValue());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                performers.add(rs.getString("login"));
                flag = true;
            }
        } catch (SQLException e) {
            System.err.println("Execute query error:" + e);
        }
        return flag;
    }

    public boolean getProject(PreparedStatement ps, Integer id,
            JSONObject project) {
        boolean flag = false;
        try {
            ps.setInt(1, id.intValue());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                project.put("name", rs.getString("name"));
                project.put("startDate", rs.getDate("startDate").toString());
                project.put("endDate", rs.getDate("endDate").toString());
                project.put("percent", rs.getInt("percent"));
                flag = true;
            }
        } catch (SQLException e) {
            System.err.println("Execute query error:" + e);
        }
        return flag;
    }
    

    public boolean getMangersWithoutUser(PreparedStatement ps, String login,
            JSONArray managers) {
        boolean flag = false;
        try {
            ps.setString(1, login);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                JSONObject obj = new JSONObject();
                obj.put("id", rs.getInt("id"));
                obj.put("name", rs.getString("login"));
                managers.add(obj);
                flag = true;
            }
        } catch (SQLException e) {
            System.err.println("Execute query error:" + e);
        }
        return flag;
    }

    public boolean getAllPerformers(PreparedStatement ps,
            JSONArray performers) {
        boolean flag = false;
        try {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                JSONObject obj = new JSONObject();
                obj.put("id", rs.getInt("id"));
                obj.put("name", rs.getString("login"));
                obj.put("role", rs.getString("role"));
                performers.add(obj);
                flag = true;
            }
        } catch (SQLException e) {
            System.err.println("Execute query error:" + e);
        }
        return flag;
    }

    public void closeStatement(PreparedStatement ps) {
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                System.err.println("Prepared statement close error:" + e);
            }
        }
    }

    public boolean deleteProjectManagerById(PreparedStatement ps, int id) {
        boolean flag = false;
        try {
            ps.setInt(1, id);
            ps.executeUpdate();
            flag = true;
        } catch (SQLException e) {
            System.err.println("Execute query error1:" + e);
        }
        return flag;
    }

    public boolean deleteProjectById(PreparedStatement ps, int id) {
        boolean flag = false;
        try {
            ps.setInt(1, id);
            ps.executeUpdate();
            flag = true;
        } catch (SQLException e) {
            System.err.println("Execute query error2:" + e);
        }
        return flag;
    }

    public boolean deleteProjectPerformerById(PreparedStatement ps, int id) {
        boolean flag = false;
        try {
            ps.setInt(1, id);
            ps.executeUpdate();
            flag = true;
        } catch (SQLException e) {
            System.err.println("Execute query error:" + e);
        }
        return flag;
    }

    public boolean addProject(PreparedStatement ps, String name, String startDate, String endDate, int percent) {
        boolean flag = false;
        try {
            ps.setString(1, name);
            ps.setString(2, startDate);
            ps.setString(3, endDate);
            ps.setInt(4, percent);
            ps.executeUpdate();
            flag = true;
        } catch (SQLException e) {
            System.err.println("Execute query error:" + e);
        }
        return flag;
    }

    public int getLastProjectId(PreparedStatement ps) {
        ResultSet rs = null;
        try {
            rs = ps.executeQuery();
            rs.next();
            return rs.getInt("id");
        } catch (SQLException ex) {
            System.err.println("SQLException : " + ex);
        }
        return 0;
    }

    public boolean insertPorojectManager(PreparedStatement ps, int managerId, 
            int projectId) {
        boolean flag = false;
        try {
            ps.setInt(1, managerId);
            ps.setInt(2, projectId);
            ps.executeUpdate();
            flag = true;
        } catch (SQLException e) {
            System.err.println("Execute query error:" + e);
        }
        return flag;
    }
    
    public boolean insertPorojectPerformer(PreparedStatement ps, int performerId, 
            int projectId) {
        boolean flag = false;
        try {
            ps.setInt(1, performerId);
            ps.setInt(2, projectId);
            ps.executeUpdate();
            flag = true;
        } catch (SQLException e) {
            System.err.println("Execute query error:" + e);
        }
        return flag;
    }

    public boolean updateProject(PreparedStatement ps, String name, String startDate,
            String endDate, int percent, int id) {
        boolean flag = false;
        try {
            ps.setString(1, name);
            ps.setString(2, startDate);
            ps.setString(3, endDate);
            ps.setInt(4, percent);
            ps.setInt(5, id);
            ps.executeUpdate();
            flag = true;
        } catch (SQLException e) {
            System.err.println("Execute query error:" + e);
        }
        return flag;
    }

    public boolean dropAllProjectManagers(PreparedStatement ps, int id) {
        boolean flag = false;
        try {
            ps.setInt(1, id);
            ps.executeUpdate();
            flag = true;
        } catch (SQLException e) {
            System.err.println("Execute query error:" + e);
        }
        return flag;
    }

    public boolean dropAllProjectPerformers(PreparedStatement ps, int id) {
        boolean flag = false;
        try {
            ps.setInt(1, id);
            ps.executeUpdate();
            flag = true;
        } catch (SQLException e) {
            System.err.println("Execute query error:" + e);
        }
        return flag;
    }

    public boolean selectByName(PreparedStatement ps, String name, List<Integer> tmpArray) {
        boolean flag = false;
        ResultSet rs = null;
        try {
            ps.setString(1, name);
            rs = ps.executeQuery();
            while(rs.next()) {
                tmpArray.add(rs.getInt("id"));
            }
        } catch (SQLException ex) {
            System.err.println("SQLException : " + ex);
        }
        return flag;
    }
    
    public boolean selectByStartDate(PreparedStatement ps, String startDate,
            List<Integer> tmpArray) {
        boolean flag = false;
        ResultSet rs = null;
        try {
            ps.setString(1, startDate);
            rs = ps.executeQuery();
            while(rs.next()) {
                tmpArray.add(rs.getInt("id"));
            }
        } catch (SQLException ex) {
            System.err.println("SQLException : " + ex);
        }
        return flag;
    }
    
    public boolean selectByEndDate(PreparedStatement ps, String endDate,
            List<Integer> tmpArray) {
        boolean flag = false;
        ResultSet rs = null;
        try {
            ps.setString(1, endDate);
            rs = ps.executeQuery();
            while(rs.next()) {
                tmpArray.add(rs.getInt("id"));
            }
        } catch (SQLException ex) {
            System.err.println("SQLException : " + ex);
        }
        return flag;
    }

    public boolean selectByManagerId(PreparedStatement ps, int managerId, List<Integer> tmpArray) {
        boolean flag = false;
        ResultSet rs = null;
        try {
            ps.setInt(1, managerId);
            rs = ps.executeQuery();
            while(rs.next()) {
                tmpArray.add(rs.getInt("projectId"));
            }
        } catch (SQLException ex) {
            System.err.println("SQLException : " + ex);
        }
        return flag;
    }
    
    public boolean selectByPerformerId(PreparedStatement ps, int performerId,
            List<Integer> tmpArray) {
        boolean flag = false;
        ResultSet rs = null;
        try {
            ps.setInt(1, performerId);
            rs = ps.executeQuery();
            while(rs.next()) {
                tmpArray.add(rs.getInt("projectId"));
                flag = true;
            }
        } catch (SQLException ex) {
            System.err.println("SQLException : " + ex);
        }
        return flag;
    }
    
    public boolean selectAllUsersWithoutAdmin(PreparedStatement ps,
            JSONArray usersWithoutAdmin) {
        boolean flag = false;
        ResultSet rs = null;
        try {
            rs = ps.executeQuery();
            while(rs.next()) {
                JSONObject user = new JSONObject();
                user.put("id", rs.getInt("id"));
                user.put("login", rs.getString("login"));
                user.put("role", rs.getString("role"));
                usersWithoutAdmin.add(user);
                flag = true;
            }
        } catch (SQLException ex) {
            System.err.println("SQLException : " + ex);
        }
        return flag;
    }

    public boolean deleteUserById(PreparedStatement ps, int id) {
        boolean flag = false;
        try {
            ps.setInt(1, id);
            ps.executeUpdate();
            flag = true;
        } catch (SQLException ex) {
            System.err.println("Execute update error from method"
                    + " DataBaseHandler::deleteUserById : " + ex);
        }
        return flag;
    }

    public boolean insertIntoUsers(PreparedStatement ps, String login,
            String password, String role) {
        boolean flag = false;
        try {
            ps.setString(1, login);
            ps.setString(2, password);
            ps.setString(3, role);
            ps.executeUpdate();
            flag = true;
        } catch (SQLException ex) {
            System.err.println("Execute update error from method"
                    + " DataBaseHandler::insertIntoUsers : " + ex);
        }
        return flag;
        
    }
}
