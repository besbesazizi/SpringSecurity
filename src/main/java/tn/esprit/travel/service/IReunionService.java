package tn.esprit.travel.service;

import tn.esprit.travel.entities.Reunion;

public interface IReunionService {
    public Reunion add( Reunion reunion);
    public Reunion updateReunion(Long idr,Reunion reunion);
    public void delete(Long idReunion);
    public void sendInvitationByDomain(Long id, Long idR);
    public void sendInvtationByProfession(Long id, Long idR);

}
