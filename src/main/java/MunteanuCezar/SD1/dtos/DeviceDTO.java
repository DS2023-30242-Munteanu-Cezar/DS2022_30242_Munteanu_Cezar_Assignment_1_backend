package MunteanuCezar.SD1.dtos;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeviceDTO {
    private UUID id;
    private String userUsername;
    private int energyConsumption;
    private String adress;
    private String description;
    private List<UUID> measurementIds;

}
