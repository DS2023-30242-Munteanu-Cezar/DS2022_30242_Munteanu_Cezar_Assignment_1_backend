package MunteanuCezar.SD1.entities;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "measurements")
public class Measurement {

    @Id
    @GeneratedValue(generator = "uuid2", strategy = GenerationType.IDENTITY)
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Type(type = "uuid-binary")
    private UUID id;

    @Column(name = "energy_consumption", nullable = false)
    private int energyConsumption;

    @Column(name = "timestamp", nullable = false)
    Timestamp timestamp;

    @ManyToOne
    @JoinColumn(name = "device_id")
    private Device device;

}
