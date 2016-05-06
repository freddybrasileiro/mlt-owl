package wikidataDump;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import com.complexible.stardog.api.Connection;
import com.complexible.stardog.api.ConnectionConfiguration;
import com.complexible.stardog.jena.SDJenaFactory;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
import com.hp.hpl.jena.vocabulary.XSD;

/**
 * <p>
 * Example of how to use the Jena integration with stardog
 * </p>
 *
 * @author Michael Grove
 * @since 0.3.3
 * @version 4.0
 */
public class JenaExample {
	// Using Stardog with the [Jena](http://jena.apache.org) API
	// -------------------
	// In this example we'll show how to use the Stardog Jena API bindings.
	public static void main(String[] args) throws Exception {

		try {
			// Now we open a Connection our new database
			Connection aConn = ConnectionConfiguration.to("wikidata").credentials("admin", "admin")
					.server("http://localhost:5820").connect();

			// Then we obtain a Jena `Model` for the specified stardog
			// database which is backed by our `Connection`
			Model aModel = SDJenaFactory.createModel(aConn);

			Model newModel = ModelFactory.createDefaultModel();
			
			getInstancesOfAbove(aModel, "http://www.wikidata.org/entity/Q427626", newModel);

			saveOwlOntology(newModel, "output.owl", OWLManager.createOWLOntologyManager());
		} finally {
			// You must stop the server when you're done
			// aServer.stop();
		}
	}
	
	public static String owl = "owl";
	public static String rdfs = "rdfs";
	public static String rdf = "rdf";
	public static String xml = "xsd";
	
	public static String PREFIXES = ""
			+ "PREFIX " + owl + ": <" + OWL.getURI() + ">\n"
			+ "PREFIX " + rdfs + ": <" + RDFS.getURI() + ">\n"
			+ "PREFIX " + rdf + ": <" + RDF.getURI() + ">\n"
			+ "PREFIX " + xml + ": <" + XSD.getURI() + ">\n";
	
	public static void getInstancesOfAbove(Model aModel, String rootClassUri, Model newModel){
		//wd:Q427626
		getInstancesOf(aModel, rootClassUri, newModel);
	}
	
	public static void getInstancesOf(Model aModel, String classUri, Model newModel){
		// Query that we will run against the data we just loaded
		String aQueryString = ""
				+ PREFIXES
				//+ "PREFIX wd: <https://www.wikidata.org/entity/>\n"
				+ "select * where { \n"
				+ "	?instance rdf:type <" + classUri + "> . \n"
				+ "} \n";

		ResultSet results = executeQuery(aModel, aQueryString);
		
		ArrayList<String> result = getResultValues(aModel, results, "instance");
		if(classUri.contains("Q7432")){
			System.out.println();
		}
		for (String instanceUri : result) {
			
			System.out.print(".");
			Statement iofStmt = newModel.createStatement(newModel.createResource(instanceUri), RDF.type, newModel.createResource(classUri));
			newModel.add(iofStmt);
			getInstancesOf(aModel, instanceUri, newModel);
		}
		System.out.println();
	}
	
	public static ResultSet executeQuery(Model model, String queryString){
		if(!queryString.contains(PREFIXES)){
			queryString = PREFIXES + "\n" + queryString;
		}
		Query query = QueryFactory.create(queryString); 		
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();
		
		return results;
	}
	
	public static ArrayList<HashMap<String, String>> getResultValues(Model model, ResultSet results, String... vars){
		ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();	
		while (results.hasNext()) 
		{	
			HashMap<String, String> resultRow = new HashMap<String, String>();
			QuerySolution row = results.next();
			for (String var: vars) {
				String varValue = row.get(var).toString();
				resultRow.put(var, varValue);				
			}			
			result.add(resultRow);
		}
		return result;
	}
	
	public static ArrayList<String> getResultValues(Model model, ResultSet results, String var){
		ArrayList<String> result = new ArrayList<String>();	
		while (results.hasNext()) 
		{	
			QuerySolution row = results.next();
			String varValue = row.get(var).toString();			
			result.add(varValue);
		}
		return result;
	}
	
	static public void saveOwlOntology(Model ontModel, String owlFileName, OWLOntologyManager manager) throws OWLOntologyCreationException, IOException, OWLOntologyStorageException{
		OWLOntology ontology = getOwlOntology(ontModel, owlFileName, manager);
		
		//a new file is created
		File arquivoOwlApi = new File(owlFileName);  
		if(arquivoOwlApi.exists()){
			arquivoOwlApi.delete();
		}		
		FileOutputStream fosOwlApi = new FileOutputStream(arquivoOwlApi);
		
		//the OWL Ontology is writen in the FOS
		manager.saveOntology(ontology, fosOwlApi);
		
		fosOwlApi.close();
	}
	
	static public OWLOntology getOwlOntology(Model ontModel, String owlFileName, OWLOntologyManager manager) throws IOException, OWLOntologyCreationException{
		String syntax = "RDF/XML-ABBREV";
		StringWriter outJenaStringWriter = new StringWriter();
		
		//ontModel is written in outStringWriter
		ontModel.write(outJenaStringWriter, syntax);
		//get outStringWriter as String
		String result = outJenaStringWriter.toString();
		
		//a temp file is created
		File tempJenaFile = new File("temp-"+owlFileName);  
		if(tempJenaFile.exists()){
			tempJenaFile.delete();
		}
		//jena is used to write in the file
		FileOutputStream fosJena = new FileOutputStream(tempJenaFile);  
		fosJena.write(result.getBytes());    
		
		//start creating a model using OWL API
		OWLOntology ontology = manager.loadOntologyFromOntologyDocument(tempJenaFile);
		tempJenaFile.delete();
		
		return ontology;
	}
}
