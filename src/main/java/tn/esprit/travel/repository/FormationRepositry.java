package tn.esprit.travel.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.travel.entities.Formation;

@Repository
public interface FormationRepositry extends CrudRepository<Formation,Long> {
}
