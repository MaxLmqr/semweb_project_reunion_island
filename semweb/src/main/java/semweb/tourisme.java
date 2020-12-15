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

public class tourisme {
	public static void main(String[] args) {
		
		
        String datasetURL = "http://localhost:3030/reunion_island";
        String sparqlEndpoint = datasetURL + "/sparql";
        String sparqlUpdate = datasetURL + "/update";
        String graphStore = datasetURL + "/data";
        RDFConnection conneg = RDFConnectionFactory.connect(sparqlEndpoint,sparqlUpdate,graphStore);
		

        String tm ="http://www.thomasandmax.com/";
		String geo ="http://www.w3.org/2003/01/geo/wgs84_pos#";
		String xsd ="http://www.w3.org/2001/XMLSchema#";

		
		// Def variables, and also the filePath.
		String csvFile = "/home/max/MasterCSP/semantic/final_project/circuits-rendonnees-lareunion-wssoubik.csv";
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
    			model.setNsPrefix("geo", geo);
    			model.setNsPrefix("xsd", xsd);
    			model.setNsPrefix("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
    			
    	        // Define generic Resource and Properties
    			Property lattitude = model.createProperty(tm + "lat");
    			Property longitude = model.createProperty(tm + "long");
    			Property duree = model.createProperty(tm + "aDuree");
    			Property distance = model.createProperty(tm + "aDistance");
    			Property denivele = model.createProperty(tm + "aDenivele");
    			Resource Randonnee = model.createResource(tm+"Randonnee");
    			Resource spatialThing = model.createResource(geo + "SpatialThing");
    			Resource LieuTourisme = model.createResource(tm + "LieuTourisme");
    			model.add(Randonnee,RDFS.subClassOf,LieuTourisme);
    			model.add(LieuTourisme,RDFS.subClassOf,spatialThing);
    			
                String[] element = line.split(cvsSplitBy);
                element[1] = element[1].replaceAll(" ", "_");
                element[6] = element[6].replaceAll(" ", "_").toString();

        		Resource current = model.createResource(tm + element[6]);
        		current.addProperty(RDF.type,Randonnee);
        		current.addProperty(duree,model.createTypedLiteral(Integer.parseInt(element[0])));
        		current.addProperty(distance,model.createTypedLiteral(Double.parseDouble(element[8])));
        		current.addProperty(denivele,model.createTypedLiteral(Double.parseDouble(element[10])));
        		String[] coordonnes = element[1].split(",");
        		try {
        		current.addProperty(lattitude,model.createTypedLiteral(Double.parseDouble(coordonnes[0])));
        		current.addProperty(longitude,model.createTypedLiteral(Double.parseDouble(coordonnes[1])));
        		conneg.load(model); // add the content of model to the triplestore
			    } catch (Exception e) {
					current.addProperty(longitude, "0");
				} finally {
				}
//		        
		        
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
