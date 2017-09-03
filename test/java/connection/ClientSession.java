package connection;

import database.Main;
import me.paraweb.orderbuilder.Orders.Order;
import java.io.*;
import java.net.Socket;


public class ClientSession implements Runnable {
//public class ClientSession extends Thread {

    private Socket socket;
    BufferedReader In;
    PrintWriter Out;
    ObjectInputStream InObj = null;
    ObjectOutputStream OutObj = null;
    String command;
    InputStream in;
    OutputStream out;
    Order order;
    Main serialData = new Main();
    private boolean isActive;


    public ClientSession(Socket socket) throws IOException {
        //super("session");
        this.socket = socket;
        In  = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        Out = new PrintWriter(this.socket.getOutputStream(),true);
        in = this.socket.getInputStream();
        out = this.socket.getOutputStream();
        isActive = true;
        //start();
    }

    void disable(){
        isActive = false;
    }

    @Override
    public void run() {
        boolean b = true;
        System.out.printf("Поток %s начал работу... \n", Thread.currentThread().getName());
        while (isActive) {
            try {
                command = In.readLine();
                switch (command) {
                    case "save":
                        System.out.println("\nComamnd: " + command);
                        if (InObj == null) {
                            InObj = new ObjectInputStream(in);
                        }
                        order = (Order) InObj.readObject();
                        order.Debug();
                        serialData.ProjectList(order);
                        break;
                    case "getList":
                        System.out.println("\nComamnd: " + command);
                        String[] result = serialData.GetDBList();
                        Out.println(result.length);
                        for (int i = 0; i < result.length; i++) {
                            Out.println(result[i]);
                        }
                        break;
                    case "get":
                        System.out.println("\nComamnd: " + command);
                        int id = Integer.parseInt(In.readLine());
                        System.out.println("id: " + id);
                        order = serialData.GetTemplate(id);
                        order.Debug();
                        OutObj = new ObjectOutputStream(out);
                        OutObj.writeObject(order);
                        OutObj.flush();
                        break;
                    case "stop":
                        System.out.println("\nComamnd: " + command);
                        In.close();
                        Out.close();
                        in.close();
                        out.close();
                        socket.close();
                        //this.interrupt();
                        disable();
                        Thread.currentThread().interrupt();
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (isActive == false) break;
        }
        System.out.printf("Поток %s завершил работу... \n", Thread.currentThread().getName());
    }
}
