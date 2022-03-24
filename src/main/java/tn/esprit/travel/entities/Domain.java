package tn.esprit.travel.entities;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Domain {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long idDomain;
    String title;
    String description;

    @OneToMany (mappedBy="domain")
    List<User> users;
}
