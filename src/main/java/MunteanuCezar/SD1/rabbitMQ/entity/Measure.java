package MunteanuCezar.SD1.rabbitMQ.entity;

import lombok.*;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Measure implements Serializable {

    private UUID id;
    private float consumption;
    private Timestamp timestamp;

}
