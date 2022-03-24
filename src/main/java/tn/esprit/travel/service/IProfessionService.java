package tn.esprit.travel.service;

import tn.esprit.travel.entities.Profession;

import java.util.List;

public interface IProfessionService {
    public void  addProffession(Profession profession);
    public List<Profession> getAll ();
}
