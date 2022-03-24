package tn.esprit.travel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.travel.entities.Profession;
import tn.esprit.travel.repository.ProfessionRepository;
import tn.esprit.travel.service.ProfessionService;

import java.util.List;

@RestController
@RequestMapping("/profession")
public class ProfessionController {

    @Autowired
    ProfessionService ps;
    @PostMapping("/addP")
    @ResponseBody
    public void addProffession(@RequestBody Profession profession){
    ps.addProffession(profession);
    }

    @GetMapping("/getallp")
    public List<Profession> getAll(){
        return ps.getAll();
    }
}
