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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class DesktopController implements Initializable {
    private BufferedReader in;
    private PrintWriter out;
    @FXML
    private Button add;
    @FXML
    private Button delete;
    @FXML
    private Button edit;
    @FXML
    private Button view;
    @FXML
    private TableView<ProjectView> projectsTable;
    @FXML
    private TextField name;
    @FXML
    private TextField manager;
    @FXML
    private TextField performers;
    @FXML
    private DatePicker startDate;
    @FXML
    private DatePicker endDate;
    @FXML
    private Button search;
    @FXML
    private Text userName;
    @FXML
    private Text role;
    @FXML
    private ObservableList<ProjectView> data;
    private int UserId;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        data = FXCollections.observableArrayList();
    }
    
    public void initData(String login, String r, BufferedReader i, 
            PrintWriter o) {
        out = o;
        in = i;
        userName.setText(login);
        role.setText(r);
        loadData();
    }
    
    public void loadData() {
        JSONObject obj = new JSONObject();
        obj.put("method", "entryView");
        obj.put("login", userName.getText());
        obj.put("role", role.getText());
        out.println(obj);
        out.flush();
        while (true) {
            String answer = null;
            try {
                if ((answer = in.readLine()) != null) {
                    JSONParser parser = new JSONParser();
                    JSONArray projects = new JSONArray();
                    try {
                        projects = (JSONArray) parser.parse(answer);
                    } catch (ParseException ex) {
                        System.out.println("Parse JSON exception: " + ex);
                    }
                    
                    for (Object project : projects) {
                        JSONObject ob = (JSONObject)project;
                        int id = Integer.parseInt(ob.get("id").toString());
                        String name = ob.get("name").toString();
                        String startDate = ob.get("startDate").toString();
                        String endDate = ob.get("endDate").toString();
                        int percent =
                                Integer.parseInt(ob.get("percent").toString());
                        String managers = "";
                        JSONArray managersArray = new JSONArray();
                        managersArray = (JSONArray) ob.get("managers");
                        
                        for (Object manager : managersArray) {
                            managers += manager.toString() + "\n";
                        }
                        String performers = "";
                        JSONArray performersArray = new JSONArray();
                        performersArray = (JSONArray) ob.get("performers");
                        
                        for (Object performer : performersArray) {
                            performers += performer.toString() + "\n";
                        }
                        ProjectView pv = new ProjectView(id, name, managers,
                                performers, startDate, endDate, percent);
                        data.add(pv);  
                    }
                    projectsTable.setItems(data);
                    break;
                }
            } catch (IOException ex) {
                System.err.println("IOException: " + ex);
            }
        }
    }
    
    @FXML
    private void add(MouseEvent event) {
        showAddDialog();
    }

    @FXML
    private void delete(MouseEvent event) {
        ProjectView selectedItem = 
                projectsTable.getSelectionModel().getSelectedItem();
        JSONObject obj = new JSONObject();
        obj.put("method", "deleteRow");
        obj.put("projectId", selectedItem.getId());
        out.println(obj);
        out.flush();
        
        data.remove(selectedItem);
        projectsTable.setItems(data);
    }

    @FXML
    private void edit(MouseEvent event) {
        
    }

    @FXML
    private void view(MouseEvent event) {
        data.clear();
        loadData();
    }

    @FXML
    private void search(MouseEvent event) {
    }
    
    public Stage showAddDialog() {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                        "add.fxml"
                )
        );
        Stage stage = new Stage();
        try {
            stage.setScene(new Scene((Pane) loader.load()));
        } catch (IOException ex) {
            System.err.println("IOException : " + ex);
        }
        AddController controller
                = loader.<AddController>getController();
        controller.initData(userName.getText(), in, out);
        stage.show();
        return stage;
    }
    
    public Stage showEditDialog() {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                        "edit.fxml"
                )
        );
        Stage stage = new Stage();
        try {
            stage.setScene(new Scene((Pane) loader.load()));
        } catch (IOException ex) {
            System.err.println("IOException : " + ex);
        }
        AddController controller
                = loader.<AddController>getController();
        controller.initData(userName.getText(), in, out);
        stage.show();
        return stage;
    }
    
}
