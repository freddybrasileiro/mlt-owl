import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;


public class OwlUtil {
	private OntModel owlModel;
	
	public OwlUtil(String owlFileName) throws FileNotFoundException {
		createOwlModel(owlFileName);		
	}
	
	public void runInferences(){
		inferHighOrderType();
	}
	
	private void createOwlModel(String owlFileName) throws FileNotFoundException{
		owlModel = ModelFactory.createOntologyModel();
		FileInputStream inOwl = new FileInputStream(new File(owlFileName));
		owlModel.read(inOwl,null);
		owlModel.loadImports();
		
		System.out.println();
	}
	
	private void inferHighOrderType(){
		HashMap<String, String> result = SparqlUtil.inferHighOrderType(owlModel);
		
		System.out.println();
	}
}
