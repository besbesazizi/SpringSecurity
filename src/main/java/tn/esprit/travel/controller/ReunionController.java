package tn.esprit.travel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.travel.entities.Reunion;
import tn.esprit.travel.service.ReunionService;

@RestController
    @RequestMapping("/api")
public class ReunionController {
    @Autowired
    ReunionService rs ;

    @GetMapping("/add")
    @ResponseBody
    public Reunion add(@RequestBody Reunion reunion){
        return rs.add(reunion);
    }

    @PutMapping("/updater/{idr}")
    public Reunion updateReunion(@PathVariable("idr")Long idr,@RequestBody Reunion reunion){
        return rs.updateReunion(idr,reunion);
    }

    @DeleteMapping("/deleter/{idr}")
    public void delete(@PathVariable("idr") Long idReunion){
        rs.delete(idReunion);
    }
    @PostMapping("/sendDomain/{id}/{idr}")
    @ResponseBody
    public void sendInvitationByDomain(@PathVariable("id") Long id,@PathVariable("idr") Long idR){
        rs.sendInvitationByDomain(id,idR);
    }
    @PostMapping("/sendbyProfession/{id}/{idr}")
    @ResponseBody
    public void sendInvtationByProfession(@PathVariable("id") Long id,@PathVariable("idr") Long idp){
        rs.sendInvtationByProfession(id,idp);
    }

}
