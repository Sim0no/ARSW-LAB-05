package edu.eci.arsw.blueprints.controller;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import edu.eci.arsw.blueprints.services.BlueprintsServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping(value = "/")

public class BlueprintAPIController {
    @Autowired
    BlueprintsServices blprsrvc;

    @GetMapping("/blueprints")
    public ResponseEntity<?> getAllBlueprints() throws BlueprintNotFoundException {
        return new ResponseEntity<>(blprsrvc.getAllBlueprints(), HttpStatus.OK);

    }
    @GetMapping("/blueprints/{author}")
    public ResponseEntity<?> getAllBlueprintsByAuthor(@PathVariable(value = "author")String author)  {

        try {
            return new ResponseEntity<>(blprsrvc.getBlueprintsByAuthor(author),HttpStatus.OK);
        } catch (BlueprintNotFoundException e) {
            return new ResponseEntity<>("No se encontró este autor.",HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/blueprints/{author}/{bpname}")
    public ResponseEntity<?> getBlueprintByAuthorAndName(@PathVariable(value = "author") String author,@PathVariable(value = "bpname") String bpname){
        try {
            return new ResponseEntity<>( blprsrvc.getBlueprint(author,bpname), HttpStatus.OK);
        } catch (BlueprintNotFoundException e) {
            return new ResponseEntity<>("No se encontró este plano.",HttpStatus.NOT_FOUND);
        }

    }
    @PostMapping("/blueprints")
    public ResponseEntity<?> addBlueprint(@RequestBody Blueprint bp){
        try {
            blprsrvc.addNewBlueprint(bp);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (BlueprintPersistenceException e) {
            return new ResponseEntity<>("Este plano ya existe",HttpStatus.FORBIDDEN);
        }


    }

    @PutMapping("/blueprints/{author}/{bpname}")
    public ResponseEntity<?> updateBlueprint(@PathVariable(value = "author") String author,@PathVariable(value = "bpname") String bpname,@RequestBody Blueprint bp){
        blprsrvc.updateBlueprint(author, bpname, bp);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }





}
