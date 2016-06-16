package br.ufes.inf.nemo.mlt.web.reasoner.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class NewFileUtil {
	public static void main(String[] args) {
		
		try {
			OntModel jenaModel = createOwlModel("C:\\Users\\Freddy\\Desktop\\teste.owl");
			saveOwlOntology(jenaModel , "outFile.owl", OWLManager.createOWLOntologyManager());
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OWLOntologyStorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	static public OntModel createOwlModel(String owlFileName) throws FileNotFoundException{
		OntModel jenaModel = ModelFactory.createOntologyModel();
		FileInputStream inOwl = new FileInputStream(new File(owlFileName));
		jenaModel.read(inOwl,null, "RDF/XML");
		return jenaModel;
	}
	
	static public OWLOntology getOwlOntology(OntModel ontModel, String owlFileName, OWLOntologyManager manager) throws IOException, OWLOntologyCreationException{
		String syntax = "RDF/XML-ABBREV";
		StringWriter outJenaStringWriter = new StringWriter();
		
		//ontModel is written in outStringWriter
		ontModel.write(outJenaStringWriter, syntax);
		//get outStringWriter as String
		String result = outJenaStringWriter.toString();
		
		//a temp file is created
		File tempJenaFile = new File("resources/output/"+"temp-"+owlFileName);  
		if(tempJenaFile.exists()){
			tempJenaFile.delete();
		}
		//jena is used to write in the file
		FileOutputStream fosJena = new FileOutputStream(tempJenaFile);  
		fosJena.write(result.getBytes());    
		
		//start creating a model using OWL API
		OWLOntology ontology = manager.loadOntologyFromOntologyDocument(tempJenaFile);
		tempJenaFile.delete();
		fosJena.close();
		return ontology;
	}
	
	static public void saveOwlOntology(OntModel ontModel, String owlFileName, OWLOntologyManager manager) throws OWLOntologyCreationException, IOException, OWLOntologyStorageException{
		OWLOntology ontology = getOwlOntology(ontModel, owlFileName, manager);
		
		//a new file is created
		File arquivoOwlApi = new File("resources/output/"+owlFileName);  
		if(arquivoOwlApi.exists()){
			arquivoOwlApi.delete();
		}		
		FileOutputStream fosOwlApi = new FileOutputStream(arquivoOwlApi);
		
		//the OWL Ontology is writen in the FOS
		manager.saveOntology(ontology, fosOwlApi);
		
		fosOwlApi.close();
	}
}
