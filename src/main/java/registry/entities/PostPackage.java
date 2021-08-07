package registry.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "packages")
public class PostPackage {

    @Id
    //@JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "doku_number", nullable = false, length = 10)
    private String doku;

    private String huNumber;

    private LocalDate arrival;

    @Embedded
    private Sender sender;

    //@JoinColumn(name = "addressee_name")
    @ManyToOne
    private Addressee addressee;

    @Enumerated(EnumType.STRING)
    private StorageStatus storageStatus;

    public PostPackage(String doku, String huNumber, LocalDate arrival,
                       Sender sender, Addressee addressee, StorageStatus storageStatus) {
        this.doku = doku;
        this.huNumber = huNumber;
        this.arrival = arrival;
        this.sender = sender;
        this.addressee = addressee;
        this.storageStatus = storageStatus;
    }
}