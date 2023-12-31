package edu.seg2105.edu.server.backend;

import java.io.IOException;

// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 
import edu.seg2105.edu.server.ui.ServerConsole;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 */
public class EchoServer extends AbstractServer {
  // Class variables *************************************************

  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;

  static ServerConsole console;

  // Constructors ****************************************************

  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port) {
    super(port);
  }

  // Instance methods ************************************************

  /**
   * This method handles any messages received from the client.
   *
   * @param msg    The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient(Object msg, ConnectionToClient client) {
    console.display("Message received: " + msg.toString() + " from " + client.getInfo("loginID"));

    if (msg.toString().startsWith("#login")) {
      String login_id = msg.toString().split(" ")[1];

      if (client.getInfo("loginID") != null) {
        try {
          client.sendToClient("You are already logged in.");
          client.close();
        } catch (IOException e) {
        }
      }

      client.setInfo("loginID", login_id);
      System.out.println(login_id + " has logged on.");
      return;
    }

    this.sendToAllClients(client.getInfo("loginID").toString() + "> " + msg);
  }

  /**
   * This method overrides the one in the superclass. Called
   * when the server starts listening for connections.
   */
  protected void serverStarted() {
    System.out.println("Server listening for connections on port " + getPort());
  }

  /**
   * This method overrides the one in the superclass. Called
   * when the server stops listening for connections.
   */
  protected void serverStopped() {
    System.out.println("Server has stopped listening for connections.");
  }

  @Override
  protected void clientConnected(ConnectionToClient client) {
    System.out.println("A new client has connected to the server.");
  }

  @Override
  protected void clientDisconnected(ConnectionToClient client) {
    System.out.println("Client disconnected: " + client);
  }

  // Class methods ***************************************************

  /**
   * This method is responsible for the creation of
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on. Defaults to 5555
   *                if no argument is entered.
   */
  public static void main(String[] args) {
    int port = 0; // Port to listen on

    try {
      port = Integer.parseInt(args[0]); // Get port from command line
    } catch (Throwable t) {
      port = DEFAULT_PORT; // Set port to 5555
    }

    EchoServer sv = new EchoServer(port);

    try {
      sv.listen(); // Start listening for connections

      // start console
      console = new ServerConsole(sv);
      console.start();

    } catch (Exception ex) {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }

  public void quit() {
    try {
      close();
      System.exit(0);
    } catch (Exception e) {
      System.out.println("ERROR - Could not close server!");
    }
  }
}
// End of EchoServer class
