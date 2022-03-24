package tn.esprit.travel.entities;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reunion implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id ;
    @Temporal(TemporalType.TIMESTAMP)
    Date date;
    String Duree;
    String ojectif;
    String place;

    @ManyToOne
    User user;

    @ManyToMany(cascade = CascadeType.ALL ,mappedBy = "reunions")
    List<User> paticipants;



}
