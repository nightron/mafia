package pl.edu.agh.mafia;

import com.hp.hpl.jena.rdf.model.*;

import java.io.FileWriter;
import java.io.IOException;

public class MafiaModel {

    static final String MAFIA_URI = "http://mafia/";
    static final String RELATIONSHIP_URI = "http://purl.org/vocab/relationship/";
    static final String ONTOLOGY_FILENAME = "ontolgy.owl";

    public Model model;
    private MafiaModel() {

        model = ModelFactory.createDefaultModel();



    }

    private void createMafiaModel(){
        Property bossOf = model.createProperty(RELATIONSHIP_URI,"bossOf");
        Property counselorOf = model.createProperty(RELATIONSHIP_URI,"counselorOf");
        Property associateOf = model.createProperty(RELATIONSHIP_URI,"associateOf");

        Resource boss = model.createResource(MAFIA_URI+"boss");
        Resource consigliere = model.createResource(MAFIA_URI+"consigliere");
        Resource viceBoss = model.createResource(MAFIA_URI+"viceBoss");

        boss.addProperty(bossOf, viceBoss);
        boss.addProperty(bossOf, consigliere);

        consigliere.addProperty(counselorOf, boss);
        for (int i=0; i<3; i++){
            Resource officer = model.createResource(MAFIA_URI+"officer"+i);
            viceBoss.addProperty(bossOf, officer);
            for (int j=0; j<3; j++){
                Resource soldier = model.createResource(MAFIA_URI+"soldier" +((i*3)+j));
                officer.addProperty(bossOf, soldier);
                for (int k=0; k<5; k++){
                    Resource associate = model.createResource(MAFIA_URI + "associate" + ((i*3+j)*5+k));
                    soldier.addProperty(associateOf, associate);
                }
            }
        }
    }

    private void exportToFile(){
        try{
            FileWriter out = new FileWriter(ONTOLOGY_FILENAME);
            this.model.write(out, "RDF/XML-ABBREV");
        }catch(IOException e){
            e.printStackTrace();
        }
    }


    public static void main(String args[]) {


        MafiaModel theFamily = new MafiaModel();
        theFamily.createMafiaModel();
        theFamily.model.write(System.out, "RDF/XML");

        theFamily.exportToFile();

        System.out.println(theFamily.model);
    }
}

