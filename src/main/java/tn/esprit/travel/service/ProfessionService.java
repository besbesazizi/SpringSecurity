package tn.esprit.travel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.travel.entities.Profession;
import tn.esprit.travel.repository.ProfessionRepository;

import java.util.List;

@Service
public class ProfessionService implements IProfessionService{
    @Autowired
    ProfessionRepository pr;

    @Override
    public void addProffession(Profession profession) {
      if(!pr.existsById(profession.getIdp()))
            pr.save(profession);
    }

    @Override
    public List<Profession> getAll() {
        return (List<Profession>) pr.findAll();
    }
}
