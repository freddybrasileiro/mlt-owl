

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Model;

public class OwlFileUtil {
	//static public OWLOntology getOwlOntology(OntModel ontModel, String owlFileName, OWLOntologyManager manager) throws IOException, OWLOntologyCreationException{
	static public OWLOntology getOwlOntology(OntModel ontModel, String owlFileName, OWLOntologyManager manager) throws IOException, OWLOntologyCreationException{
		return getOwlOntology(ontModel, owlFileName, manager);
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
	
	static public void saveOwlOntology(OntModel ontModel, String owlFileName) throws OWLOntologyCreationException, IOException, OWLOntologyStorageException{
		saveOwlOntology(ontModel, owlFileName);
	}
	
	static public void saveOwlOntology(Model ontModel, String owlFileName) throws OWLOntologyCreationException, IOException, OWLOntologyStorageException{
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
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
	
	
}
