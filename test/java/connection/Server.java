package connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static final int DEFAULT_PORT = 3000;


    public static void main(String[] args) {

      ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(DEFAULT_PORT);
            System.out.println("Server started on port: " + serverSocket.getLocalPort() + "\n");
        } catch (IOException e) {
            System.out.println("Port " + DEFAULT_PORT + " is blocked.");
            System.exit(-1);
        }

        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                ClientSession session = new ClientSession(clientSocket);
                new Thread(session).start();
                System.out.println("Connection sucsessful!");
                //new ClientSession(clientSocket).start();
            } catch (IOException e) {
                System.out.println("Failed to establish connection.");
                System.out.println(e.getMessage());
                System.exit(-1);
            }
        }
    }
}
