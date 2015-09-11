package br.ufes.inf.nemo.mlt.web.reasoner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.ufes.inf.nemo.mlt.web.reasoner.owl.OwlUtil;
import br.ufes.inf.nemo.mlt.web.reasoner.sparql.MltAxiomsSparqlUtil;
import br.ufes.inf.nemo.mlt.web.reasoner.sparql.MltSparqlUtil;
import br.ufes.inf.nemo.mlt.web.reasoner.sparql.MltTheoremsSparqlUtil;
import br.ufes.inf.nemo.mlt.web.vocabulary.MLT;

import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

public class MltReasonerInferences {
	private OwlUtil owlUtil;
	private List<Statement> stmts = new ArrayList<Statement>();
	private String inferenceLogMsg = "";
	private String duplicatedInferenceLogMsg = "";
	private String logMsg = "";
	
	public String getDuplicatedInferenceLogMsg() {
		duplicatedInferenceLogMsg = "DUPLICATED INFERENCE MESSAGES\n" + duplicatedInferenceLogMsg;
		return duplicatedInferenceLogMsg;
	}
	
	public String getInferenceLogMsg() {
		inferenceLogMsg = "INFERENCE MESSAGES\n" + inferenceLogMsg;
		return inferenceLogMsg;
	}
	
	public String getLogMsg() {
		logMsg = "LOG MESSAGES (Model already knows)" + logMsg;		
		return logMsg;
	}
	
	public MltReasonerInferences(OwlUtil owlUtil) {
		this.owlUtil = owlUtil;
	}
	
	public void addStatement(Statement stmt, String fromAxiom){
		if(stmt == null){
			return;
		}
		String stmtStr = stmt.toString().replace(RDFS.getURI(), "rdfs:").replace(owlUtil.getOwlModelPrefix(), "").replace(OWL.getURI(), "owl:").replace(RDF.getURI(), "rdf:").replace(MLT.getURI(), "mlt:");
		boolean modelKnows = MltSparqlUtil.ask(owlUtil.getOwlModel(), stmt.getSubject(), stmt.getPredicate(), stmt.getObject());
		if(modelKnows){
			logMsg += "Model already knows: " + stmtStr + "\n";
		}else if(stmts.contains(stmt)){
			duplicatedInferenceLogMsg += "We already inferred and now by " + fromAxiom + ": " + stmtStr + "\n";
		}else{
			inferenceLogMsg += "Inferred by " + fromAxiom + ": " + stmtStr + "\n";
			stmts.add(stmt);
		}
	}
	
	public List<Statement> getStatementsByInferences() {
		generateStatementsByA2Inferences();
		generateStatementsByA3Inferences();
		generateStatementsByA4Inferences();
		generateStatementsByA9Inferences();
		generateStatementsByA10Inferences();
		generateStatementsByA11Inferences();
		generateStatementsByA12Inferences();
		generateStatementsByA13Inferences();
		generateStatementsByA14Inferences();
		generateStatementsByA15Inferences();
		
		generateStatementsByT4Inferences();
		generateStatementsByT5Inferences();
		generateStatementsByT6Inferences();
		generateStatementsByT12Inferences();
		generateStatementsByT13Inferences();
		
		return stmts;
	}

	//∀t iof(t,1stOT)<->(∀x iof(x,t)->iof(x,Individual))
	private void generateStatementsByA2Inferences() {
		//A2 going: ->
		List<HashMap<String, String>> a2GoingResults = MltAxiomsSparqlUtil.getA2Going(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : a2GoingResults) {
			String x = hashMap.get("x");
			
			Statement stmt = owlUtil.createIofStatement(x, MLT.TokenIndividual);
			
			addStatement(stmt, "A2");
		}
		
		//A2 returning: <-
		List<HashMap<String, String>> a2ReturningResults = MltAxiomsSparqlUtil.getA2Returning(owlUtil.getOwlModel());
		for (HashMap<String, String> hashMap : a2ReturningResults) {
			String t = hashMap.get("t");
			
			Statement stmt = owlUtil.createIofStatement(t, MLT._1stOrderClass);
			
			addStatement(stmt, "A2");
		}
	}

	//∀t iof(t,2ndOT)<->(∀t′ iof(t′,t)->iof(t′,1stOT))
	private void generateStatementsByA3Inferences() {
		//A3 going: ->
		List<HashMap<String, String>> a3GoingResults = MltAxiomsSparqlUtil.getA3Going(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : a3GoingResults) {
			String t1 = hashMap.get("t1");
			
			Statement stmt = owlUtil.createIofStatement(t1, MLT._1stOrderClass);
			
			addStatement(stmt, "A3");
		}
		
		//A3 returning: <-
		List<HashMap<String, String>> a3ReturningResults = MltAxiomsSparqlUtil.getA3Returning(owlUtil.getOwlModel());
		for (HashMap<String, String> hashMap : a3ReturningResults) {
			String t = hashMap.get("t");
			
			Statement stmt = owlUtil.createIofStatement(t, MLT._2ndOrderClass);
			
			addStatement(stmt, "A3");
		}		
	}

	//∀t iof(t,3rdOT)<->(∀t′ iof(t′,t)->iof(t′,2ndOT))
	private void generateStatementsByA4Inferences() {
		//A4 going: ->
		List<HashMap<String, String>> a4GoingResults = MltAxiomsSparqlUtil.getA4Going(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : a4GoingResults) {
			String t1 = hashMap.get("t1");
			
			Statement stmt = owlUtil.createIofStatement(t1, MLT._2ndOrderClass);
			
			addStatement(stmt, "A4");
		}
		
		//A4 returning: <-
		List<HashMap<String, String>> a4ReturningResults = MltAxiomsSparqlUtil.getA4Returning(owlUtil.getOwlModel());
		for (HashMap<String, String> hashMap : a4ReturningResults) {
			String t = hashMap.get("t");
			
			Statement stmt = owlUtil.createIofStatement(t, MLT._3rdOrderClass);
			
			addStatement(stmt, "A4");
		}	
	}

	//∀ t1,t2 properSpecializes(t1,t2)<->(specializes(t1,t2)∧t1≠t2)
	private void generateStatementsByA9Inferences() {
		//A9 going: ->
		List<HashMap<String, String>> a9GoingResults = MltAxiomsSparqlUtil.getA9Going(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : a9GoingResults) {
			String t1 = hashMap.get("t1");
			String t2 = hashMap.get("t2");
			
			Statement stmt1 = owlUtil.createSubClassOfStatement(t1, t2);
			addStatement(stmt1, "A9");
			
			Statement stmt2 = owlUtil.createDifferentFromStatement(t1, t2);
			addStatement(stmt2, "A9");
		}
		
		//A9 returning: <-
		List<HashMap<String, String>> a9ReturningResults = MltAxiomsSparqlUtil.getA9Returning(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : a9ReturningResults) {
			String t1 = hashMap.get("t1");
			String t2 = hashMap.get("t2");
			
			Statement stmt = owlUtil.createStatement(t1, MLT.properSpecializes, t2);
			addStatement(stmt, "A9");
		}
	}

	//∀t1,t2 isSubordinateTo (t1,t2)<->(¬iof(t1,Individual)∧(∀t3 iof(t3,t1)→(∃t4 iof(t4,t2)∧properSpecializes(t3,t4))))
	private void generateStatementsByA10Inferences() {
		//A10 returning: <-
		List<HashMap<String, String>> a10ReturningResults = MltAxiomsSparqlUtil.getA10Returning(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : a10ReturningResults) {
			String t1 = hashMap.get("t1");
			String t2 = hashMap.get("t2");
			
			Statement stmt = owlUtil.createStatement(t1, MLT.isSubordinatedTo, t2);
			addStatement(stmt, "A10");
		}
	}

	//∀t1,t2 isPowertypeOf(t1,t2)<->(¬iof(t1,Individual)∧(∀t3 iof(t3,t1)<->specializes(t3,t2)))
	private void generateStatementsByA11Inferences() {
		//A11 going 1: ->
		List<HashMap<String, String>> a11GoingResults1 = MltAxiomsSparqlUtil.getA11Going1(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : a11GoingResults1) {
			String t3 = hashMap.get("t3");
			String t2 = hashMap.get("t2");
			
			Statement stmt = owlUtil.createSubClassOfStatement(t3, t2);
			addStatement(stmt, "A11");
		}
		
		//A11 going 2: ->
		List<HashMap<String, String>> a11GoingResults2 = MltAxiomsSparqlUtil.getA11Going2(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : a11GoingResults2) {
			String t3 = hashMap.get("t3");
			String t1 = hashMap.get("t1");
			
			Statement stmt = owlUtil.createIofStatement(t3, t1);
			addStatement(stmt, "A11");
		}
		
		//A11 returning: <-
		List<HashMap<String, String>> a11ReturningResults = MltAxiomsSparqlUtil.getA11Returning(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : a11ReturningResults) {
			String t1 = hashMap.get("t1");
			String t2 = hashMap.get("t2");
//			String t3 = hashMap.get("t3");
			
//			System.out.print(t1.replace(RDFS.getURI(), "").replace(owlUtil.getOwlModelPrefix(), "").replace(OWL.getURI(), "").replace(RDF.getURI(), "").replace(MLT.getURI(), ""));
//			System.out.print("\t");
//			System.out.print(t2.replace(RDFS.getURI(), "").replace(owlUtil.getOwlModelPrefix(), "").replace(OWL.getURI(), "").replace(RDF.getURI(), "").replace(MLT.getURI(), ""));
//			System.out.print("\t");
//			System.out.println(t3.replace(RDFS.getURI(), "").replace(owlUtil.getOwlModelPrefix(), "").replace(OWL.getURI(), "").replace(RDF.getURI(), "").replace(MLT.getURI(), ""));
			Statement stmt = owlUtil.createStatement(t1, MLT.isPowertypeOf, t2);
			addStatement(stmt, "A11");
		}
	}

	//∀t1,t2 characterizes (t1,t2)<->(¬iof(t1,Individual)∧(∀t3 iof(t3,t1)->properSpecializes(t3,t2)))
	private void generateStatementsByA12Inferences() {
		//A12 going: <-
		List<HashMap<String, String>> a12GoingResults = MltAxiomsSparqlUtil.getA12Going(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : a12GoingResults) {
			String t3 = hashMap.get("t3");
			String t2 = hashMap.get("t2");
			
			Statement stmt = owlUtil.createStatement(t3, MLT.properSpecializes, t2);
			addStatement(stmt, "A12");
		}
		
		//A12 returning: <-
		List<HashMap<String, String>> a12ReturningResults = MltAxiomsSparqlUtil.getA12Returning(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : a12ReturningResults) {
			String t1 = hashMap.get("t1");
			String t2 = hashMap.get("t2");
			
			Statement stmt = owlUtil.createStatement(t1, MLT.characterizes, t2);
			addStatement(stmt, "A12");
		}		
	}

	//∀t1,t2 completelyCharacterizes(t1,t2)<->(characterizes(t1,t2)∧(∀e iof(e,t2)->∃t3 (iof(e,t3)∧iof(t3,t1))))
	private void generateStatementsByA13Inferences() {
		//A13 returning: <-
		List<HashMap<String, String>> a13ReturningResults = MltAxiomsSparqlUtil.getA13Returning(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : a13ReturningResults) {
			String t1 = hashMap.get("t1");
			String t2 = hashMap.get("t2");
			
			Statement stmt = owlUtil.createStatement(t1, MLT.completelyCharacterizes, t2);
			addStatement(stmt, "A13");
		}	
	}

	//∀t1,t2 disjointlyCharacterizes (t1,t2)<->(characterizes(t1,t2)∧∀e,t3,t4 ((iof(t3,t1)∧iof(t4,t1)∧iof(e,t3)∧iof(e,t4))->t3=t4)))
	private void generateStatementsByA14Inferences() {
		//A14 going 1: ->
		List<HashMap<String, String>> a14GoingResults1 = MltAxiomsSparqlUtil.getA14Going1(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : a14GoingResults1) {
			String t1 = hashMap.get("t1");
			String t2 = hashMap.get("t2");
			
			Statement stmt = owlUtil.createStatement(t1, MLT.characterizes, t2);
			addStatement(stmt, "A14");
		}
		
		//A14 going 2: ->
		List<HashMap<String, String>> a14GoingResults2 = MltAxiomsSparqlUtil.getA14Going2(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : a14GoingResults2) {
			String t3 = hashMap.get("t3");
			String t4 = hashMap.get("t4");
			
			Statement stmt = owlUtil.createSameAsStatement(t3, t4);
			addStatement(stmt, "A14");
		}
		
		//A14 returning: <-
		List<HashMap<String, String>> a14ReturningResults = MltAxiomsSparqlUtil.getA14Returning(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : a14ReturningResults) {
			String t1 = hashMap.get("t1");
			String t2 = hashMap.get("t2");
			
			Statement stmt = owlUtil.createStatement(t1, MLT.disjointlyCharacterizes, t2);
			addStatement(stmt, "A14");
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
			addStatement(stmt1, "A15");
			
			Statement stmt2 = owlUtil.createStatement(t1, MLT.disjointlyCharacterizes, t2);
			addStatement(stmt2, "A15");
		}
		
		//A15 returning: <-
		List<HashMap<String, String>> a15ReturningResults = MltAxiomsSparqlUtil.getA15Returning(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : a15ReturningResults) {
			String t1 = hashMap.get("t1");
			String t2 = hashMap.get("t2");
			
			Statement stmt = owlUtil.createStatement(t1, MLT.partitions, t2);
			addStatement(stmt, "A15");
		}
	}
	
	//∀t iof(t,1stOT)<->specializes(t,Individual)
	private void generateStatementsByT4Inferences() {
		//T4 going: ->
		List<HashMap<String, String>> t4GoingResults = MltTheoremsSparqlUtil.getT4Going(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : t4GoingResults) {
			String t = hashMap.get("t");
			
			Statement stmt = owlUtil.createSubClassOfStatement(t, MLT.TokenIndividual);
			addStatement(stmt, "T4");
		}
		
		//T4 returning: <-
		List<HashMap<String, String>> t4ReturningResults = MltTheoremsSparqlUtil.getT4Returning(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : t4ReturningResults) {
			String t = hashMap.get("t");
			
			Statement stmt = owlUtil.createIofStatement(t, MLT._1stOrderClass);
			addStatement(stmt, "T4");
		}
	}

	//∀t iof(t,2ndOT)<->specializes(t,1stOT)
	private void generateStatementsByT5Inferences() {
		//T5 going: ->
		List<HashMap<String, String>> t5GoingResults = MltTheoremsSparqlUtil.getT5Going(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : t5GoingResults) {
			String t = hashMap.get("t");
			
			Statement stmt = owlUtil.createSubClassOfStatement(t, MLT._1stOrderClass);
			addStatement(stmt, "T5");
		}
		
		//T5 returning: <-
		List<HashMap<String, String>> t5ReturningResults = MltTheoremsSparqlUtil.getT5Returning(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : t5ReturningResults) {
			String t = hashMap.get("t");
			
			Statement stmt = owlUtil.createIofStatement(t, MLT._2ndOrderClass);
			addStatement(stmt, "T5");
		}
	}

	//∀t iof(t,3rdOT)<->specializes(t,2ndOT)
	private void generateStatementsByT6Inferences() {
		//T6 going: ->
		List<HashMap<String, String>> t6GoingResults = MltTheoremsSparqlUtil.getT6Going(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : t6GoingResults) {
			String t = hashMap.get("t");
			
			Statement stmt = owlUtil.createSubClassOfStatement(t, MLT._2ndOrderClass);
			addStatement(stmt, "T6");
		}
		
		//T6 returning: <-
		List<HashMap<String, String>> t6ReturningResults = MltTheoremsSparqlUtil.getT6Returning(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : t6ReturningResults) {
			String t = hashMap.get("t");
			
			Statement stmt = owlUtil.createIofStatement(t, MLT._3rdOrderClass);
			addStatement(stmt, "T6");
		}
	}

	//∀t1,t2,t3,t4(specializes(t2,t1)∧isPowertypeOf(t4,t2)∧isPowertypeOf(t3,t1))->specializes(t4,t3)
	private void generateStatementsByT12Inferences() {
		//T12 going: ->
		List<HashMap<String, String>> t12GoingResults = MltTheoremsSparqlUtil.getT12Going(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : t12GoingResults) {
			String t4 = hashMap.get("t4");
			String t3 = hashMap.get("t3");
			
			Statement stmt = owlUtil.createSubClassOfStatement(t4, t3);
			addStatement(stmt, "T12");
		}
	}

	//∀t1,t2,t3 (isPowertypeOf(t2,t1)∧characterizes(t3,t1))->properSpecializes(t3,t2)
	private void generateStatementsByT13Inferences() {
		//T13 going: ->
		List<HashMap<String, String>> t13GoingResults = MltTheoremsSparqlUtil.getT13Going(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : t13GoingResults) {
			String t3 = hashMap.get("t3");
			String t2 = hashMap.get("t2");
			
			Statement stmt = owlUtil.createStatement(t3, MLT.properSpecializes, t2);
			addStatement(stmt, "T13");
		}
	}
}
