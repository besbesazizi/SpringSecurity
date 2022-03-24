package tn.esprit.travel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.travel.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findByUsername(String username);

}
