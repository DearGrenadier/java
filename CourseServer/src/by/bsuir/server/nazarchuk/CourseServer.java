package by.bsuir.server.nazarchuk;

import by.bsuir.server.nazarchuk.model.ServerThread;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class CourseServer {
    
    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(Integer.parseInt(args[0]));
            System.out.println("Server is started...");
            Socket socket = null;
            while (true) {
                socket = server.accept();
                System.out.println(socket.getInetAddress()
						+ " connected");
                new ServerThread(socket);
            }
        } catch (IOException ex) { 
            System.err.println("IOException in method CourseServer::main : " + ex);
        }
    }
}
