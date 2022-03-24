package tn.esprit.travel.service;

import tn.esprit.travel.entities.Role;
import tn.esprit.travel.entities.User;

import java.util.List;
import java.util.Map;

public interface IUserservice {
    User saveUser(User user);
    Role saveRole(Role role);
    void addRoleToUser(String username , String roleName);
    User getUser(String username);
    List<User>getUsers();


}
