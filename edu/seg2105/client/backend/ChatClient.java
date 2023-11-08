// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package edu.seg2105.client.backend;

import ocsf.client.*;

import java.io.*;

import edu.seg2105.client.common.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 */
public class ChatClient extends AbstractClient {
  // Instance variables **********************************************

  /**
   * The interface type variable. It allows the implementation of
   * the display method in the client.
   */
  ChatIF clientUI;
  String loginID;

  // Constructors ****************************************************

  /**
   * Constructs an instance of the chat client.
   *
   * @param host     The server to connect to.
   * @param port     The port number to connect on.
   * @param clientUI The interface type variable.
   */

  public ChatClient(String loginID, String host, int port, ChatIF clientUI) throws IOException {
    super(host, port); // Call the superclass constructor
    this.clientUI = clientUI;
    this.loginID = loginID;
    openConnection();

    // authenticate
    sendToServer("#login " + loginID);
  }

  // Instance methods ************************************************

  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) {
    clientUI.display(msg.toString());

  }

  /**
   * This method handles all data coming from the UI
   *
   * @param message The message from the UI.
   */
  public void handleMessageFromClientUI(String message) {
    if (message.charAt(0) == '#') {
      String command = message.split(" ")[0];

      switch (command) {
        case "#quit":
          quit();
          System.exit(0);
          break;

        case "#logoff":
          try {
            closeConnection();
          } catch (IOException e) {
            clientUI.display("Could not logoff");
          }
          break;

        case "#sethost":
          if (!isConnected()) {
            setHost(message.split(" ")[1]);
          } else {
            clientUI.display("You must logoff before changing the host");
          }
          break;

        case "#setport":
          if (!isConnected()) {
            setPort(Integer.valueOf(message.split(" ")[1]));
          } else {
            clientUI.display("You must logoff before changing the port");
          }

          break;

        case "#login":
          if (!isConnected()) {
            try {
              openConnection();

              if (message.split(" ").length > 1) {
                loginID = message.split(" ")[1];
              }

              sendToServer("#login " + loginID);
            } catch (IOException e) {
              clientUI.display("Could not connect to server");
            }
          } else {
            clientUI.display("You must logoff before logging in");
          }
          break;

        case "#gethost":
          System.out.println("Hostname: " + getHost());
          break;

        case "#getport":
          System.out.println("Port: " + getPort());
          break;

        default:
          break;
      }

      return;
    }

    try {
      sendToServer(message);
    } catch (IOException e) {
      clientUI.display("Could not send message to server.  Terminating client.");
      quit();
    }
  }

  /**
   * This method terminates the client.
   */
  public void quit() {
    try {
      closeConnection();
    } catch (IOException e) {
      clientUI.display("Could not close connection");
    }
  }

  @Override
  protected void connectionClosed() {
    clientUI.display("Server has shutdown");

  }

  @Override
  protected void connectionException(Exception exception) {
    clientUI.display("Server has shutdown");
    System.exit(1);
  }

}
// End of ChatClient class
