package br.ufes.inf.nemo.mlt.web.reasoner.owl;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;

import br.ufes.inf.nemo.mlt.web.reasoner.sparql.MltSparqlUtil;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;


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
	
	public String getOwlModelPrefix(){
		return owlModel.getNsPrefixURI("");
	}	
	
	public List<HashMap<String, String>> getBaseAndSubTypesFromDifferentOrders(){
		List<HashMap<String, String>> result = MltSparqlUtil.getBaseAndSubTypesFromDifferentOrders(owlModel);
		return result;
	}
	
	public List<HashMap<String, String>> getHighOrderFromByTransitivity(){
		List<HashMap<String, String>> result = MltSparqlUtil.getHighOrderFromByTransitivity(owlModel);
		return result;
	}

	public void createIof(String instanceURI, String classURI) {
		Resource instance = owlModel.createResource(instanceURI);
		Resource clasS = owlModel.createResource(classURI);
		
		Statement iofStmt = owlModel.createStatement(instance, RDF.type, clasS);
		
		owlModel.add(iofStmt);
	}

	public void createStatements(List<Statement> stmts) {
		owlModel.add(stmts);
	}
	
	public Resource getResource(String uri){
		return owlModel.createResource(uri);
	}

	public Statement createIofStatement(String instance, Resource type) {
		Resource instanceRsrc = getResource(instance);
		
		Statement stmt = owlModel.createStatement(instanceRsrc, RDF.type, type);
		
		return stmt;
	}
	
	public Statement createIofStatement(String instance, String type) {
		Resource typeRsrc = getResource(type);
		return createIofStatement(instance, typeRsrc);
	}

	public Statement createSubClassOfStatement(String t1, String t2) {
		Resource t2Rsrc = getResource(t2);
		
		return createSubClassOfStatement(t1, t2Rsrc);
	}
	
	public Statement createSubClassOfStatement(String t1, Resource t2) {
		Resource t1Rsrc = getResource(t1);
		Statement stmt = owlModel.createStatement(t1Rsrc, RDFS.subClassOf, t2);
		return stmt;
	}

	public Statement createDifferentFromStatement(String t1, String t2) {
		Resource t1Rsrc = getResource(t1);
		Resource t2Rsrc = getResource(t2);
		
		Statement stmt = owlModel.createStatement(t1Rsrc, OWL.differentFrom, t2Rsrc);
		
		return stmt;
	}

	public Statement createStatement(String t1, Property property, String t2) {
		Resource t1Rsrc = getResource(t1);
		Resource t2Rsrc = getResource(t2);
		
		Statement stmt = owlModel.createStatement(t1Rsrc, property, t2Rsrc);
		
		return stmt;
	}

	public Statement createSameAsStatement(String t1, String t2) {
		Resource t1Rsrc = getResource(t1);
		Resource t2Rsrc = getResource(t2);
		
		Statement stmt = owlModel.createStatement(t1Rsrc, OWL.sameAs, t2Rsrc);
		
		return stmt;
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
