package tn.esprit.travel.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import tn.esprit.travel.entities.ConfirmationToken;
import tn.esprit.travel.entities.Role;
import tn.esprit.travel.entities.User;
import tn.esprit.travel.repository.ConfirmationTokenRepository;
import tn.esprit.travel.repository.RoleRepository;
import tn.esprit.travel.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class UserService implements IUserservice, UserDetailsService {

    @Autowired
    UserRepository ur ;
    @Autowired
    RoleRepository rr;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
     ConfirmationTokenRepository confirmationTokenRepository;
    @Autowired
     EmailService emailService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       User user = ur.findByUsername(username);
       if(user==null){
           log.info("user noy found");
           throw new UsernameNotFoundException("user not found");

       }else {
           log.info("username :{}",username);
       }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
       user.getRoles().forEach(role -> {
           authorities.add(new SimpleGrantedAuthority(role.getName()));
       });

        return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(), authorities);
    }

    /**************----- company area --------*******************/
    //------------------- company wil create account -------------------
    public void registerCompany( User user) {
        User existingUser = ur.findByUsername(user.getUsername());
        if(existingUser != null)
        {log.info("This email already exists!");}
        else
        {
            Role roleCompany = new Role("ROLE_COMPANY");
            rr.save(roleCompany);
            user.getRoles().add(roleCompany);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            ur.save(user);
            ConfirmationToken confirmationToken = new ConfirmationToken(user);
            confirmationTokenRepository.save(confirmationToken);
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(user.getEmail());
            mailMessage.setSubject("Activé compte");
            mailMessage.setFrom("ahmed.zarrai@esprit.tn");
            mailMessage.setText("To confirm your account, please click here : "
                    +"http://localhost:8089/api/active-account?token="+confirmationToken.getConfirmationToken());
            emailService.sendEmail(mailMessage);
            log.info("successfulRegisteration");
        }
    }
    //------------------- active company account -------------------
    public void confirmUserAccountCompany(String confirmationToken)
    {
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);
        if(token != null)
        {
            User user = ur.findByUsername(token.getUserEntity().getUsername());
            user.setEnable(true);
            ur.save(user);
            log.info("accountVerified");
        }
        else
        {
            log.info("The link is invalid or broken!");
        }
    }

    //-------- send invitation  to employee ---------------
    @Override
    public User saveUser(User user) {
        User existingUser = ur.findByUsername(user.getUsername());
        if(existingUser != null)
        {
            log.info("This email already exists!");
        }else {
            Role role = new Role("ROLE_USER");
            rr.save(role);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setEmail(user.getEmail());
            user.getRoles().add(role);
            ur.save(user);
            log.info("saving new user");
            ConfirmationToken confirmationToken = new ConfirmationToken(user);
            confirmationTokenRepository.save(confirmationToken);
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(user.getEmail());
            mailMessage.setSubject("Activé compte");
            mailMessage.setFrom("ahmed.zarrai@esprit.tn");
            mailMessage.setText("To confirm your account, please click here : "
                    +"http://localhost:8089/api/active-account?token="+confirmationToken.getConfirmationToken());
            emailService.sendEmail(mailMessage);
        }
        return user;
    }

    //---------company will  add new role ----------------
    @Override
    public Role saveRole(Role role) {
        log.info("saving new role");
        return rr.save(role);
    }

    // --------campany will add role to user ------------
    @Override
    public void addRoleToUser(String username, String roleName) {
        log.info("add role to usser");
        User user = ur.findByUsername(username);
        Role role = rr.findByName(roleName);
        user.getRoles().add(role);
        ur.save(user);
    }

    // ------------campany get user by username --------------------
    @Override
    public User getUser(String username) {
        log.info("get user");
        return ur.findByUsername(username);
    }

    // ------------campany get all user by username ----------------
    @Override
    public List<User> getUsers() {
        log.info("get users");
        return ur.findAll();
    }



    /**********************------employee area-------*****************/

    // ------------ app will active account emplyee -------------

    public void activeUserAccount(String confirmationToken) {
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);
        if(token != null)
        {
            User user = ur.findByUsername(token.getUserEntity().getUsername());
            user.setEnable(true);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            ur.save(user);
            log.info("accountVerified");
        }
        else {log.info("The link is invalid or broken!");}
    }

    /******************************REST PASSWORD ********************************/
    //----------------------Demande To Rest PWD-----------------------
    public void demandToRestPassword(@PathVariable("name") String username){
        User user = ur.findByUsername(username);
        if(user !=null){
            ConfirmationToken restPwdToken = new ConfirmationToken(user);
            confirmationTokenRepository.save(restPwdToken);
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(user.getEmail());
            mailMessage.setSubject("rest password ! ");
            mailMessage.setFrom("ahmed.zarrai@esprit.tn");
            mailMessage.setText("To rest your password, please click here : "
                    +"http://localhost:8089/api/confirm-password?token="+ restPwdToken.getConfirmationToken());
            emailService.sendEmail(mailMessage);
            log.info("/rest-password sended");
        } else {log.info("does not exist");}
    }

  //----------------------REST PWD --------------------------------

    public void RestPassword(String confirmationToken, String NewPassword ,String ConfirmPassword ){
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);
        if(token!=null){
            User user = token.getUserEntity();

            user.setPassword(passwordEncoder.encode(NewPassword));
            ur.save(user);
            log.info("done!");
        }
        else {log.info("The link is invalid or broken!");}
    }
 /*************************** Scheduler 3 days *********************************/
/*
 @Scheduled(fixedRate = 60000)
 public  void reminderEmployeesToActiveAccount(){
     List<User> users = ur.findAll();
     List<User> usersdisable = new ArrayList<>();
     usersdisable = users.stream().filter(e->!e.isEnable()).collect(Collectors.toList());
     for (User u: usersdisable) {
         log.info("{}",u.getUsername());
         ConfirmationToken restPwdToken = new ConfirmationToken(u);
         confirmationTokenRepository.save(restPwdToken);
         SimpleMailMessage mailMessage = new SimpleMailMessage();
         mailMessage.setTo(u.getEmail());
         mailMessage.setSubject("reminder to confirm account ! ");
         mailMessage.setFrom("ahmed.zarrai@esprit.tn");
         mailMessage.setText("To active your password, please click here : "
                 +"http://localhost:8089/api/active-account?token="+ restPwdToken.getConfirmationToken());
         emailService.sendEmail(mailMessage);
         log.info("/rest-password sended");
     }
 }
*/

    /***************************-------- stat--------*********************************/

}
