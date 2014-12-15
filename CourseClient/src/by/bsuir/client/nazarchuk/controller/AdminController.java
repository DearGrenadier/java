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
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class AdminController implements Initializable {

    @FXML
    private TextField login;
    @FXML
    private TextField password;
    @FXML
    private TextField role;
    @FXML
    private Text message;
    @FXML
    private ObservableList<User> data;
    @FXML
    private TableView<User> usersTable;

    private BufferedReader in;
    private PrintWriter out;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        data = FXCollections.observableArrayList();
        usersTable.setPlaceholder(new Text("Пусто!"));
    }

    @FXML
    private void add(MouseEvent event) {
        String login = this.login.getText();
        String password = this.password.getText();
        String role = this.role.getText();
        if (!login.equals("") && !password.equals("") && !password.equals("")) {
            JSONObject request = new JSONObject();
            request.put("login", login);
            request.put("password", password);
            request.put("role", role);
            request.put("method", "addUser");
            
            this.login.clear();
            this.password.clear();
            this.role.clear();
            
            out.println(request);
            out.flush();

            JSONParser parser = new JSONParser();
            String json = null;

            try {
                while ((json = in.readLine()) != null) {
                    JSONObject response;
                    try {
                        response = (JSONObject) parser.parse(json);
                        if (response.get("flag").toString().equals("true")) {
                            data.clear();
                            loadUsersAtTable();
                            message.setText("Добавленно успешно!");
                        } else {
                            message.setText("Ошибка добавления");
                        }
                    } catch (ParseException ex) {
                        System.err.println("Parse JSON exception in method "
                            + "AdminController::add : " + ex);
                    }
                    break;
                }
            } catch (IOException ex) {
                System.err.println("IOException in method "
                        + "AdminController::add : " + ex);
            }
        } else {
            message.setText("Не все формы заполнены!");
        }
    }

    @FXML
    private void delete(MouseEvent event) {
        User selectedItem
                = usersTable.getSelectionModel().getSelectedItem();
        JSONObject request = new JSONObject();
        request.put("id", selectedItem.getId());
        request.put("method", "deleteUser");
        out.println(request);
        out.flush();
        JSONParser parser = new JSONParser();
        String json = null;
        try {
            while ((json = in.readLine()) != null) {
                try {
                    JSONObject response = (JSONObject) parser.parse(json);
                    if (response.get("flag").toString().equals("true")) {
                        message.setText("Удалено успешно!");
                    } else {
                        message.setText("Ошибка удаления");
                    }
                    data.remove(selectedItem);
                    usersTable.setItems(data);
                } catch (ParseException ex) {
                    System.err.println("Parse JSON exception in method "
                            + "AdminController::delete : " + ex);
                }
                break;
            }
        } catch (IOException ex) {
            System.err.println("IOException in method "
                    + "AdminController::delete : " + ex);
        }
    }

    public void initData(BufferedReader in, PrintWriter out) {
        this.in = in;
        this.out = out;
        loadUsersAtTable();
    }

    public void loadUsersAtTable() {

        JSONObject request = new JSONObject();
        request.put("method", "loadAllUsersWithoutAdmin");

        out.println(request);
        out.flush();

        JSONParser parser = new JSONParser();
        String json = null;
        try {
            while ((json = in.readLine()) != null) {
                try {
                    JSONObject response = (JSONObject) parser.parse(json);
                    if (response.get("flag").toString().equals("true")) {
                        JSONArray users
                                = (JSONArray) response.get("usersWithoutAdmin");
                        for (Object user : users) {
                            JSONObject juser = (JSONObject) user;
                            User su = new SimpleUser(
                                    Integer.parseInt(juser.get("id").toString()),
                                    juser.get("login").toString(),
                                    juser.get("role").toString());
                            data.add(su);
                        }
                        usersTable.setItems(data);
                    }
                } catch (ParseException ex) {
                    System.err.println("Parse JSON exception in method "
                            + "AdminController::loadUsersAtTable : " + ex);
                }
                break;
            }
        } catch (IOException ex) {
            System.err.println("IOException in method "
                    + "AdminController::loadUsersAtTable : " + ex);
        }

    }

}
