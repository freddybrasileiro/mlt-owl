import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.vocabulary.RDF;


public class OwlUtil {
	private OntModel owlModel;
//	private List<Statement> stmts;
	
	public OwlUtil(String owlFileName) throws FileNotFoundException {
		createOwlModel(owlFileName);		
	}
	
	public OwlUtil(OntModel owlModel) {
		this.owlModel = owlModel;
	}
	
	private void createOwlModel(String owlFileName) throws FileNotFoundException{
		owlModel = ModelFactory.createOntologyModel();
		FileInputStream inOwl = new FileInputStream(new File(owlFileName));
		owlModel.read(inOwl,null, "RDF/XML");
		
//		owlModel.loadImports();
		
//		OntModel mltModel = ModelFactory.createOntologyModel();
//		FileInputStream inMlt = new FileInputStream(new File("resources/examples/mlt.owl"));
//		mltModel.read(inMlt,null);
//		
//		stmts = mltModel.listStatements().toList();
//		for (int i = stmts.size()-1; i >= 0; i--) {
//			Resource subj = stmts.get(i).getSubject();
//			String subjNs = subj.getNameSpace();
//			if(subjNs == null || !subjNs.contains(MLT.NS) || subjNs.equals(MLT.NS)){
//				stmts.remove(stmts.get(i));
//			}
//		}
//		owlModel.add(stmts);
	}
	
	public OntModel getOwlModel() {
		return owlModel;
	}
	
	public List<HashMap<String, String>> getBaseAndSubTypesFromDifferentOrders(){
		List<HashMap<String, String>> result = SparqlUtil.getBaseAndSubTypesFromDifferentOrders(owlModel);
		return result;
	}
	
	public List<HashMap<String, String>> getHighOrderFromByTransitivity(){
		List<HashMap<String, String>> result = SparqlUtil.getHighOrderFromByTransitivity(owlModel);
		return result;
	}

	public void createIof(String instanceURI, String classURI) {
		Resource instance = owlModel.createResource(instanceURI);
		Resource clasS = owlModel.createResource(classURI);
		
		Statement iofStmt = owlModel.createStatement(instance, RDF.type, clasS);
		
		owlModel.add(iofStmt);
	}
	
//	public void removeMltAxioms(){
//		owlModel.remove(stmts);
//		
//		Resource modelRsrc = owlModel.createResource(owlModel.getNsPrefixURI(""));
//		Resource mltRsrc = owlModel.createResource(MLT.NS);
//		
//		Statement importStmt = owlModel.createStatement(modelRsrc, OWL.imports, mltRsrc);
//		
//		owlModel.add(importStmt);
//	}
}
