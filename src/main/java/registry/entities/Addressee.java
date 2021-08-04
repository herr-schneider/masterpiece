package registry.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.CascadeType.REMOVE;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "addressees_table")
public class Addressee extends Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(name = "addressee_name", nullable = false, length = 255)
    private String name;

    @Column(name = "addressee_address", nullable = false, length = 255)
    private String address;

    @OneToMany(cascade = {PERSIST, REMOVE}, mappedBy = "addressee")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    //@JsonBackReference
    @JsonIgnore
    private List<PostPackage> postPackages;

    public void addPackage(PostPackage postPackage) {
        if (postPackages == null) {
            postPackages = new ArrayList<>();
        }
        postPackages.add(postPackage);
        postPackage.setAddressee(this);
    }

    public Addressee(String name) {
        this.name = name;
    }

    public Addressee(String name, String address) {
        this.name = name;
        this.address = address;
    }
}
