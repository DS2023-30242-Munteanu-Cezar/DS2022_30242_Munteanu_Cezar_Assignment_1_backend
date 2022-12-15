package MunteanuCezar.SD1.rabbitMQ.controller;

import MunteanuCezar.SD1.rabbitMQ.dto.MessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@RestController
public class WebSocketTextController {

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(@RequestBody MessageDTO text){
        simpMessagingTemplate.convertAndSend("/topic/message", text);
        return new ResponseEntity<>("Send", HttpStatus.OK);
    }

    @MessageMapping("/sendMessage")
    public void receiveMessage(@Payload MessageDTO text) {
    }

    @SendTo("/topic/message")
    public MessageDTO broadcastMessage(@Payload MessageDTO text) {
        return text;
    }

}
