package org.example;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static Student input(Scanner scanner){
        System.out.println("Enter some information about person");
        System.out.println("Enter age: ");
        int age = scanner.nextInt();
        System.out.println("Enter name: ");
        String name = scanner.next();
        System.out.println("Enter height: ");
        int height = scanner.nextInt();
        return new Student(name, age, height);
    }
    public static void main(String[] args) {


        try(Socket socket = new Socket("localhost", 3333);
           Scanner scanner = new Scanner(System.in);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {
            System.out.println("Client connected.");
            while(!socket.isOutputShutdown()){

                Student input = input(scanner);
                System.out.println("Write to Server: \n" + input);
                out.writeObject(input);
                out.flush();

                System.out.println("Want to stop? (n if you want to stop)");
                String ans = scanner.next();
                if(ans.equals("n")) {
                    System.out.println("Exiting...");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
