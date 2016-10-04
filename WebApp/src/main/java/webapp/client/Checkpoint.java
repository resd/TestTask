package webapp.client;

import webapp.server.MainServer;
import webapp.data.Client;
import webapp.data.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Random;

/**
 * Class represents a client application.
 */
public class Checkpoint {

    private Client getNewClient() {
        return new Client(new Random().nextLong(), "resd94@gmail.com");
    }

    /**
     * Send information to server from checkpoint.
     *
     * @param client       the client instance
     * @param checkpointId the id of checkpoint
     * @param leave        is client leave autobahn
     */
    private void sendToServerFromCheckpoint(Client client, int checkpointId, boolean leave) {

        // Create socket
        Socket socket = new Socket();

        try {

            // Connect to server
            socket.connect(new InetSocketAddress(MainServer.SERVER_PORT));

            // Write information to server from checkpoint
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(new Message(client, checkpointId, leave, LocalDateTime.now().toString()));
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * The entry point of client application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        // Create sample data to test classes
        Checkpoint checkpoint = new Checkpoint();
        Client client = checkpoint.getNewClient();
        checkpoint.sendToServerFromCheckpoint(client, 0, false);
        checkpoint.sendToServerFromCheckpoint(client, 3, true);
    }

}
