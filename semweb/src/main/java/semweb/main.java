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


public class main {
	// This function parse a CSV file and construct the RDF graph related to it
	public static Model constructGraph() {
		
		// Def variables, and also the filePath.
		String csvFile = "/home/max/MasterCSP/semantic/final_project/communes-millesimeespublic.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ";(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
        String[] element = null;
        
        // Create model that will be returned
        Model model = ModelFactory.createDefaultModel();
        
    	// First we initialize prefixes
		String tm = "http://www.thomasandmax.com/";
		String geo = "http://www.w3.org/2003/01/geo/wgs84_pos#";
		String xsd = "http://www.w3.org/2001/XMLSchema#";
		model.setNsPrefix("tm", tm);
		model.setNsPrefix("geo", geo);
		model.setNsPrefix("xsd", xsd);
		// Prefix initialized.
        
        
        // Define generic Resource and Properties
        Resource current = null;
		Resource spatialThing = model.createResource(geo + "SpatialThing");
		Resource commune = model.createResource(tm + "Commune");
		Property aCodePostal = model.createProperty(tm + "aCodePostal");
		model.add(commune, RDFS.subClassOf, spatialThing);
		// Loop over the csv file, read it line by line and generate the associated RDF triples.
        try {
            br = new BufferedReader(new FileReader(csvFile));
            br.readLine(); // We skip the header line
            while ((line = br.readLine()) != null) {
                // use comma as separator. I specified that we should ignore commas inside a field delimited by ""
            	// with a regular expression that I found on Internet. It is stored in csvSplitBy
                element = line.split(cvsSplitBy);
                element[5] = element[5].replaceAll(" ", "_");
        		current = model.createResource(tm + element[5]);
        		current.addProperty(RDF.type, commune);
        		current.addProperty(aCodePostal, model.createTypedLiteral(Integer.parseInt(element[4])));
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
        
		return model;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		// Generate the model
		Model model = constructGraph();
		
		// Upload the model
		String datasetURL = "http://localhost:3030/reunion_island";
		String sparqlEndpoint = datasetURL + "/sparql";
		String sparqlUpdate = datasetURL + "/update";
		String graphStore = datasetURL + "/data";
		RDFConnection conneg = RDFConnectionFactory.connect(sparqlEndpoint,sparqlUpdate,graphStore);
		conneg.load(model); // add the content of model to the triplestore
		// Write model in the console
		model.write(System.out,"Turtle");
	}
}



