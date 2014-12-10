package courseserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class CourseServer {
    
    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(8071);
        try {
            Socket socket = null;
            while (true) {
                socket = server.accept();
                try {
                    new ServerThread(socket);
                } catch (IOException e) {
                    socket.close();
                }
            }
        } finally {
            server.close();
        }
    }
}
