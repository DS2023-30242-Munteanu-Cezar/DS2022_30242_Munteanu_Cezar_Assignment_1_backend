package MunteanuCezar.SD1.entities;


import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "devices")
public class Device {
    @Id
    @GeneratedValue(generator = "uuid2", strategy = GenerationType.IDENTITY)
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Type(type = "uuid-binary")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "energy_consumption", nullable = false)
    private int energyConsumption;

    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL)
    private List<Measurement> measurementList;

    @Column(name = "adress", nullable = false)
    private String adress;

    @Column(name = "description", nullable = false)
    private String description;

}
