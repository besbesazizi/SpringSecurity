package tn.esprit.travel.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.travel.entities.Reunion;

@Repository
public interface RunionRepository extends CrudRepository<Reunion, Long> {
}
