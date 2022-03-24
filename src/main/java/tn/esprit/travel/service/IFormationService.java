package tn.esprit.travel.service;

import tn.esprit.travel.entities.Formation;

import java.util.List;

public interface IFormationService {
    public Formation addFormation(Formation formation);
    public void updateFormation(Long idf, Formation formation);
    public void deleteFormation(Long idF);
    public List<Formation> getFormationByUser(Long idUser);
}
