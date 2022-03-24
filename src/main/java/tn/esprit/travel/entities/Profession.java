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
public class Profession {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long idp ;
    String title;
    String description;

    @OneToMany(mappedBy="profession")
    List<User> users;
}
