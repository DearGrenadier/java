/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package courseclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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

/**
 * FXML Controller class
 *
 * @author dima
 */
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
    private TableView<Manager> managersTable;
    @FXML
    private TableView<Performer> performersTable;
    private ObservableList<Manager> dataManagers;
    private ObservableList<Performer> dataPerformers;
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
        ObservableList<Performer> selectedPerformers = 
                performersTable.getSelectionModel().getSelectedItems();
        ObservableList<Manager> selectedManagers = 
                managersTable.getSelectionModel().getSelectedItems();
        JSONObject project = new JSONObject();
        JSONArray performers = new JSONArray();
        JSONArray managers = new JSONArray();
        for (Performer p : selectedPerformers) {
            performers.add(p.getId());
        }
        project.put("performers", performers);
        for (Manager m : selectedManagers) {
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
                        Manager managerView = new Manager(Integer.parseInt(
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
                        Performer performerfView = new Performer(Integer.parseInt(
                                o.get("id").toString()),
                                o.get("name").toString(),
                                o.get("role").toString());
                        dataPerformers.add(performerfView);
                    }
                    performersTable.setItems(dataPerformers);
                } catch (ParseException ex) {
                    System.out.println("Parse JSON exception: " + ex);
                }
                break;
            }
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }
}
