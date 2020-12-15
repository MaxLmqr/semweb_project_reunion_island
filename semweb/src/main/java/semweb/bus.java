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

public class bus {
	public static void main(String[] args) {
		
		
        String datasetURL = "http://localhost:3030/reunion_island";
        String sparqlEndpoint = datasetURL + "/sparql";
        String sparqlUpdate = datasetURL + "/update";
        String graphStore = datasetURL + "/data";
        RDFConnection conneg = RDFConnectionFactory.connect(sparqlEndpoint,sparqlUpdate,graphStore);
		

        String tm ="http://www.thomasandmax.com/";
		String rdfs ="http://www.w3.org/2000/01/rdf-schema#";
		String xsd ="http://www.w3.org/2001/XMLSchema#";
		String geo ="http://www.w3.org/2003/01/geo/wgs84_pos#";

		
		// Def variables, and also the filePath.
		String csvFile = "/home/max/MasterCSP/semantic/final_project/gtfs-stops-cars-jaunes-lareunion.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ";(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
        
		try {

		    br = new BufferedReader(new FileReader(csvFile));
		    br.readLine();
		    while ((line = br.readLine()) != null) {
		    	
		    	//we create and upload the model every time 
		    	Model model = ModelFactory.createDefaultModel();
		    	
    			model.setNsPrefix("tm", tm);
    			model.setNsPrefix("rdfs", rdfs);
    			model.setNsPrefix("xsd", xsd);
    			model.setNsPrefix("geo", geo);
    			
    			Resource ArretTransport = model.createResource(tm+"ArretTransport");
				Resource StopCarJaune = model.createResource(tm+"StopCarJaune");
    			Resource spatialThing = model.createResource(geo + "SpatialThing");

				
				model.add(StopCarJaune, RDFS.subClassOf, ArretTransport);
				model.add(ArretTransport, RDFS.subClassOf, spatialThing);
				
				Property latitude = model.createProperty(tm+"lat");
				Property longitude = model.createProperty(tm+"long");
				
		        // we read the line
		        String[] ligne = line.split(cvsSplitBy);
				Resource nom_stop = model.createResource(tm+ligne[2].replaceAll(" ", "_"));
				String[] coordonnees = ligne[3].split(",");
				nom_stop.addProperty(RDF.type, StopCarJaune);
				nom_stop.addProperty(latitude, model.createTypedLiteral(Double.parseDouble(coordonnees[0])));
				nom_stop.addProperty(longitude, model.createTypedLiteral(Double.parseDouble(coordonnees[1])));
				conneg.load(model);
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
