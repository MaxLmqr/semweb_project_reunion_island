package semweb;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionFactory;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;

public class sante {
	

	
	public static void main(String[] args) {
		
		
        String datasetURL = "http://localhost:3030/reunion_island";
        String sparqlEndpoint = datasetURL + "/sparql";
        String sparqlUpdate = datasetURL + "/update";
        String graphStore = datasetURL + "/data";
        RDFConnection conneg = RDFConnectionFactory.connect(sparqlEndpoint,sparqlUpdate,graphStore);
		

        String tm ="http://www.thomasandmax.com/";
		String rdfs ="http://www.w3.org/2000/01/rdf-schema#";
		String geo ="http://www.w3.org/2003/01/geo/wgs84_pos#";
		String xsd ="http://www.w3.org/2001/XMLSchema#";

		
		//we read the csv file
		String csvFile = "/home/max/MasterCSP/semantic/final_project/etablissements-du-domaine-sanitaire-et-social-millesime0public.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ";";//",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";

		try {

		    br = new BufferedReader(new FileReader(csvFile));
		    br.readLine();
		    while ((line = br.readLine()) != null) {
		    	
		    	//we create and upload the model every time 
		    	Model model = ModelFactory.createDefaultModel();
		    	
				model.setNsPrefix("rdfs", rdfs);
				model.setNsPrefix("geo", geo);
				model.setNsPrefix("xsd", xsd);
				model.setNsPrefix("tm", tm);
				
				
				Resource Etablissement = model.createResource(tm+"Etablissement");
				Resource EtablissementSante = model.createResource(tm+"EtablissementSante");
    			Resource spatialThing = model.createResource(geo + "SpatialThing");

				model.add(EtablissementSante, RDFS.subClassOf, Etablissement);
				model.add(Etablissement, RDFS.subClassOf, spatialThing);

				
				Property aAdresse = model.createProperty(tm + "aAdresse");
				Property aCodePostal = model.createProperty(tm + "aCodePostal");
				Property aCommune = model.createProperty(tm + "aCommune");
				Property aLieu = model.createProperty(tm + "aLieu");

				Property lattitude = model.createProperty(tm+"lat");
				Property longitude = model.createProperty(tm+"long");

		        // we read the line
		        String[] ligne = line.split(cvsSplitBy);
		        
				Resource nom_etablissement = model.createResource(tm+ligne[2].replaceAll(" ", "_"));
				Resource CategorieSante = model.createResource(tm+ligne[13].replaceAll(" ","_").replaceAll("é", "e"));
				Resource commune = model.createResource(tm+ligne[26].replaceAll(" ", "_"));
				model.add(CategorieSante, RDFS.subClassOf, EtablissementSante);
				nom_etablissement.addProperty(RDF.type, CategorieSante);
				commune.addProperty(aLieu, nom_etablissement);
				
				try {
				nom_etablissement.addProperty(aAdresse, model.createLiteral(ligne[23]));
				nom_etablissement.addProperty(aCodePostal, model.createTypedLiteral(Integer.parseInt(ligne[25])));
				nom_etablissement.addProperty(aCommune, commune);		
				String[] coordonnees = ligne[27].split(",");				
					nom_etablissement.addProperty(lattitude, model.createTypedLiteral(Double.parseDouble(coordonnees[0])));
					nom_etablissement.addProperty(longitude, model.createTypedLiteral(Double.parseDouble(coordonnees[1])));
					conneg.load(model);
				} catch (Exception e) {
					nom_etablissement.addProperty(longitude, "0");
					System.out.println("Erreur sur cette donnée.");
				} finally {
				}	        
		        model.write(System.out,"Turtle");
		        
		    }

		} catch (FileNotFoundException e) {
		    e.printStackTrace();
		} catch (IOException e) {
		    e.printStackTrace();
		} finally {
		    if (br != null) {
		        try {
		            br.close();
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		    }
		}
		
        

	}

}