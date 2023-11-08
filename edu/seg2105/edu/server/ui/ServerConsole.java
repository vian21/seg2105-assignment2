package edu.seg2105.edu.server.ui;

import edu.seg2105.client.common.ChatIF;
import edu.seg2105.edu.server.backend.EchoServer;

import java.io.IOException;
import java.util.Scanner;

public class ServerConsole implements ChatIF {
    private Scanner fromConsole;
    private EchoServer server;

    public ServerConsole(EchoServer server) {
        this.server = server;
    }

    public void display(String message) {
        System.out.println(message);
    }

    public void start() {
        fromConsole = new Scanner(System.in);
        String message;

        while (true) {
            message = fromConsole.nextLine();

            if (message.charAt(0) == '#') {
                handleCommand(message);
                continue;
            }

            String msg = "SERVER MESSAGE> " + message;

            this.display(msg);
            server.sendToAllClients(msg);
        }
    }

    private void handleCommand(String command) {
        command = command.split(" ")[0];

        switch (command) {
            case "#quit":
                server.quit();
                break;

            case "#stop":
                server.stopListening();
                break;

            case "#close":
                server.stopListening();
                try {
                    server.close();
                } catch (IOException e) {
                    System.out.println("ERROR - Could not close server!");
                }
                break;

            case "#setport":
                if (!server.isListening()) {
                    server.setPort(Integer.valueOf(command.split(" ")[1]));
                } else {
                    display("Server must be closed before changing the port");
                }
                break;

            case "#start":
                if (!server.isListening()) {
                    try {
                        server.listen();
                    } catch (Exception e) {
                        System.out.println("ERROR - Could not start listening for clients!");
                    }
                } else {
                    System.out.println("Server is already listening for clients");
                }
                break;

            case "#getport":
                System.out.println("Port: " + server.getPort());
                break;
            default:
                break;
        }
    }

}
