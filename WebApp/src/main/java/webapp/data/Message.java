package webapp.data;

import java.io.Serializable;
import java.util.Objects;

/**
 * Message class represents data that sends through sockets.
 */
public class Message implements Serializable {

    private final Client client;
    private final int checkpointId;
    private final boolean leave;
    private final String dateTime;

    /**
     * Instantiates a new Message.
     *
     * @param client       the client instance
     * @param checkpointId the id of checkpoint
     * @param leave        is client leave autobahn
     * @param dateTime     the time of event happened
     */
    public Message(Client client, int checkpointId, boolean leave, String dateTime) {
        this.client = client;
        this.checkpointId = checkpointId;
        this.leave = leave;
        this.dateTime = dateTime;
    }

    public Client getClient() {
        return client;
    }

    public int getCheckpointId() {
        return checkpointId;
    }

    public boolean isLeave() {
        return leave;
    }

    public String getDateTime() {
        return dateTime;
    }

    @Override
    public String toString() {
        return "Message[" + client + ", " + checkpointId + ", " + leave + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message)) return false;
        Message message = (Message) o;
        return checkpointId == message.checkpointId &&
                leave == message.leave &&
                Objects.equals(client, message.client);
    }

    @Override
    public int hashCode() {
        return Objects.hash(client, checkpointId, leave);
    }
}
