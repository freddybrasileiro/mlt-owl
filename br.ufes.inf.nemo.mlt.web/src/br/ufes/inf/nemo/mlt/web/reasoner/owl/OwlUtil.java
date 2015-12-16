package br.ufes.inf.nemo.mlt.web.reasoner.owl;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.jena.iri.IRI;
import org.apache.jena.iri.IRIFactory;

import br.ufes.inf.nemo.mlt.web.reasoner.exceptions.MltException;
import br.ufes.inf.nemo.mlt.web.reasoner.sparql.MltSparqlUtil;
import br.ufes.inf.nemo.mlt.web.vocabulary.MLT;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.reasoner.ValidityReport;
import com.hp.hpl.jena.reasoner.ValidityReport.Report;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;


public class OwlUtil {
	private OntModel owlModel;
	private String logMsg = "";
	private int beginningStmts = 0;
	private int endStmts = 0;
	List<Statement> tinha;
	List<Statement> inclui = new ArrayList<Statement>();
	
	public String getLogMsg() {
		if(!logMsg.isEmpty())
			logMsg = logMsg.replaceAll(RDFS.getURI(), "rdfs:").replaceAll(OWL.getURI(), "owl:").replaceAll(RDF.getURI(), "rdf:").replaceAll(MLT.getURI(), "mlt:").replaceAll(getOwlModelPrefix(), "");
		return logMsg;
	}
	
	public OwlUtil(String owlFileName) throws FileNotFoundException {
		createOwlModel(owlFileName);		
		tinha = owlModel.listStatements().toList();
		beginningStmts = owlModel.listStatements().toList().size();
	}
	
	public void printNoStatements(){
		System.out.println("Triples in the beginning: " + beginningStmts);
		endStmts = owlModel.listStatements().toList().size();
		System.out.println("Triples in the end: " + endStmts);
		System.out.println("New triples: " + (endStmts-beginningStmts));
		
//		List<Statement> diff = owlModel.listStatements().toList();
//		diff.removeAll(tinha);
////		diff.removeAll(inclui);
//		
//		System.out.println("New triples: " + diff.size());
//		for (Statement statement : diff) {
//			System.out.println(statement);
//		}
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

	public void createStatements(List<Statement> stmts) {
		if(!getLogMsg().isEmpty()){
			System.out.println("WARNINGS (Ops... A non well formed uri...)");
			System.out.println(getLogMsg());
		}
		owlModel.add(stmts);
		inclui.addAll(stmts);
	}
	
	public Resource getResource(String uri){
		return owlModel.createResource(uri);
	}

	public Statement createIofStatement(String instance, Resource type) {
		Resource instanceRsrc = getResource(instance);
		
		Statement stmt = createStatement(instanceRsrc, RDF.type, type);
		
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
		Statement stmt = createStatement(t1Rsrc, RDFS.subClassOf, t2);
		return stmt;
	}

	public Statement createDifferentFromStatement(String t1, String t2) {
		Resource t1Rsrc = getResource(t1);
		Resource t2Rsrc = getResource(t2);
		
		Statement stmt = createStatement(t1Rsrc, OWL.differentFrom, t2Rsrc);
		
		return stmt;
	}

	public Statement createStatement(String t1, Property property, String t2) {
		Resource t1Rsrc = getResource(t1);
		Resource t2Rsrc = getResource(t2);
		
		Statement stmt = createStatement(t1Rsrc, property, t2Rsrc);
		
		return stmt;
	}
	
	public boolean validUri(Resource r1){
		IRIFactory factory = IRIFactory.jenaImplementation();
		IRI iri = factory.create( r1.getURI() );
		
        if (iri.hasViolation(false) ) {
        	return false;
        }else{
        	return true;
        }
	}
	
	public Statement createStatement(Resource r1, Property p, Resource r2) {
		Statement stmt = null;
		
		if(validUri(r1) && validUri(p) && validUri(r2)){
			stmt = owlModel.createStatement(r1, p, r2);
		}else{
			logMsg  += "[" + r1.getURI() + ", " + p.getURI() + ", " + r2.getURI() + "]\n";
		}
		return stmt;
	}

	public Statement createSameAsStatement(String t1, String t2) {
		Resource t1Rsrc = getResource(t1);
		Resource t2Rsrc = getResource(t2);
		
		Statement stmt = createStatement(t1Rsrc, OWL.sameAs, t2Rsrc);
		
		return stmt;
	}

	public void validate() throws MltException {
		ValidityReport valReport = owlModel.validate();
		if(valReport.isValid()){
			System.out.println("Valid model.");
			System.out.println();
		}else{
			String reportMsg = "";
			Iterator<Report> reports = valReport.getReports();
			while(reports.hasNext()){
				Report report = reports.next();
				reportMsg += report.description + "\n";
			}
			throw new MltException(reportMsg);
		}
	}
	
//	public void removeMltAxioms(){
//		owlModel.remove(stmts);
//		
//		Resource modelRsrc = owlModel.createResource(owlModel.getNsPrefixURI(""));
//		Resource mltRsrc = owlModel.createResource(MLT.NS);
//		
//		Statement importStmt = createStatement(modelRsrc, OWL.imports, mltRsrc);
//		
//		owlModel.add(importStmt);
//	}
}
