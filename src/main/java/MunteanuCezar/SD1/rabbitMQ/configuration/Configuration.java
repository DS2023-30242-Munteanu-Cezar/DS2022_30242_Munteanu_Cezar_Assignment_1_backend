package MunteanuCezar.SD1.rabbitMQ.configuration;

import lombok.Getter;
import lombok.Setter;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

@Getter
@Setter
public class Configuration {

    private String TASK_QUEUE_NAME;
    private String HOST_NAME;
    private String USERNAME;
    private String PASSWORD;
    private String VIRTUAL_HOST_NAME;
    private String applicationIp;

    public Configuration(){
        this.TASK_QUEUE_NAME = "sdApp2";
        this.HOST_NAME = "goose-01.rmq2.cloudamqp.com";
        this.VIRTUAL_HOST_NAME = "aegchapt";
        this.USERNAME = "aegchapt";
        this.PASSWORD = "2bsZOrizreCCJmNfv62WWU3OWgsvDFeI";

        try {
            this.applicationIp = String.valueOf(InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException exception) {
            throw new RuntimeException(exception);
        }

    }
}
