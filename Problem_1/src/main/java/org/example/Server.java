package org.example;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private Server(){}
    public static void start(){
        Student student;
        try	(ServerSocket server = new ServerSocket(3333)){

            Socket client = server.accept();
            System.out.print("Accepted.");

            ObjectInputStream inputStream = new ObjectInputStream(client.getInputStream());
            while(!client.isClosed()) {
                System.out.println("Server reading");
                student = (Student) inputStream.readObject();
                System.out.println("Read from client: \n" + student);
            }
            System.out.println("Client disconnected");
            inputStream.close();
            client.close();
        } catch (ClassNotFoundException | IOException e) {
            System.exit(0);
        }
    }
    public static void main(String[] args) {
        Server.start();
    }
}