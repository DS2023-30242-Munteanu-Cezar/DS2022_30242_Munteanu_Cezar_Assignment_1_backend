package MunteanuCezar.SD1.rabbitMQ;

import MunteanuCezar.SD1.entities.Device;
import MunteanuCezar.SD1.rabbitMQ.configuration.Configuration;
import MunteanuCezar.SD1.rabbitMQ.entity.Measure;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import MunteanuCezar.SD1.services.MeasurementService;
import com.rabbitmq.client.DeliverCallback;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

@Slf4j
public class Worker extends Thread{
    private MeasurementService measurementService;
    private Configuration configuration = new Configuration();

    private float thisConsumption = 0;

    private float maxConsumption = 0;
    private String maxUser = "";
    private UUID maxDescription = null;
    private int countTime = 0;

    public Worker(MeasurementService measurementService){
        this.measurementService = measurementService;
    }

    private ConnectionFactory createConnection(){
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(configuration.getHOST_NAME());
        connectionFactory.setUsername(configuration.getUSERNAME());
        connectionFactory.setPassword(configuration.getPASSWORD());
        connectionFactory.setVirtualHost(configuration.getVIRTUAL_HOST_NAME());
        connectionFactory.setRequestedHeartbeat(35);
        connectionFactory.setConnectionTimeout(35000);

        return connectionFactory;
    }

    private void Reciever(long id) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = this.createConnection();
        final Connection connection = connectionFactory.newConnection();
        final Channel channel = connection.createChannel();
        channel.queueDeclare(configuration.getTASK_QUEUE_NAME(), true, false, false, null);

        channel.basicQos(1);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            ObjectMapper objectMapper = new ObjectMapper();
            Measure measure = objectMapper.readValue(delivery.getBody(), Measure.class);

            try {
                doWork(measure);
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }
        };
        channel.basicConsume(configuration.getTASK_QUEUE_NAME(), false, deliverCallback, consumerTag -> {});
    }

    private void sendSocketMessage(UUID deviceName, String username) throws IOException {
        URL url= new URL("http://"+ configuration.getApplicationIp() + ":8080/send");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);

        String jsonInputString = "{" +
                "\"message\": \"Maximum hourly energy consumption exceeded for device: " + deviceName + "!\","+
                "\"username\": \"" + username + "\"" +
                "}";

        try(OutputStream outputStream = connection.getOutputStream()){
            byte[] input = jsonInputString.getBytes("utf-8");
            outputStream.write(input, 0, input.length);
        }

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(),  "utf-8"))){
            StringBuilder response = new StringBuilder();
            String responseLine;

            while((responseLine = bufferedReader.readLine()) != null){
                response.append(responseLine.trim());
            }
        }
    }

    private void doWork(Measure measure) throws IOException {
        Device device = this.measurementService.addMeasurementFromRabbitMQ(measure);

        this.thisConsumption = measure.getConsumption();
        this.countTime ++;

        if(this.thisConsumption >= this.maxConsumption){
            this.maxUser = device.getUser().getUsername();
            this.maxDescription = device.getId();
            this.maxConsumption = this.thisConsumption;
        }

        if(this.countTime == 6){
            if(this.maxConsumption > 100){
                log.info("Consumption excedeed! " + this.maxUser);
                sendSocketMessage(this.maxDescription, this.maxUser);
                this.maxConsumption = 0;
                this.countTime = 0;
            }
            else{
                this.countTime = 0;
            }
        }
//
//        if(this.thisConsumption > 100){
//            log.info("Consumption excedeed!");
//            sendSocketMessage(device.getDescription(), device.getUser().getUsername());
//        }
    }

    public void run(){
        try {
            Reciever(Thread.currentThread().getId());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
