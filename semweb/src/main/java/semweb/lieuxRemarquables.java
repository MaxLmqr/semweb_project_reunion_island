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

public class lieuxRemarquables {
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

		
		// Def variables, and also the filePath.
		String csvFile = "/home/max/MasterCSP/semantic/final_project/lieux-remarquables-lareunion-wssoubik.csv";
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
    			
    			Resource LieuTourisme = model.createResource(tm+"LieuTourisme");
				Resource LieuRemarquable = model.createResource(tm+"LieuRemarquable");
    			Resource spatialThing = model.createResource(geo + "SpatialThing");

				model.add(LieuRemarquable, RDFS.subClassOf, LieuTourisme);
				model.add(LieuTourisme, RDFS.subClassOf, spatialThing);


				Property latitude = model.createProperty(tm+"lat");
				Property longitude = model.createProperty(tm+"long");
				Property aCommune = model.createProperty(tm+"aCommune");
				Property aLieu = model.createProperty(tm + "aLieu");
				Property accessibleMobiliteReduite = model.createProperty(tm+"accessibleMobiliteReduite");

		        // we read the line
		        String[] ligne = line.split(cvsSplitBy);
		        try {
				Resource nom_lieu = model.createResource(tm+ligne[5].replaceAll(" ", "_"));
				Resource commune = model.createResource(tm+ligne[7].replaceAll(" ", "_"));
				commune.addProperty(aLieu, nom_lieu);
				nom_lieu.addProperty(RDF.type, LieuRemarquable);
				String[] coordonnees = ligne[2].split(",");
				
				nom_lieu.addProperty(latitude, model.createTypedLiteral(Double.parseDouble(coordonnees[0])));
				nom_lieu.addProperty(longitude, model.createTypedLiteral(Double.parseDouble(coordonnees[1])));
				nom_lieu.addProperty(aCommune, commune);
				nom_lieu.addProperty(accessibleMobiliteReduite, model.createLiteral(ligne[11]));
				conneg.load(model);
		        } catch (Exception e) {
		        	
		        } finally {}
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
