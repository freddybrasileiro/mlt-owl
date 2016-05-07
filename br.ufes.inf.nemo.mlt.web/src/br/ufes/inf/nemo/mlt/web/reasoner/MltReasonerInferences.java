package br.ufes.inf.nemo.mlt.web.reasoner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

import br.ufes.inf.nemo.mlt.web.reasoner.owl.OwlFileUtil;
import br.ufes.inf.nemo.mlt.web.reasoner.owl.OwlUtil;
import br.ufes.inf.nemo.mlt.web.reasoner.sparql.MltAxiomsSparqlUtil;
import br.ufes.inf.nemo.mlt.web.reasoner.sparql.MltSparqlUtil;
import br.ufes.inf.nemo.mlt.web.reasoner.sparql.MltTheoremsSparqlUtil;
import br.ufes.inf.nemo.mlt.web.reasoner.util.PerformanceUtil;
import br.ufes.inf.nemo.mlt.web.vocabulary.MLT;

public class MltReasonerInferences {
	private OwlUtil owlUtil;
	private List<Statement> stmts = new ArrayList<Statement>();
	private String inferenceLogMsg = "";
	private String duplicatedInferenceLogMsg = "";
	private String logMsg = "";
	
	public String getDuplicatedInferenceLogMsg() {
		if(!duplicatedInferenceLogMsg.isEmpty()){
			return "DUPLICATED INFERENCE MESSAGES\n" + duplicatedInferenceLogMsg;
		}
		return duplicatedInferenceLogMsg;
	}
	
	public String getInferenceLogMsg() {
		if(!inferenceLogMsg.isEmpty()){
			return "INFERENCE MESSAGES\n" + inferenceLogMsg;
		}
		return inferenceLogMsg;
	}
	
	public String getLogMsg() {
		if(!logMsg.isEmpty()){
			return "LOG MESSAGES\n" + logMsg;
		}
		return logMsg;
	}
	
	public MltReasonerInferences(OwlUtil owlUtil) {
		this.owlUtil = owlUtil;
	}
	
	public boolean hasNewStatements(){
		System.out.println("\tNew Triples: " + stmts.size());
		if(stmts.size() > 0) return true;
		return false;
	}
	
	public void addStatement(Statement stmt, String fromAxiom){
		if(stmt == null){
			return;
		}
		String stmtStr = stmt.toString().replace(RDFS.getURI(), "rdfs:").replace(OWL.getURI(), "owl:").replace(RDF.getURI(), "rdf:").replace(MLT.getURI(), "mlt:").replace(owlUtil.getOwlModelPrefix(), "");
		boolean modelKnows = MltSparqlUtil.ask(owlUtil.getOwlModel(), stmt.getSubject(), stmt.getPredicate(), stmt.getObject());
		if(modelKnows){
//			logMsg += "Model already knows " + fromAxiom + ": " + stmtStr + "\n";
			modelKnows=modelKnows;
		}else if(stmts.contains(stmt)){
			duplicatedInferenceLogMsg += "We already inferred and now by " + fromAxiom + ": " + stmtStr + "\n";
		}else{
			inferenceLogMsg += "Inferred by " + fromAxiom + ": " + stmtStr + "\n";
			stmts.add(stmt);
		}
	}
	
	public void createStatementsByInferences() throws OWLOntologyCreationException, OWLOntologyStorageException, IOException {
		int count = 0;
		do {
			Date begin = new Date();
			stmts = new ArrayList<Statement>();
			
			System.out.println("A2");
			generateStatementsByA2Inferences();
			PerformanceUtil.printExecutionTime(begin);
			Date dt = new Date();
			System.out.println("A3");
			generateStatementsByA3Inferences();
			PerformanceUtil.printExecutionTime(dt);
			dt = new Date();
			System.out.println("A4");
			generateStatementsByA4Inferences();
			PerformanceUtil.printExecutionTime(dt);
			dt = new Date();
			System.out.println("A8");
			generateStatementsByA8Inferences();
			PerformanceUtil.printExecutionTime(dt);
			dt = new Date();
			System.out.println("A11");
			generateStatementsByA11Inferences();
			PerformanceUtil.printExecutionTime(dt);
			dt = new Date();
			System.out.println("A12");
			generateStatementsByA12Inferences();
			PerformanceUtil.printExecutionTime(dt);
			dt = new Date();
			System.out.println("A13");
			generateStatementsByA13Inferences();
			PerformanceUtil.printExecutionTime(dt);
			dt = new Date();
			System.out.println("A14");
			generateStatementsByA14Inferences();
			PerformanceUtil.printExecutionTime(dt);
			dt = new Date();
			System.out.println("A15");
			generateStatementsByA15Inferences();
			
			PerformanceUtil.printExecutionTime(dt);
			dt = new Date();
			System.out.println("T4");
			generateStatementsByT4Inferences();
			PerformanceUtil.printExecutionTime(dt);
			dt = new Date();
			System.out.println("T5");
			generateStatementsByT5Inferences();
			PerformanceUtil.printExecutionTime(dt);
			dt = new Date();
			System.out.println("T6");
			generateStatementsByT6Inferences();
			PerformanceUtil.printExecutionTime(dt);
			dt = new Date();
			System.out.println("T12");
			generateStatementsByT12Inferences();
			PerformanceUtil.printExecutionTime(dt);
			dt = new Date();
			System.out.println("T13");
			generateStatementsByT13Inferences();
			PerformanceUtil.printExecutionTime(dt);
			
			owlUtil.createStatements(stmts);
			
			count++;
			System.out.println("Execution "+count);
			PerformanceUtil.printExecutionTime(begin);
			long timeInMs = PerformanceUtil.getExecutionTimeInMs(begin);
			Float timeInMin = Float.valueOf(String.valueOf(timeInMs));
			timeInMin = (float) (timeInMs/60000.0);
//			timeInMin = (float) (timeInMin/60);
			System.out.println(timeInMin);
			
			OwlFileUtil.saveOwlOntology(owlUtil.getOwlModel(), "output"+count+".owl");
			
		} while (hasNewStatements());
		System.out.println();
	}

	//∀t iof(t,1stOT)<->(∀x iof(x,t)->iof(x,Individual))
	private void generateStatementsByA2Inferences() {
		//A2 going: ->
		List<HashMap<String, String>> a2GoingResults = MltAxiomsSparqlUtil.getA2Going(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : a2GoingResults) {
			String x = hashMap.get("x");
			
			Statement stmt = owlUtil.createIofStatement(x, MLT.TokenIndividual);
			
			addStatement(stmt, "A2->");
		}
		
		//A2 returning: <-
		List<HashMap<String, String>> a2ReturningResults = MltAxiomsSparqlUtil.getA2Returning(owlUtil.getOwlModel());
		for (HashMap<String, String> hashMap : a2ReturningResults) {
			String t = hashMap.get("t");
			
			Statement stmt = owlUtil.createIofStatement(t, MLT._1stOrderClass);
			
			addStatement(stmt, "A2<-");
		}
	}

	//∀t iof(t,2ndOT)<->(∀t′ iof(t′,t)->iof(t′,1stOT))
	private void generateStatementsByA3Inferences() {
		//A3 going: ->
		List<HashMap<String, String>> a3GoingResults = MltAxiomsSparqlUtil.getA3Going(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : a3GoingResults) {
			String t1 = hashMap.get("t1");
			
			Statement stmt = owlUtil.createIofStatement(t1, MLT._1stOrderClass);
			
			addStatement(stmt, "A3->");
		}
		
		//A3 returning: <-
		List<HashMap<String, String>> a3ReturningResults = MltAxiomsSparqlUtil.getA3Returning(owlUtil.getOwlModel());
		for (HashMap<String, String> hashMap : a3ReturningResults) {
			String t = hashMap.get("t");
			
			Statement stmt = owlUtil.createIofStatement(t, MLT._2ndOrderClass);
			
			addStatement(stmt, "A3<-");
		}		
	}

	//∀t iof(t,3rdOT)<->(∀t′ iof(t′,t)->iof(t′,2ndOT))
	private void generateStatementsByA4Inferences() {
		//A4 going: ->
		List<HashMap<String, String>> a4GoingResults = MltAxiomsSparqlUtil.getA4Going(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : a4GoingResults) {
			String t1 = hashMap.get("t1");
			
			Statement stmt = owlUtil.createIofStatement(t1, MLT._2ndOrderClass);
			
			addStatement(stmt, "A4->");
		}
		
		//A4 returning: <-
		List<HashMap<String, String>> a4ReturningResults = MltAxiomsSparqlUtil.getA4Returning(owlUtil.getOwlModel());
		for (HashMap<String, String> hashMap : a4ReturningResults) {
			String t = hashMap.get("t");
			
			Statement stmt = owlUtil.createIofStatement(t, MLT._3rdOrderClass);
			
			addStatement(stmt, "A4<-");
		}	
	}

	//∀t1,t2 specializes(t1,t2)<->(¬iof(t1,Individual)∧¬iof(t2,Individual)∧(∀e iof(e,t1)->iof(e,t2)))
	private void generateStatementsByA8Inferences() {
		//A8 going: ->
		List<HashMap<String, String>> a8GoingResults = MltAxiomsSparqlUtil.getA8Going(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : a8GoingResults) {
			String e = hashMap.get("e");
			String t2 = hashMap.get("t2");
			
			Statement stmt = owlUtil.createIofStatement(e, t2);
			
			addStatement(stmt, "A8->");
		}
	}

	//∀t1,t2 isPowertypeOf(t1,t2)<->(¬iof(t1,Individual)∧(∀t3 iof(t3,t1)<->specializes(t3,t2)))
	private void generateStatementsByA11Inferences() {
		//A11 going 1: ->
		List<HashMap<String, String>> a11GoingResults1 = MltAxiomsSparqlUtil.getA11Going1(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : a11GoingResults1) {
			String t3 = hashMap.get("t3");
			String t2 = hashMap.get("t2");
			if(t2.equals(t3)) continue; //ignoring results when t2==t3 
			Statement stmt = owlUtil.createSubClassOfStatement(t3, t2);
			addStatement(stmt, "A11->1");
		}
		
		//A11 going 2: ->
		List<HashMap<String, String>> a11GoingResults2 = MltAxiomsSparqlUtil.getA11Going2(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : a11GoingResults2) {
			String t3 = hashMap.get("t3");
			String t1 = hashMap.get("t1");
			
			Statement stmt = owlUtil.createIofStatement(t3, t1);
			addStatement(stmt, "A11->2");
		}
	}

	//∀t1,t2 characterizes (t1,t2)<->(¬iof(t1,Individual)∧(∀t3 iof(t3,t1)->properSpecializes(t3,t2)))
	private void generateStatementsByA12Inferences() {
		//A12 going: <-
		List<HashMap<String, String>> a12GoingResults = MltAxiomsSparqlUtil.getA12Going(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : a12GoingResults) {
			String t1 = hashMap.get("t1");
			Resource t1AsResource = owlUtil.getOwlModel().createResource(t1);
			List<Statement> _1stOT = owlUtil.getOwlModel().listStatements(t1AsResource, RDF.type, MLT._1stOrderClass).toList();
			if(_1stOT.size() > 0) continue;
			
			String t3 = hashMap.get("t3");
			String t2 = hashMap.get("t2");
			
			Statement stmt = owlUtil.createSubClassOfStatement(t3, t2);
			addStatement(stmt, "A12->");
		}		
	}

	//∀t1,t2 completelyCharacterizes(t1,t2)<->(characterizes(t1,t2)∧(∀e iof(e,t2)->∃t3 (iof(e,t3)∧iof(t3,t1))))
	private void generateStatementsByA13Inferences() {
		//A14 going 1: ->
		List<HashMap<String, String>> a13GoingResults = MltAxiomsSparqlUtil.getA13Going(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : a13GoingResults) {
			String t1 = hashMap.get("t1");
			String t2 = hashMap.get("t2");
			
			Statement stmt = owlUtil.createStatement(t1, MLT.characterizes, t2);
			addStatement(stmt, "A14->1");
		}
	}

	//∀t1,t2 disjointlyCharacterizes (t1,t2)<->(characterizes(t1,t2)∧∀e,t3,t4 ((iof(t3,t1)∧iof(t4,t1)∧iof(e,t3)∧iof(e,t4))->t3=t4)))
	private void generateStatementsByA14Inferences() {
		//A14 going 1: ->
		List<HashMap<String, String>> a14GoingResults = MltAxiomsSparqlUtil.getA14Going(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : a14GoingResults) {
			String t1 = hashMap.get("t1");
			String t2 = hashMap.get("t2");
			
			Statement stmt = owlUtil.createStatement(t1, MLT.characterizes, t2);
			addStatement(stmt, "A14->1");
		}
	}

	//∀t1,t2 partitions(t1,t2)<->(completelyCharacterizes(t1,t2)∧disjointlyCharacterizes(t1,t2))
	private void generateStatementsByA15Inferences() {
		//A15 going: ->
		List<HashMap<String, String>> a15GoingResults = MltAxiomsSparqlUtil.getA15Going(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : a15GoingResults) {
			String t1 = hashMap.get("t1");
			String t2 = hashMap.get("t2");
			
			Statement stmt1 = owlUtil.createStatement(t1, MLT.completelyCharacterizes, t2);
			addStatement(stmt1, "A15->");
			
			Statement stmt2 = owlUtil.createStatement(t1, MLT.disjointlyCharacterizes, t2);
			addStatement(stmt2, "A15->");
		}
		
		//A15 returning: <-
		List<HashMap<String, String>> a15ReturningResults = MltAxiomsSparqlUtil.getA15Returning(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : a15ReturningResults) {
			String t1 = hashMap.get("t1");
			String t2 = hashMap.get("t2");
			
			Statement stmt = owlUtil.createStatement(t1, MLT.partitions, t2);
			addStatement(stmt, "A15<-");
		}
	}
	
	//∀t iof(t,1stOT)<->specializes(t,Individual)
	private void generateStatementsByT4Inferences() {
		//T4 going: ->
		List<HashMap<String, String>> t4GoingResults = MltTheoremsSparqlUtil.getT4Going(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : t4GoingResults) {
			String t = hashMap.get("t");
			if(t.equals(MLT.TokenIndividual.toString())) continue; //ignoring results when t==MLT.TokenIndividual
			Statement stmt = owlUtil.createSubClassOfStatement(t, MLT.TokenIndividual);
			addStatement(stmt, "T4->");
		}
		
		//T4 returning: <-
		List<HashMap<String, String>> t4ReturningResults = MltTheoremsSparqlUtil.getT4Returning(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : t4ReturningResults) {
			String t = hashMap.get("t");
			if(t.equals(MLT._1stOrderClass.toString())) continue; //ignoring results when t==MLT._1stOrderClass
			Statement stmt = owlUtil.createIofStatement(t, MLT._1stOrderClass);
			addStatement(stmt, "T4<-");
		}
	}

	//∀t iof(t,2ndOT)<->specializes(t,1stOT)
	private void generateStatementsByT5Inferences() {
		//T5 going: ->
		List<HashMap<String, String>> t5GoingResults = MltTheoremsSparqlUtil.getT5Going(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : t5GoingResults) {
			String t = hashMap.get("t");
			if(t.equals(MLT._1stOrderClass.toString())) continue; //ignoring results when t==MLT._1stOrderClass
			Statement stmt = owlUtil.createSubClassOfStatement(t, MLT._1stOrderClass);
			addStatement(stmt, "T5->");
		}
		
		//T5 returning: <-
		List<HashMap<String, String>> t5ReturningResults = MltTheoremsSparqlUtil.getT5Returning(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : t5ReturningResults) {
			String t = hashMap.get("t");
			if(t.equals(MLT._2ndOrderClass.toString())) continue; //ignoring results when t==MLT._2ndOrderClass
			Statement stmt = owlUtil.createIofStatement(t, MLT._2ndOrderClass);
			addStatement(stmt, "T5<-");
		}
	}

	//∀t iof(t,3rdOT)<->specializes(t,2ndOT)
	private void generateStatementsByT6Inferences() {
		//T6 going: ->
		List<HashMap<String, String>> t6GoingResults = MltTheoremsSparqlUtil.getT6Going(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : t6GoingResults) {
			String t = hashMap.get("t");
			if(t.equals(MLT._2ndOrderClass.toString())) continue; //ignoring results when t==MLT._2ndOrderClass
			Statement stmt = owlUtil.createSubClassOfStatement(t, MLT._2ndOrderClass);
			addStatement(stmt, "T6->");
		}
		
		//T6 returning: <-
		List<HashMap<String, String>> t6ReturningResults = MltTheoremsSparqlUtil.getT6Returning(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : t6ReturningResults) {
			String t = hashMap.get("t");
			if(t.equals(MLT._3rdOrderClass.toString())) continue; //ignoring results when t==MLT._3rdOrderClass
			Statement stmt = owlUtil.createIofStatement(t, MLT._3rdOrderClass);
			addStatement(stmt, "T6<-");
		}
	}

	//∀t1,t2,t3,t4(specializes(t2,t1)∧isPowertypeOf(t4,t2)∧isPowertypeOf(t3,t1))->specializes(t4,t3)
	private void generateStatementsByT12Inferences() {
		//T12 going: ->
		List<HashMap<String, String>> t12GoingResults = MltTheoremsSparqlUtil.getT12Going(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : t12GoingResults) {
			String t4 = hashMap.get("t4");
			String t3 = hashMap.get("t3");
			if(t4.equals(t3)) continue; //ignoring results when t3==t4
			Statement stmt = owlUtil.createSubClassOfStatement(t4, t3);
			addStatement(stmt, "T12->");
		}
	}

	//∀t1,t2,t3 (isPowertypeOf(t2,t1)∧characterizes(t3,t1))->properSpecializes(t3,t2)
	private void generateStatementsByT13Inferences() {
		//T13 going: ->
		List<HashMap<String, String>> t13GoingResults = MltTheoremsSparqlUtil.getT13Going(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : t13GoingResults) {
			String t3 = hashMap.get("t3");
			String t2 = hashMap.get("t2");
			if(t2.equals(t3)) continue; //ignoring results when t2==t3 
			Statement stmt = owlUtil.createSubClassOfStatement(t3, t2);
			addStatement(stmt, "T13->");
		}
	}
}
