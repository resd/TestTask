package webapp.data;

import java.io.Serializable;
import java.util.Objects;

/**
 * Client class represents data about client.
 */
public class Client implements Serializable {

    private final long id;
    private final String email;

    /**
     * Instantiates a new Client.
     *
     * @param id    the client id
     * @param email the client email
     */
    public Client(long id, String email) {
        this.id = id;
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "Client[" + id + ", " + email + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return id == client.id &&
                Objects.equals(email, client.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }
}








