package br.ufes.inf.nemo.mlt.web.reasoner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.ufes.inf.nemo.mlt.web.reasoner.owl.OwlUtil;
import br.ufes.inf.nemo.mlt.web.reasoner.sparql.MltAxiomsSparqlUtil;
import br.ufes.inf.nemo.mlt.web.reasoner.sparql.MltTheoremsSparqlUtil;
import br.ufes.inf.nemo.mlt.web.vocabulary.MLT;

import com.hp.hpl.jena.rdf.model.Statement;

public class MltReasonerInferences {
	private OwlUtil owlUtil;
	List<Statement> stmts = new ArrayList<Statement>();
	
	public MltReasonerInferences(OwlUtil owlUtil) {
		this.owlUtil = owlUtil;
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
			
			stmts.add(stmt);
		}
		
		//A2 returning: <-
		List<HashMap<String, String>> a2ReturningResults = MltAxiomsSparqlUtil.getA2Returning(owlUtil.getOwlModel());
		for (HashMap<String, String> hashMap : a2ReturningResults) {
			String t = hashMap.get("t");
			
			Statement stmt = owlUtil.createIofStatement(t, MLT._1stOrderClass);
			
			stmts.add(stmt);
		}
	}

	//∀t iof(t,2ndOT)<->(∀t′ iof(t′,t)->iof(t′,1stOT))
	private void generateStatementsByA3Inferences() {
		//A3 going: ->
		List<HashMap<String, String>> a3GoingResults = MltAxiomsSparqlUtil.getA3Going(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : a3GoingResults) {
			String t1 = hashMap.get("t1");
			
			Statement stmt = owlUtil.createIofStatement(t1, MLT._1stOrderClass);
			
			stmts.add(stmt);
		}
		
		//A3 returning: <-
		List<HashMap<String, String>> a3ReturningResults = MltAxiomsSparqlUtil.getA3Returning(owlUtil.getOwlModel());
		for (HashMap<String, String> hashMap : a3ReturningResults) {
			String t = hashMap.get("t");
			
			Statement stmt = owlUtil.createIofStatement(t, MLT._2ndOrderClass);
			
			stmts.add(stmt);
		}		
	}

	//∀t iof(t,3rdOT)<->(∀t′ iof(t′,t)->iof(t′,2ndOT))
	private void generateStatementsByA4Inferences() {
		//A4 going: ->
		List<HashMap<String, String>> a4GoingResults = MltAxiomsSparqlUtil.getA4Going(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : a4GoingResults) {
			String t1 = hashMap.get("t1");
			
			Statement stmt = owlUtil.createIofStatement(t1, MLT._2ndOrderClass);
			
			stmts.add(stmt);
		}
		
		//A4 returning: <-
		List<HashMap<String, String>> a4ReturningResults = MltAxiomsSparqlUtil.getA4Returning(owlUtil.getOwlModel());
		for (HashMap<String, String> hashMap : a4ReturningResults) {
			String t = hashMap.get("t");
			
			Statement stmt = owlUtil.createIofStatement(t, MLT._3rdOrderClass);
			
			stmts.add(stmt);
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
			stmts.add(stmt1);
			
			Statement stmt2 = owlUtil.createDifferentFromStatement(t1, t2);
			stmts.add(stmt2);
		}
		
		//A9 returning: <-
		List<HashMap<String, String>> a9ReturningResults = MltAxiomsSparqlUtil.getA9Returning(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : a9ReturningResults) {
			String t1 = hashMap.get("t1");
			String t2 = hashMap.get("t2");
			
			Statement stmt = owlUtil.createStatement(t1, MLT.properSpecializes, t2);
			stmts.add(stmt);
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
			stmts.add(stmt);
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
			stmts.add(stmt);
		}
		
		//A11 going 2: ->
		List<HashMap<String, String>> a11GoingResults2 = MltAxiomsSparqlUtil.getA11Going2(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : a11GoingResults2) {
			String t3 = hashMap.get("t3");
			String t1 = hashMap.get("t1");
			
			Statement stmt = owlUtil.createIofStatement(t3, t1);
			stmts.add(stmt);
		}
		
		//A11 returning: <-
		List<HashMap<String, String>> a11ReturningResults = MltAxiomsSparqlUtil.getA11Returning(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : a11ReturningResults) {
			String t1 = hashMap.get("t1");
			String t2 = hashMap.get("t2");
			
			Statement stmt = owlUtil.createStatement(t1, MLT.isPowertypeOf, t2);
			stmts.add(stmt);
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
			stmts.add(stmt);
		}
		
		//A12 returning: <-
		List<HashMap<String, String>> a12ReturningResults = MltAxiomsSparqlUtil.getA12Returning(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : a12ReturningResults) {
			String t1 = hashMap.get("t1");
			String t2 = hashMap.get("t2");
			
			Statement stmt = owlUtil.createStatement(t1, MLT.characterizes, t2);
			stmts.add(stmt);
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
			stmts.add(stmt);
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
			stmts.add(stmt);
		}
		
		//A14 going 2: ->
		List<HashMap<String, String>> a14GoingResults2 = MltAxiomsSparqlUtil.getA14Going2(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : a14GoingResults2) {
			String t3 = hashMap.get("t3");
			String t4 = hashMap.get("t4");
			
			Statement stmt = owlUtil.createSameAsStatement(t3, t4);
			stmts.add(stmt);
		}
		
		//A14 returning: <-
		List<HashMap<String, String>> a14ReturningResults = MltAxiomsSparqlUtil.getA14Returning(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : a14ReturningResults) {
			String t1 = hashMap.get("t1");
			String t2 = hashMap.get("t2");
			
			Statement stmt = owlUtil.createStatement(t1, MLT.disjointlyCharacterizes, t2);
			stmts.add(stmt);
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
			stmts.add(stmt1);
			
			Statement stmt2 = owlUtil.createStatement(t1, MLT.disjointlyCharacterizes, t2);
			stmts.add(stmt2);
		}
		
		//A15 returning: <-
		List<HashMap<String, String>> a15ReturningResults = MltAxiomsSparqlUtil.getA15Returning(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : a15ReturningResults) {
			String t1 = hashMap.get("t1");
			String t2 = hashMap.get("t2");
			
			Statement stmt = owlUtil.createStatement(t1, MLT.partitions, t2);
			stmts.add(stmt);
		}
	}
	
	//∀t iof(t,1stOT)<->specializes(t,Individual)
	private void generateStatementsByT4Inferences() {
		//T4 going: ->
		List<HashMap<String, String>> t4GoingResults = MltTheoremsSparqlUtil.getT4Going(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : t4GoingResults) {
			String t = hashMap.get("t");
			
			Statement stmt = owlUtil.createSubClassOfStatement(t, MLT.TokenIndividual);
			stmts.add(stmt);
		}
		
		//T4 returning: <-
		List<HashMap<String, String>> t4ReturningResults = MltTheoremsSparqlUtil.getT4Returning(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : t4ReturningResults) {
			String t = hashMap.get("t");
			
			Statement stmt = owlUtil.createIofStatement(t, MLT._1stOrderClass);
			stmts.add(stmt);
		}
	}

	//∀t iof(t,2ndOT)<->specializes(t,1stOT)
	private void generateStatementsByT5Inferences() {
		//T5 going: ->
		List<HashMap<String, String>> t5GoingResults = MltTheoremsSparqlUtil.getT5Going(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : t5GoingResults) {
			String t = hashMap.get("t");
			
			Statement stmt = owlUtil.createSubClassOfStatement(t, MLT._1stOrderClass);
			stmts.add(stmt);
		}
		
		//T5 returning: <-
		List<HashMap<String, String>> t5ReturningResults = MltTheoremsSparqlUtil.getT5Returning(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : t5ReturningResults) {
			String t = hashMap.get("t");
			
			Statement stmt = owlUtil.createIofStatement(t, MLT._2ndOrderClass);
			stmts.add(stmt);
		}
	}

	//∀t iof(t,3rdOT)<->specializes(t,2ndOT)
	private void generateStatementsByT6Inferences() {
		//T6 going: ->
		List<HashMap<String, String>> t6GoingResults = MltTheoremsSparqlUtil.getT6Going(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : t6GoingResults) {
			String t = hashMap.get("t");
			
			Statement stmt = owlUtil.createSubClassOfStatement(t, MLT._2ndOrderClass);
			stmts.add(stmt);
		}
		
		//T6 returning: <-
		List<HashMap<String, String>> t6ReturningResults = MltTheoremsSparqlUtil.getT6Returning(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : t6ReturningResults) {
			String t = hashMap.get("t");
			
			Statement stmt = owlUtil.createIofStatement(t, MLT._3rdOrderClass);
			stmts.add(stmt);
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
			stmts.add(stmt);
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
			stmts.add(stmt);
		}
	}
}
