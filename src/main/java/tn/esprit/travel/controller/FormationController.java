package tn.esprit.travel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.travel.entities.Formation;
import tn.esprit.travel.service.FormationService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class FormationController {

    @Autowired
    FormationService fs;

    @GetMapping("/addf")
    @ResponseBody
    public Formation addFormation(@RequestBody Formation formation){
        return fs.addFormation(formation);
    };

    @PutMapping("/update/{idf}")
    @ResponseBody
    public void updateFormation(@PathVariable("idf") Long idf, @RequestBody Formation formation){
        fs.updateFormation(idf,formation);
    };

    @DeleteMapping("/delete/{idf}")
    public void deleteFormation(@PathVariable("idf")Long idF){
        fs.deleteFormation(idF);
    };

    @GetMapping("/Getbyuser/{idu}")
    public List<Formation> getFormationByUser(@PathVariable("idu") Long idUser){
        return fs.getFormationByUser(idUser);
    }
}
