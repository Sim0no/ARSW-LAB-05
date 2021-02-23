/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blueprints.persistence.impl;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import edu.eci.arsw.blueprints.persistence.BlueprintsPersistence;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author hcadavid
 */
@Component
@Qualifier("enMemoria")
public class InMemoryBlueprintPersistence implements BlueprintsPersistence{

    private final Map<Tuple<String,String>,Blueprint> blueprints=new HashMap<>();

    public InMemoryBlueprintPersistence() {
        //load stub data
        Point[] pts=new Point[]{new Point(140, 140),new Point(115, 115)};
        Blueprint bp=new Blueprint("german", "arte",pts);
        blueprints.put(new Tuple<>(bp.getAuthor(),bp.getName()), bp);
        pts=new Point[]{new Point(141, 120),new Point(145, 135)};
        bp=new Blueprint("german", "no es arte",pts);
        blueprints.put(new Tuple<>(bp.getAuthor(),bp.getName()), bp);
        pts=new Point[]{new Point(240, 146),new Point(135, 118)};
        bp=new Blueprint("james", "cerdaco",pts);
        blueprints.put(new Tuple<>(bp.getAuthor(),bp.getName()), bp);
        
    }    
    
    @Override
    public void saveBlueprint(Blueprint bp) throws BlueprintPersistenceException {
        if (blueprints.containsKey(new Tuple<>(bp.getAuthor(),bp.getName()))){
            throw new BlueprintPersistenceException("The given blueprint already exists: "+bp);
        }
        else{
            blueprints.put(new Tuple<>(bp.getAuthor(),bp.getName()), bp);
        }        
    }

    @Override
    public Blueprint getBlueprint(String author, String bprintname) throws BlueprintNotFoundException {
        Blueprint bp = blueprints.get(new Tuple<>(author, bprintname));
        if (bp == null){
            throw new BlueprintNotFoundException("Autor o nombre de plano no encontrado");
        }
        return bp;
    }




    public Set<Blueprint> getBlueprints() throws BlueprintNotFoundException {
        Set<Blueprint> conjunto = new HashSet<Blueprint>();
        for(Tuple<String,String> f: blueprints.keySet()) {
            String autor = f.o1;
            conjunto.add(getBlueprint(f.o1,f.o2));
        }

        return conjunto;
    }

    @Override
    public Set<Blueprint> getBlueprintbyAuthor(String author) throws BlueprintNotFoundException {
        Set<Blueprint> conjunto = new HashSet<Blueprint>();
        boolean flag = true;
        for(Tuple<String,String> f: blueprints.keySet()) {
            String autor = f.o1;
            if (author.equals(autor)){
                conjunto.add(getBlueprint(f.o1,f.o2));
                flag = false;
            }
        }
        if(flag){
            throw new BlueprintNotFoundException("No se encontró el autor");
        }
        return conjunto;
    }

    @Override
    public void updateBlueprint(String author, String bprintname, Blueprint newBlueprint) {

        blueprints.remove(new Tuple<>(author,bprintname));
        blueprints.put(new Tuple<>(newBlueprint.getAuthor(),newBlueprint.getName()), newBlueprint);

    }
}
