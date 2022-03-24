package tn.esprit.travel.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tn.esprit.travel.entities.ConfirmationToken;
import tn.esprit.travel.entities.Role;
import tn.esprit.travel.entities.User;
import tn.esprit.travel.repository.ConfirmationTokenRepository;
import tn.esprit.travel.repository.RoleRepository;
import tn.esprit.travel.repository.UserRepository;
import tn.esprit.travel.service.EmailService;
import tn.esprit.travel.service.UserService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
@Slf4j
@RestController
@RequestMapping("/api")
public class UserController {

    /********************************----IoC -----*********************/
    @Autowired
    UserService us ;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    private EmailService emailService;
    @Autowired
    private RoleRepository rr;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /********************************----crud user -----*********************/
    @GetMapping("/company/users")
    public ResponseEntity<List<User>>getUsers(){
        return ResponseEntity.ok().body(us.getUsers());
    }

    @PostMapping("/company/save")
    public ResponseEntity<User>SaveUser(@RequestBody User user){
        URI uri =URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("api/user/save").toUriString());
        return ResponseEntity.created(uri).body(us.saveUser(user));
    }

    @PostMapping("/company/Role/save")
    public ResponseEntity<Role>SaveRole(@RequestBody Role role){
        URI uri =URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("api/role/save").toUriString());
        return ResponseEntity.created(uri).body(us.saveRole(role));

    }

    @PostMapping("/company/Role/addtouser")
    public ResponseEntity<?> addRoleToUser(@RequestBody RoleToUserForm form){
        us.addRoleToUser(form.getUsername(), form.getRolename());
        return ResponseEntity.ok().build();
    }

    /********************************----refresh Token-----*********************/
@GetMapping("/token/refresh")
    public void refreshtoken(HttpServletRequest request , HttpServletResponse response) throws IOException {
    String authorizationHeader = request.getHeader(AUTHORIZATION);
    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
        try {
            String refrech_token = authorizationHeader.substring("Bearer ".length());
            Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(refrech_token);
            String username = decodedJWT.getSubject();
            User user = us.getUser(username);
            String access_token = JWT.create().withSubject(user.getUsername()).withExpiresAt(new Date(System.currentTimeMillis()+10*60*1000))
                    .withIssuer(request.getRequestURL().toString())
                    .withClaim("roles",user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                    .sign(algorithm);
            Map<String ,String> tokens= new HashMap<>();
            tokens.put("access_token",access_token);
            tokens.put("refrech_token",refrech_token);
            response.setContentType(APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(),tokens);
        } catch (Exception exception) {
            response.setHeader("error", exception.getMessage());
            response.setStatus(FORBIDDEN.value());
            Map<String, String> error = new HashMap<>();
            error.put("error", exception.getMessage());
            response.setContentType(APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), error);
        }
    } else {
        throw new RuntimeException("Refressh token failed");
    }
}
/********************************----register as company-----*********************/
    @GetMapping("/register")
    public void registerCompany(@RequestBody User user) {
        us.registerCompany(user);
    }

    @GetMapping("/confirm-account")
    public void confirmUserAccountCompany( @RequestParam("token")String confirmationToken) {
        us.confirmUserAccountCompany(confirmationToken);
    }
/**********************************************************************************/

@GetMapping("/active-account")
public void activeUserAccount( @RequestParam("token")String confirmationToken) {
    us.activeUserAccount(confirmationToken);
}
    /******************************REST PASSWORD ********************************/
    @GetMapping("/rest-password/{name}")
    public void demandToRestPassword(@PathVariable("name") String username){
       us.demandToRestPassword(username);
    }
    @GetMapping("/confirm-password/{new}/{confirm}")
    public void RestPassword( @RequestParam("token")String confirmationToken, @PathVariable("new") String NewPassword ,@PathVariable("confirm") String ConfirmPassword ){
       us.RestPassword(confirmationToken,NewPassword,ConfirmPassword);
    }

}

@Data
class RoleToUserForm{
    private String username;
    private String rolename;
}
