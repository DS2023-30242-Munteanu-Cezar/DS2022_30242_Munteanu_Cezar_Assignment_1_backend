package MunteanuCezar.SD1.dtos;

import lombok.*;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeasurementDTO {
    private UUID id;
    private int energyConsumption;
    private Timestamp timestamp;
    private UUID deviceId;
}
