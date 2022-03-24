package tn.esprit.travel.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.travel.entities.Formation;
import tn.esprit.travel.entities.Profession;

@Repository
public interface ProfessionRepository extends CrudRepository<Profession, Long> {
}
