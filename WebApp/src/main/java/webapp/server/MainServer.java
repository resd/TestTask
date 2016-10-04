package webapp.server;

import com.mongodb.MongoClient;
import com.mongodb.ReadPreference;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import webapp.data.Message;
import webapp.util.SendEmail;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

/**
 * Class represents server application.
 */
public class MainServer {

    public static final int SERVER_PORT = 46712;
    private MongoCollection records;
    private MongoCollection prices;

    public MainServer() {
        setupDataBaseConnection();
    }

    private void setupDataBaseConnection() {
        MongoClient localhost = new MongoClient("localhost", 27017);
        MongoDatabase mongoDatabase = localhost.getDatabase("autobahn").
                withReadPreference(ReadPreference.secondary());
        records = mongoDatabase.getCollection("records");
        prices = mongoDatabase.getCollection("prices");
    }

    /**
     * The entry point of server application.
     *
     * @param args the input arguments
     * @throws IOException the io exception
     */
    public static void main(String[] args) throws IOException {
        // Create ServerSocket
        ServerSocket serverSocket = new ServerSocket(SERVER_PORT);

        // Run Server
        while (true) {
            System.out.println("\n -- Wait for next socket");
            Socket socket = serverSocket.accept();

            MainServer server = new MainServer();
            new Thread(server.new ServerThread(socket)).start();
        }


    }

    private class ServerThread implements Runnable {

        private Socket socket;

        public ServerThread(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {

                try {

                    ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                    Message message = (Message) in.readObject();

                    handleMessage(message);

                    in.close();

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    socket.close();
                    System.out.println(" -- Socket has closed");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void handleMessage(Message message) {
            // If client enter autobahn
            if (!message.isLeave()) {

                insertRecordOfEnterToAutobahn(message);

            } else {
                // If client leave autobahn

                // Get information about entering into the autobahn from database
                Document inRecord = (Document) records.find(and(eq("client", message.getClient().getId()),
                        eq("leave", false))).first();

                double payment = calculatePayment(inRecord, message.getCheckpointId());

                // Send to email
                SendEmail.sendFromGMail(message.getClient().getEmail(), "Amount of payment : " +
                        payment + ";\nYou start using autobahn from checkpoint ¹" +
                        inRecord.get("in_checkpoint") + " at time: " + inRecord.getDate("date_in") +
                        " and leave autobahn at checkpoint ¹" + message.getCheckpointId() +
                        " at time: " + message.getDateTime());

                // Update record in database
                records.updateOne(and(
                        eq("client", message.getClient().getId()),
                        eq("leave", false)),
                        new Document("$set",
                                new Document("out_checkpoint", message.getCheckpointId()).
                                        append("leave", true).
                                        append("data_out", message.getDateTime())));
            }
        }

        private double calculatePayment(Document inRecord, int checkpointIdAtLeave) {
            // Get data from DB or generate sample
            Document pricesDocument = (Document) prices.find().first();
            ArrayList<Double> pricesList = (ArrayList<Double>) pricesDocument.get("prices");
            if (pricesList.isEmpty()) {
                for (int i = 0; i < 10; i++) {
                    pricesList.add(10D);
                }
            }

            // Calculate payment
            int checkpointIdAtEnter = inRecord.getInteger("in_checkpoint");
            double payment = 0;

            for (int i = checkpointIdAtEnter; i < checkpointIdAtLeave; i++) {
                payment = payment + pricesList.get(i);
            }

            return payment;
        }

        private void insertRecordOfEnterToAutobahn(Message message) {
            records.insertOne(new Document("client", message.getClient().getId()).
                    append("in_checkpoint", message.getCheckpointId()).
                    append("out_checkpoint", null).
                    append("leave", false).
                    append("date_in", message.getDateTime()).
                    append("date_out", null));
        }

    }
}
