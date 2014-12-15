package by.bsuir.client.nazarchuk.controller;

import by.bsuir.client.nazarchuk.view.representation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

public class EditController implements Initializable {
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
    private ObservableList<User> dataManagers;
    @FXML
    private ObservableList<User> dataPerformers;
    @FXML
    private TableView<User> performersTable;
    private BufferedReader in;
    private PrintWriter out;
    private String user;
    private int projectId;
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        managersTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        performersTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        dataManagers = FXCollections.observableArrayList();
        dataPerformers = FXCollections.observableArrayList();
    }    
    
    public void initData(BufferedReader i, PrintWriter o, String user, 
            int projectId) {
        this.in = i;
        this.out = o;
        this.user = user;
        this.projectId = projectId;
        loadManagers();
        loadPerformers();
        initProject();
    }
    
    @FXML
    private void edit(MouseEvent event) {
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
        project.put("method", "edit");
        project.put("user", user);
        project.put("id", projectId);
        out.println(project);
        out.flush();
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
                        Manager managerView = new Manager(Integer.parseInt(
                                o.get("id").toString()),
                                o.get("name").toString());
                        dataManagers.add(managerView);
                    }
                    managersTable.setItems(dataManagers);
                } catch (ParseException ex) {
                    System.err.println("Parse JSON exception: " + ex);
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
    
    public void initProject() {
        JSONObject obj = new JSONObject();
        obj.put("method", "initProject");
        obj.put("projectId" , projectId);
        out.println(obj);
        out.flush();
        String answer = null;
        try {
            while ((answer = in.readLine()) != null) {
                JSONParser parser = new JSONParser();
                try {
                    JSONObject project = (JSONObject) parser.parse(answer);
                    JSONArray projectManagers = (JSONArray) project.get("managers");
                    JSONArray projectPerformers = (JSONArray) project.get("performers");
                    name.setText(project.get("name").toString());
                    percent.setText(project.get("percent").toString());
                    startDate.setValue(
                            LocalDate.parse(project.get("startDate").toString(),
                                    DateTimeFormatter.ISO_DATE));
                    endDate.setValue(
                            LocalDate.parse(project.get("endDate").toString(),
                                    DateTimeFormatter.ISO_DATE));
                    for (User manager : dataManagers) {
                        for (Object projectManager : projectManagers) {
                            if (projectManager.toString().equals(manager.getLogin())) {   
                                managersTable.getSelectionModel().select(
                                    managersTable.getItems().indexOf(manager));
                            }
                        }
                    }
                    for (User performer : dataPerformers) {
                        for (Object projectPerformer : projectPerformers) {
                            if (projectPerformer.toString().equals(performer.getLogin())) {   
                                performersTable.getSelectionModel().select(
                                    performersTable.getItems().indexOf(performer));
                            }
                        }
                    }
                } catch (ParseException ex) {
                    System.err.println("Parse JSON exception: " + ex);
                }
                
                JSONArray performers = new JSONArray();
                
                break;
            }
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }
}
