package registry.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Sender extends Person {

    @Column(name = "sender_name", nullable = false, length = 255)
    private String name;

    @Column(name = "sender_address", nullable = false, length = 255)
    private String address;

    public Sender(String name) {
        super(name);
    }
}
