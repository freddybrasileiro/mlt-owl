package br.ufes.inf.nemo.mlt.web.reasoner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import br.ufes.inf.nemo.mlt.web.reasoner.owl.OwlFileUtil;
import br.ufes.inf.nemo.mlt.web.reasoner.owl.OwlUtil;
import br.ufes.inf.nemo.mlt.web.reasoner.sparql.MltDerivationRulesSparql;
import br.ufes.inf.nemo.mlt.web.reasoner.sparql.MltSparqlUtil;
import br.ufes.inf.nemo.mlt.web.vocabulary.MLT;

import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

public class MltDerivationRules {
	private OwlUtil owlUtil;
	private List<Statement> stmts = new ArrayList<Statement>();
	private String derivationLogMsg = "";
	private String duplicatedDerivationLogMsg = "";
	private String logMsg = "";
	
	public String getDuplicatedDerivationLogMsg() {
		if(!duplicatedDerivationLogMsg.isEmpty()){
			return "DUPLICATED DERIVATION MESSAGES\n" + duplicatedDerivationLogMsg;
		}
		return duplicatedDerivationLogMsg;
	}
	
	public String getDerivationLogMsg() {
		if(!derivationLogMsg.isEmpty()){
			return "DERIVATION MESSAGES\n" + derivationLogMsg;
		}
		return derivationLogMsg;
	}
	
	public String getLogMsg() {
		if(!logMsg.isEmpty()){
			return "LOG MESSAGES\n" + logMsg;
		}
		return logMsg;
	}
	
	public MltDerivationRules(OwlUtil owlUtil) {
		this.owlUtil = owlUtil;
	}
	
	public boolean hasNewStatements(){
//		System.out.println("\tNew Triples: " + stmts.size());
		owlUtil.refreshNewStmts(stmts.size());
		if(stmts.size() > 0) return true;
		return false;
	}
	
	public void addStatement(Statement stmt, String fromAxiom){
		if(stmt == null){
			return;
		}
		String stmtStr = stmt.toString().replace(RDFS.getURI(), "rdfs:").replace(OWL.getURI(), "owl:").replace(RDF.getURI(), "rdf:").replace(MLT.getURI(), "mlt:").replace(owlUtil.getOwlModelPrefix(), "");
		boolean modelKnows = MltSparqlUtil.ask(owlUtil.getOwlModel(), stmt.getSubject(), stmt.getPredicate(), stmt.getObject());
//		if(modelKnows){
//			logMsg += "Model already knows " + fromAxiom + ": " + stmtStr + "\n";
//			modelKnows=modelKnows;
//		}else 
		if(!modelKnows && stmts.contains(stmt)){
			duplicatedDerivationLogMsg += "We already derived and now by " + fromAxiom + ": " + stmtStr + "\n";
		}else if(!modelKnows){
			derivationLogMsg += "Derived using " + fromAxiom + ": " + stmtStr + "\n";
			stmts.add(stmt);
		}
	}
	
	public void createStatementsByDerivations() throws OWLOntologyCreationException, OWLOntologyStorageException, IOException {
		int count = 0;
		do {
			stmts = new ArrayList<Statement>();
			
			generateStatementsByA3Derivations();
			generateStatementsByA4Derivations();
			generateStatementsByA5Derivations();
			generateStatementsByD1Derivations();
			generateStatementsByD4Derivations();
			generateStatementsByD5Derivations();
			generateStatementsByD6Derivations();
			generateStatementsByD7Derivations();
			generateStatementsByD8Derivations();
			generateStatementsByT7Derivations();
			generateStatementsByT8Derivations();
			generateStatementsByT9Derivations();
			generateStatementsByT15Derivations();
			generateStatementsByT16Derivations();
			generateStatementsByT17Derivations();
			
			owlUtil.createStatements(stmts);
			
			count++;
//			System.out.println("Execution "+count);
//			PerformanceUtil.printExecutionTime(begin);
//			long timeInMs = PerformanceUtil.getExecutionTimeInMs(begin);
//			Float timeInMin = Float.valueOf(String.valueOf(timeInMs));
//			timeInMin = (float) (timeInMs/60000.0);
//			timeInMin = (float) (timeInMin/60);
//			System.out.println(timeInMin);
			
			OwlFileUtil.saveOwlOntology(owlUtil.getOwlModel(), "output"+count+".owl");
			
		} while (hasNewStatements());
		System.out.println();
	}

	//∀t iof(t,1stOT)<->(∀x iof(x,t)->iof(x,Individual))
	private void generateStatementsByA3Derivations() {
		//A2 going: ->
		List<HashMap<String, String>> a2GoingResults = MltDerivationRulesSparql.dr1(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : a2GoingResults) {
			String x = hashMap.get("x");
			
			if(x.contains(MLT.getURI())) continue;
			
			Statement stmt = owlUtil.createIofStatement(x, MLT.TokenIndividual);
			
			addStatement(stmt, "DR1 (A3)");
		}
		
		//A2 returning: <-
		List<HashMap<String, String>> a2ReturningResults = MltDerivationRulesSparql.dr2(owlUtil.getOwlModel());
		for (HashMap<String, String> hashMap : a2ReturningResults) {
			String t = hashMap.get("t");
			
			if(t.contains(MLT.getURI())) continue;
			
			Statement stmt = owlUtil.createIofStatement(t, MLT._1stOrderClass);
			
			addStatement(stmt, "DR2 (A3)");
		}
	}

	//∀t iof(t,2ndOT)<->(∀t′ iof(t′,t)->iof(t′,1stOT))
	private void generateStatementsByA4Derivations() {
		//A3 going: ->
		List<HashMap<String, String>> a3GoingResults = MltDerivationRulesSparql.dr3(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : a3GoingResults) {
			String t1 = hashMap.get("t1");
			
			if(t1.contains(MLT.getURI())) continue;
			
			Statement stmt = owlUtil.createIofStatement(t1, MLT._1stOrderClass);
			
			addStatement(stmt, "DR3 (A4)");
		}
		
		//A3 returning: <-
		List<HashMap<String, String>> a3ReturningResults = MltDerivationRulesSparql.dr4(owlUtil.getOwlModel());
		for (HashMap<String, String> hashMap : a3ReturningResults) {
			String t = hashMap.get("t");
			
			if(t.contains(MLT.getURI())) continue;
			
			Statement stmt = owlUtil.createIofStatement(t, MLT._2ndOrderClass);
			
			addStatement(stmt, "DR4 (A5)");
		}		
	}

	//∀t iof(t,3rdOT)<->(∀t′ iof(t′,t)->iof(t′,2ndOT))
	private void generateStatementsByA5Derivations() {
		//A4 going: ->
		List<HashMap<String, String>> a4GoingResults = MltDerivationRulesSparql.dr5(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : a4GoingResults) {
			String t1 = hashMap.get("t1");
			
			if(t1.contains(MLT.getURI())) continue;
			
			Statement stmt = owlUtil.createIofStatement(t1, MLT._2ndOrderClass);
			
			addStatement(stmt, "DR5 (A5)");
		}
		
		//A4 returning: <-
		List<HashMap<String, String>> a4ReturningResults = MltDerivationRulesSparql.dr6(owlUtil.getOwlModel());
		for (HashMap<String, String> hashMap : a4ReturningResults) {
			String t = hashMap.get("t");
			
			if(t.contains(MLT.getURI())) continue;
			
			Statement stmt = owlUtil.createIofStatement(t, MLT._3rdOrderClass);
			
			addStatement(stmt, "DR6 (A5)");
		}	
	}

	//∀t1,t2 specializes(t1,t2)<->(¬iof(t1,Individual)∧¬iof(t2,Individual)∧(∀e iof(e,t1)->iof(e,t2)))
	private void generateStatementsByD1Derivations() {
		//A8 going: ->
		List<HashMap<String, String>> a8GoingResults = MltDerivationRulesSparql.dr7(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : a8GoingResults) {
			String e = hashMap.get("e");
			String t2 = hashMap.get("t2");
			
			if(e.contains(MLT.getURI())) continue;
			
			Statement stmt = owlUtil.createIofStatement(e, t2);
			
			addStatement(stmt, "DR7 (D1)");
		}
	}

	//∀t1,t2 isPowertypeOf(t1,t2)<->(¬iof(t1,Individual)∧(∀t3 iof(t3,t1)<->specializes(t3,t2)))
	private void generateStatementsByD4Derivations() {
		//A11 going 1: ->
		List<HashMap<String, String>> a11GoingResults1 = MltDerivationRulesSparql.dr8(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : a11GoingResults1) {
			String t3 = hashMap.get("t3");
			String t2 = hashMap.get("t2");
			if(t3.contains(MLT.getURI())) continue;
			if(t2.equals(t3)) continue; //ignoring results when t2==t3 
			Statement stmt = owlUtil.createSubClassOfStatement(t3, t2);
			addStatement(stmt, "DR8 (D4)");
		}
		
		//A11 going 2: ->
		List<HashMap<String, String>> a11GoingResults2 = MltDerivationRulesSparql.dr9(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : a11GoingResults2) {
			String t3 = hashMap.get("t3");
			String t1 = hashMap.get("t1");
			if(t3.contains(MLT.getURI())) continue;
			Statement stmt = owlUtil.createIofStatement(t3, t1);
			addStatement(stmt, "DR9 (D5)");
		}
	}

	//∀t1,t2 characterizes (t1,t2)<->(¬iof(t1,Individual)∧(∀t3 iof(t3,t1)->properSpecializes(t3,t2)))
	private void generateStatementsByD5Derivations() {
		//A12 going: <-
		List<HashMap<String, String>> a12GoingResults = MltDerivationRulesSparql.dr10(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : a12GoingResults) {
			String t1 = hashMap.get("t1");
			Resource t1AsResource = owlUtil.getOwlModel().createResource(t1);
			List<Statement> _1stOT = owlUtil.getOwlModel().listStatements(t1AsResource, RDF.type, MLT._1stOrderClass).toList();
			if(_1stOT.size() > 0) continue;
			
			String t3 = hashMap.get("t3");
			String t2 = hashMap.get("t2");
			
			if(t3.contains(MLT.getURI())) continue;
			
			Statement stmt = owlUtil.createSubClassOfStatement(t3, t2);
			addStatement(stmt, "DR10 (D5)");
		}		
	}

	//∀t1,t2 completelyCharacterizes(t1,t2)<->(characterizes(t1,t2)∧(∀e iof(e,t2)->∃t3 (iof(e,t3)∧iof(t3,t1))))
	private void generateStatementsByD6Derivations() {
		//A14 going 1: ->
		List<HashMap<String, String>> a13GoingResults = MltDerivationRulesSparql.dr11(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : a13GoingResults) {
			String t1 = hashMap.get("t1");
			String t2 = hashMap.get("t2");
			
			if(t1.contains(MLT.getURI())) continue;
			
			Statement stmt = owlUtil.createStatement(t1, MLT.characterizes, t2);
			addStatement(stmt, "DR11 (D6)");
		}
	}

	//∀t1,t2 disjointlyCharacterizes (t1,t2)<->(characterizes(t1,t2)∧∀e,t3,t4 ((iof(t3,t1)∧iof(t4,t1)∧iof(e,t3)∧iof(e,t4))->t3=t4)))
	private void generateStatementsByD7Derivations() {
		//A14 going 1: ->
		List<HashMap<String, String>> a14GoingResults = MltDerivationRulesSparql.dr12(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : a14GoingResults) {
			String t1 = hashMap.get("t1");
			String t2 = hashMap.get("t2");
			
			if(t1.contains(MLT.getURI())) continue;
			
			Statement stmt = owlUtil.createStatement(t1, MLT.characterizes, t2);
			addStatement(stmt, "DR12 (D7)");
		}
	}

	//∀t1,t2 partitions(t1,t2)<->(completelyCharacterizes(t1,t2)∧disjointlyCharacterizes(t1,t2))
	private void generateStatementsByD8Derivations() {
		//A15 going: ->
		List<HashMap<String, String>> a15GoingResults = MltDerivationRulesSparql.dr13(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : a15GoingResults) {
			String t1 = hashMap.get("t1");
			String t2 = hashMap.get("t2");
			
			if(t1.contains(MLT.getURI())) continue;
			
			Statement stmt1 = owlUtil.createStatement(t1, MLT.completelyCharacterizes, t2);
			addStatement(stmt1, "DR13 (D8)");
			
			Statement stmt2 = owlUtil.createStatement(t1, MLT.disjointlyCharacterizes, t2);
			addStatement(stmt2, "DR13 (D8)");
		}
		
		//A15 returning: <-
		List<HashMap<String, String>> a15ReturningResults = MltDerivationRulesSparql.dr14(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : a15ReturningResults) {
			String t1 = hashMap.get("t1");
			String t2 = hashMap.get("t2");
			
			if(t1.contains(MLT.getURI())) continue;
			
			Statement stmt = owlUtil.createStatement(t1, MLT.partitions, t2);
			addStatement(stmt, "DR14 (D8)");
		}
	}
	
	//∀t iof(t,1stOT)<->specializes(t,Individual)
	private void generateStatementsByT7Derivations() {
		//T4 returning: <-
		List<HashMap<String, String>> t4ReturningResults = MltDerivationRulesSparql.dr15(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : t4ReturningResults) {
			String t = hashMap.get("t");
			if(t.contains(MLT.getURI())) continue;
			if(t.equals(MLT._1stOrderClass.toString())) continue; //ignoring results when t==MLT._1stOrderClass
			Statement stmt = owlUtil.createIofStatement(t, MLT._1stOrderClass);
			addStatement(stmt, "DR15 (T7)");
		}
				
		//T4 going: ->
		List<HashMap<String, String>> t4GoingResults = MltDerivationRulesSparql.dr16(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : t4GoingResults) {
			String t = hashMap.get("t");
			if(t.contains(MLT.getURI())) continue;
			if(t.equals(MLT.TokenIndividual.toString())) continue; //ignoring results when t==MLT.TokenIndividual
			Statement stmt = owlUtil.createSubClassOfStatement(t, MLT.TokenIndividual);
			addStatement(stmt, "DR16 (T7)");
		}		
	}

	//∀t iof(t,2ndOT)<->specializes(t,1stOT)
	private void generateStatementsByT8Derivations() {
		//T5 returning: <-
		List<HashMap<String, String>> t5ReturningResults = MltDerivationRulesSparql.dr17(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : t5ReturningResults) {
			String t = hashMap.get("t");
			if(t.contains(MLT.getURI())) continue;
			if(t.equals(MLT._2ndOrderClass.toString())) continue; //ignoring results when t==MLT._2ndOrderClass
			Statement stmt = owlUtil.createIofStatement(t, MLT._2ndOrderClass);
			addStatement(stmt, "DR17 (T8)");
		}
	
		//T5 going: ->
		List<HashMap<String, String>> t5GoingResults = MltDerivationRulesSparql.dr18(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : t5GoingResults) {
			String t = hashMap.get("t");
			if(t.contains(MLT.getURI())) continue;
			if(t.equals(MLT._1stOrderClass.toString())) continue; //ignoring results when t==MLT._1stOrderClass
			Statement stmt = owlUtil.createSubClassOfStatement(t, MLT._1stOrderClass);
			addStatement(stmt, "DR18 (T9)");
		}
		
		
	}

	//∀t iof(t,3rdOT)<->specializes(t,2ndOT)
	private void generateStatementsByT9Derivations() {		
		//T6 returning: <-
		List<HashMap<String, String>> t6ReturningResults = MltDerivationRulesSparql.dr19(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : t6ReturningResults) {
			String t = hashMap.get("t");
			if(t.contains(MLT.getURI())) continue;
			if(t.equals(MLT._3rdOrderClass.toString())) continue; //ignoring results when t==MLT._3rdOrderClass
			Statement stmt = owlUtil.createIofStatement(t, MLT._3rdOrderClass);
			addStatement(stmt, "DR19 (T9)");
		}
		//T6 going: ->
		List<HashMap<String, String>> t6GoingResults = MltDerivationRulesSparql.dr20(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : t6GoingResults) {
			String t = hashMap.get("t");
			if(t.contains(MLT.getURI())) continue;
			if(t.equals(MLT._2ndOrderClass.toString())) continue; //ignoring results when t==MLT._2ndOrderClass
			Statement stmt = owlUtil.createSubClassOfStatement(t, MLT._2ndOrderClass);
			addStatement(stmt, "DR20 (T10)");
		}
	}

	//∀t1,t2,t3,t4(specializes(t2,t1)∧isPowertypeOf(t4,t2)∧isPowertypeOf(t3,t1))->specializes(t4,t3)
	private void generateStatementsByT15Derivations() {
		//T12 going: ->
		List<HashMap<String, String>> t12GoingResults = MltDerivationRulesSparql.dr21(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : t12GoingResults) {
			String t4 = hashMap.get("t4");
			String t3 = hashMap.get("t3");
			if(t4.contains(MLT.getURI())) continue;
			if(t4.equals(t3)) continue; //ignoring results when t3==t4
			Statement stmt = owlUtil.createSubClassOfStatement(t4, t3);
			addStatement(stmt, "DR21 (T15)");
		}
	}

	private void generateStatementsByT16Derivations() {
		//T13 going: ->
		List<HashMap<String, String>> t16GoingResults = MltDerivationRulesSparql.dr22(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : t16GoingResults) {
			String t1 = hashMap.get("t1");
			String t3 = hashMap.get("t3");
			if(t3.contains(MLT.getURI())) continue;
			if(t1.equals(t3)) continue; //ignoring results when t2==t3 
			Statement stmt = owlUtil.createStatement(t1, MLT.characterizes, t3);
			addStatement(stmt, "DR22 (T16)");
		}
	}
	
	//∀t1,t2,t3 (isPowertypeOf(t2,t1)∧characterizes(t3,t1))->properSpecializes(t3,t2)
	private void generateStatementsByT17Derivations() {
		//T13 going: ->
		List<HashMap<String, String>> t13GoingResults = MltDerivationRulesSparql.dr23(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : t13GoingResults) {
			String t3 = hashMap.get("t3");
			String t2 = hashMap.get("t2");
			if(t3.contains(MLT.getURI())) continue;
			if(t2.equals(t3)) continue; //ignoring results when t2==t3 
			Statement stmt = owlUtil.createSubClassOfStatement(t3, t2);
			addStatement(stmt, "DR23 (T17)");
		}
	}
}
