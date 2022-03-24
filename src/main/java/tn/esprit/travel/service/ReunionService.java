package tn.esprit.travel.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import tn.esprit.travel.entities.Domain;
import tn.esprit.travel.entities.Profession;
import tn.esprit.travel.entities.Reunion;
import tn.esprit.travel.entities.User;
import tn.esprit.travel.repository.DomainRepository;
import tn.esprit.travel.repository.ProfessionRepository;
import tn.esprit.travel.repository.RunionRepository;
import tn.esprit.travel.repository.UserRepository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class ReunionService implements IReunionService{

    @Autowired
    UserRepository ur ;
    @Autowired
    RunionRepository rr ;
    @Autowired
    DomainRepository dr ;
    @Autowired
    ProfessionRepository pr;
    @Autowired
    EmailService emailService;


    @Override
    public Reunion add( Reunion reunion) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user =new User();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails)principal).getUsername();
            user = ur.findByUsername(username);

        } else {
            String username = principal.toString();
            user = ur.findByUsername(username);
        }
        reunion.setUser(user);
        return  rr.save(reunion);
    }

    @Override
    public Reunion updateReunion(Long idr,Reunion reunion) {
        Reunion reunion1 = rr.findById(idr).get();
        reunion1.setDate(reunion.getDate());
        reunion1.setDuree(reunion.getDuree());
        reunion1.setPlace(reunion.getPlace());
        return rr.save(reunion1);
    }

    @Override
    public void delete(Long idReunion) {
        Reunion reunion1 = rr.findById(idReunion).get();
        rr.delete(reunion1);
    }

    @Override
    public void sendInvitationByDomain(Long id, Long idR) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user =new User();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails)principal).getUsername();
            user = ur.findByUsername(username);

        } else {
            String username = principal.toString();
            user = ur.findByUsername(username);
        }
        Domain domain = dr.findById(id).get();
        Reunion reunion = rr.findById(idR).get();
        List<User> users = new ArrayList<>();
        for (User u: domain.getUsers()) {
            users.add(u);
            u.getReunions().add(reunion);
            ur.save(u);
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(u.getEmail());
            mailMessage.setSubject("reunion !");
            mailMessage.setFrom(user.getEmail());
            mailMessage.setText("reunion le date de :"+reunion.getDate()+ "  / au "+reunion.getPlace());
            emailService.sendEmail(mailMessage);
            log.info("done");
        }
        reunion.setPaticipants(users);
        rr.save(reunion);
    }

    @Override
    public void sendInvtationByProfession(Long id, Long idR) {
        List<User> users = new ArrayList<>();
        Profession profession = pr.findById(id).get();
        Reunion reunion = rr.findById(idR).get();
        for (User u: profession.getUsers()) {
            users.add(u);
            u.getReunions().add(reunion);
            ur.save(u);
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(u.getEmail());
            mailMessage.setSubject("reunion !");
            mailMessage.setFrom("ahmed.zarrai@esprit.tn");
            mailMessage.setText("reunion le date de :"+reunion.getDate()+ "  / au "+reunion.getPlace());
            emailService.sendEmail(mailMessage);
            log.info("done");
        }
        reunion.setPaticipants(users);
        rr.save(reunion);

    }



    @Scheduled(fixedRate = 20000)
    public void remindForReunion() {
        Date dt = new Date();
        List<User> users = new ArrayList<>();
        Calendar c = Calendar.getInstance();
        Calendar c1 = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.DATE, -1);
        int dt1 = c.get(Calendar.DAY_OF_MONTH);
        List<Reunion> reunions = (List<Reunion>) rr.findAll();
        for (Reunion r : reunions) {
            c1.setTime(r.getDate());
            c1.add(Calendar.DATE, -1);
            int dr1 = c1.get(Calendar.DAY_OF_MONTH);
            log.info(String.valueOf(dr1));
            users = r.getPaticipants();
            int cc = users.size();
            if (dt1 == dr1 && !users.isEmpty()) {
                for (User user : users) {
                    log.info("email :"+user.getEmail());
                   SimpleMailMessage mailMessage = new SimpleMailMessage();
                    mailMessage.setTo(user.getEmail());
                    mailMessage.setSubject("reunion demain !");
                    mailMessage.setFrom("ahmed.zarrai@esprit.tn");
                    mailMessage.setText("reunion le date de :"+r.getDate() +"place:"+r.getPlace()+"titre profession: "+user.getProfession().getTitle());
                    emailService.sendEmail(mailMessage);
                    log.info("done" + user.getEmail());
                }
            }
        }
    }

}
