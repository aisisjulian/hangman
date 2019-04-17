package hangman;

import java.io.Serializable;
import java.util.function.Consumer;

/*hello this is ari!!!*/
public class Client extends ClientConnection {

    private String ip;
    private int port;
    private String name;

    public Client(String ip, int port, String name, Consumer<Serializable> callback) {
        super(callback);
        this.ip = ip;
        this.port = port;
        this.name = name;
    }

    @Override
    public String getIP() {
        // TODO Auto-generated method stub
        return this.ip;
    }

    @Override
    public int getPort() {
        // TODO Auto-generated method stub
        return this.port;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }
}