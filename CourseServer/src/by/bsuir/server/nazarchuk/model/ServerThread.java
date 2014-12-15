package by.bsuir.server.nazarchuk.model;

import by.bsuir.server.nazarchuk.model.DataBaseHandler;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.PreparedStatement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ServerThread extends Thread {

    private final Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private Router router;

    public ServerThread(Socket socket) {
        this.socket = socket;
        try {
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            this.out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                    socket.getOutputStream())), true);
        } catch (IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        router = new Router(in, out);
        start();
    }

    public void run() {
        try {
            while (true) {
                String request;
                if ((request = in.readLine()) != null) {
                    JSONParser parser = new JSONParser();
                    Object json = parser.parse(request);
                    JSONObject jsonObj = (JSONObject) json;
                    String method = jsonObj.get("method").toString();
                    switch (method) {
                        case "entry":
                            router.entry(jsonObj);
                            break;
                        case "entryView":
                            router.entryView(jsonObj);
                            break;
                        case "getManagers":
                            router.getManagers(jsonObj.get("login").toString());
                            break;
                        case "getPerformers":
                            router.getPerformers();
                            break;
                        case "deleteRow":
                            router.deleteRow(Integer.parseInt(
                                    jsonObj.get("projectId").toString()));
                            break;
                        case "add":
                            router.addProject(jsonObj);
                            break;
                        case "initProject":
                            router.initProject(jsonObj);
                            break;
                        case "edit":
                            router.edit(jsonObj);
                            break;
                        case "search":
                            router.search(jsonObj);
                            break;
                        case "loadAllUsersWithoutAdmin":
                            router.loadAllUsersWithoutAdmin();
                            break;
                        case "deleteUser" :
                            router.deleteUser(jsonObj);
                            break;
                        case "addUser" :
                            router.addUser(jsonObj);
                                break;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("IO Exception");
        } catch (ParseException ex) {
            System.err.println("Parse Exception" + ex);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Socket not closed");
            }
        }
    } 
}
    