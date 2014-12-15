package by.bsuir.client.nazarchuk.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class EntryController implements Initializable {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    @FXML
    private TextField login;
    @FXML
    private PasswordField pass;
    @FXML
    private Text entryMessage;
    @FXML
    private Button entry;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            socket = new Socket(InetAddress.getLocalHost(), 8071);
        } catch (IOException ex) {
            System.err.println("Ошибка подключения");
        }
        try {
            in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            out = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())), true);
        } catch (IOException e) {
            try {
                socket.close();
            } catch (IOException ex) {
                System.err.println("Socket not closed");
            }
        }
    }

    @FXML
    private void entry(MouseEvent event) throws InterruptedException {
        JSONObject obj = new JSONObject();
        obj.put("method", "entry");
        obj.put("login", login.getText());
        obj.put("pass", pass.getText());
        out.println(obj);
        out.flush();
        try {
            while (true) {
                String answer;
                if ((answer = in.readLine()) != null) {
                    JSONParser parser = new JSONParser();
                    obj = (JSONObject) parser.parse(answer);
                    Boolean find = (Boolean) obj.get("find");
                    if (find) {
                        ((Node) (event.getSource())).getScene().getWindow().hide();
                        if (obj.get("role").toString().equals("admin")) {
                            showAdminDialog();
                        } else {
                            showDescktopDialog(obj.get("name").toString(),
                                    obj.get("role").toString());
                        }
                    } else {
                        entryMessage.setText(
                                "Неверные данные. Повторите еще раз!");

                    }
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("IO Exception Client:" + e);
        } catch (ParseException ex) {
            System.err.println("Parse Exception");
        }
    }

    public void showDescktopDialog(String login, String role) {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("../view/fxml/desktop.fxml"));
        Stage stage = new Stage();
        try {
            stage.setScene(new Scene((Pane) loader.load()));
        } catch (IOException ex) {
            System.err.println("IOException : ");
        }
        DesktopController controller
                = loader.<DesktopController>getController();
        controller.initData(login, role, in, out);
        stage.show();
    }
    
    public void showAdminDialog() {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("../view/fxml/admin.fxml"));
        Stage stage = new Stage();
        try {
            stage.setScene(new Scene((Pane) loader.load()));
        } catch (IOException ex) {
            System.err.println("IOException : ");
        }
        AdminController controller
                = loader.<AdminController>getController();
        controller.initData(in, out);
        stage.show();
    }
    
}
