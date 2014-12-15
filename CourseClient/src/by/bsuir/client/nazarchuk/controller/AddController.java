package by.bsuir.client.nazarchuk.controller;

import by.bsuir.client.nazarchuk.view.representation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class AddController implements Initializable {
    @FXML
    private TextField name;
    @FXML
    private TextField percent;
    @FXML
    private DatePicker startDate;
    @FXML
    private DatePicker endDate;
    @FXML
    private TableView<User> managersTable;
    @FXML
    private TableView<User> performersTable;
    private ObservableList<User> dataManagers;
    private ObservableList<User> dataPerformers;
    private BufferedReader in;
    private PrintWriter out;
    private String user;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        managersTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        performersTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        dataManagers = FXCollections.observableArrayList();
        dataPerformers = FXCollections.observableArrayList();
    }    
    
    public void initData(String login, BufferedReader i, PrintWriter o) {
        user = login;
        in = i;
        out = o;
        loadManagers();
        loadPerformers();
    }
    @FXML
    private void add(MouseEvent event) {
        ObservableList<User> selectedPerformers = 
                performersTable.getSelectionModel().getSelectedItems();
        ObservableList<User> selectedManagers = 
                managersTable.getSelectionModel().getSelectedItems();
        JSONObject project = new JSONObject();
        JSONArray performers = new JSONArray();
        JSONArray managers = new JSONArray();
        for (User p : selectedPerformers) {
            performers.add(p.getId());
        }
        project.put("performers", performers);
        for (User m : selectedManagers) {
            managers.add(m.getId());
        }
        project.put("managers", managers);
        project.put("name", name.getText());
        project.put("percent", percent.getText());
        project.put("startDate", startDate.getValue().toString());
        project.put("endDate", endDate.getValue().toString());
        project.put("method", "add");
        project.put("user", user);
        out.println(project);
        out.flush();
        
        name.setText("");
        percent.setText("%");
        managersTable.getSelectionModel().clearSelection();
        performersTable.getSelectionModel().clearSelection();   
    }
    public void loadManagers() {
        JSONObject obj = new JSONObject();
        obj.put("method", "getManagers");
        obj.put("login", user);
        out.println(obj);
        out.flush();
        String answer = null;
        try {
            while ((answer = in.readLine()) != null) {
                JSONParser parser = new JSONParser();
                JSONArray managers = new JSONArray();
                try {
                    managers = (JSONArray) parser.parse(answer);
                    for (Object manager : managers) {
                        JSONObject o = (JSONObject) manager;
                        User managerView = new Manager(Integer.parseInt(
                                o.get("id").toString()),
                                o.get("name").toString());
                        dataManagers.add(managerView);
                    }
                    managersTable.setItems(dataManagers);
                } catch (ParseException ex) {
                    System.out.println("Parse JSON exception: " + ex);
                }
                break;
            }
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }
    
    public void loadPerformers() {
        JSONObject obj = new JSONObject();
        obj.put("method", "getPerformers");
        out.println(obj);
        out.flush();
        String answer = null;
        try {
            while ((answer = in.readLine()) != null) {
                JSONParser parser = new JSONParser();
                JSONArray performers = new JSONArray();
                try {
                    performers = (JSONArray) parser.parse(answer);
                    for (Object performer : performers) {
                        JSONObject o = (JSONObject) performer;
                        User performerfView = new SimpleUser(Integer.parseInt(
                                o.get("id").toString()),
                                o.get("name").toString(),
                                o.get("role").toString());
                        dataPerformers.add(performerfView);
                    }
                    performersTable.setItems(dataPerformers);
                } catch (ParseException ex) {
                    System.err.println("Parse JSON exception: " + ex);
                }
                break;
            }
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }
}
