package tn.esprit.travel.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id ;
    private String name ;
    private String username;
    private String password;
    private boolean isEnable;
    private  String Email;


    @ManyToMany(fetch = FetchType.EAGER )
    private Collection<Role> roles = new ArrayList<>();

    @ManyToMany
    List<Reunion> reunions;

    @OneToMany (mappedBy="user")
    List<Reunion> ReunionsCompany;

    @ManyToOne
    Domain domain;
    @ManyToOne
    Profession profession;

    @OneToMany(mappedBy="user")
    List<Formation> formations;



}
