package tn.esprit.travel.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.travel.entities.Domain;
@Repository
public interface DomainRepository extends CrudRepository<Domain,Long> {
}
