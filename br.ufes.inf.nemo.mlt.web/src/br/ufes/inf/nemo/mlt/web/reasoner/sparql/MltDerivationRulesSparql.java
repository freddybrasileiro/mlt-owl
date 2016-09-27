package br.ufes.inf.nemo.mlt.web.reasoner.sparql;

import java.util.HashMap;
import java.util.List;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.ResultSet;

public class MltDerivationRulesSparql extends MltSparqlUtil{
	public static List<HashMap<String, String>> dr1(OntModel model){
		String queryString = ""
				+ "select distinct ?x\n"
				+ "where {\n"
				+ "	?t rdf:type mlt:1stOrderClass .\n"
				+ "	?x rdf:type ?t .\n"
				+ "}";
		
		ResultSet results = executeQuery(model, queryString);	
		
		List<HashMap<String, String>> result = getResultValues(model, results, "x");
		
		return result;
	}	
	
	public static List<HashMap<String, String>> dr2(OntModel model){
		String queryString = ""
				+ "select distinct ?t\n"
				+ "where {\n"
				+ "	?x rdf:type ?t .\n"
				+ "	?x rdf:type mlt:TokenIndividual .\n"
				+ "	filter(?t != mlt:TokenIndividual) .\n"
				+ "}";
		
		ResultSet results = executeQuery(model, queryString);	
		
		List<HashMap<String, String>> result = getResultValues(model, results, "t");
		
		return result;
	}	
	
	public static List<HashMap<String, String>> dr3(OntModel model){
		String queryString = ""
				+ "select distinct ?t1\n"
				+ "where {\n"
				+ "	?t rdf:type mlt:2ndOrderClass . \n"
				+ "	?t1 rdf:type ?t .\n"
				+ "}";
		
		ResultSet results = executeQuery(model, queryString);	
		
		List<HashMap<String, String>> result = getResultValues(model, results, "t1");
		
		return result;
	}	
	
	public static List<HashMap<String, String>> dr4(OntModel model){
		String queryString = ""
				+ "select distinct ?t\n"
				+ "where {\n"
				+ "	?t1 rdf:type ?t . \n"
				+ "	?t1 rdf:type mlt:1stOrderClass .\n"
				+ "	filter(?t != mlt:TokenIndividual) .\n"
				+ "}";
		
		ResultSet results = executeQuery(model, queryString);	
		
		List<HashMap<String, String>> result = getResultValues(model, results, "t");
		
		return result;
	}	
	
	public static List<HashMap<String, String>> dr5(OntModel model){
		String queryString = ""
				+ "select distinct ?t1\n"
				+ "where {\n"
				+ "	?t rdf:type mlt:3rdOrderClass . \n"
				+ "	?t1 rdf:type ?t .\n"
				+ "}";
		
		ResultSet results = executeQuery(model, queryString);	
		
		List<HashMap<String, String>> result = getResultValues(model, results, "t1");
		
		return result;
	}	
	
	public static List<HashMap<String, String>> dr6(OntModel model){
		String queryString = ""
				+ "select distinct ?t\n"
				+ "where {\n"
				+ "	?t1 rdf:type ?t . \n"
				+ "	?t1 rdf:type mlt:2ndOrderClass .\n"
				+ "	filter(?t != mlt:TokenIndividual) .\n"
				+ "}";
		
		ResultSet results = executeQuery(model, queryString);	
		
		List<HashMap<String, String>> result = getResultValues(model, results, "t");
		
		return result;
	}	
	
	public static List<HashMap<String, String>> dr7(OntModel model){
		String queryString = ""
				+ "select distinct ?e ?t2 \n"
				+ "where {\n"
				+ "	?t1 rdfs:subClassOf* ?t2 .\n"
				+ "	?e rdf:type ?t1 \n"
				+ "}";
		
		ResultSet results = executeQuery(model, queryString);	
		
		List<HashMap<String, String>> result = getResultValues(model, results, "t2", "e");
		
		return result;
	}	
	
	public static List<HashMap<String, String>> dr8(OntModel model){
		String queryString = ""
				+ "select distinct ?t2 ?t3\n"
				+ "where {\n"
				+ "	?t1 mlt:isPowertypeOf ?t2 .\n"
				+ "	?t3 rdf:type ?t1 .\n"
				+ "	?t1 rdf:type ?t1Type . \n"
				+ "	filter(?t1Type != mlt:TokenIndividual) . \n"
				+ "}";
		
		ResultSet results = executeQuery(model, queryString);	
		
		List<HashMap<String, String>> result = getResultValues(model, results, "t2", "t3");
		
		return result;
	}		
	
	public static List<HashMap<String, String>> dr9(OntModel model){
		String queryString = ""
				+ "select distinct ?t1 ?t3\n"
				+ "where {\n"
				+ "	?t1 mlt:isPowertypeOf ?t2 .\n"
				+ "	?t3 rdfs:subClassOf* ?t2 .\n"
				+ "	?t1 rdf:type ?t1Type . \n"
				+ "	filter(?t1Type != mlt:TokenIndividual) . \n"
				+ "}";
		
		ResultSet results = executeQuery(model, queryString);	
		
		List<HashMap<String, String>> result = getResultValues(model, results, "t1", "t3");
		
		return result;
	}		
	
	public static List<HashMap<String, String>> dr10(OntModel model){
		String queryString = ""
				+ "select distinct ?t2 ?t3\n"
				+ "where {\n"
				+ "	?t1 mlt:characterizes ?t2 .\n"
				+ "	?t3 rdf:type ?t1 .\n"
				+ "	?t1 rdf:type ?t1Type . \n"
				+ "	filter(?t1Type != mlt:TokenIndividual) . \n"
				+ "}";
		
		ResultSet results = executeQuery(model, queryString);	
		
		List<HashMap<String, String>> result = getResultValues(model, results, "t2", "t3");
		
		return result;
	}	
	
	public static List<HashMap<String, String>> dr11(OntModel model){
		String queryString = ""
				+ "select distinct ?t1 ?t2\n"
				+ "where {\n"
				+ "	?t1 mlt:completelyCharacterizes ?t2 .\n"
				+ "}";
		
		ResultSet results = executeQuery(model, queryString);	
		
		List<HashMap<String, String>> result = getResultValues(model, results, "t1", "t2");
		
		return result;
	}	
	
	public static List<HashMap<String, String>> dr12(OntModel model){
		String queryString = ""
				+ "select distinct ?t1 ?t2\n"
				+ "where {\n"
				+ "	?t1 mlt:disjointlyCharacterizes ?t2 .\n"
				+ "}";
		
		ResultSet results = executeQuery(model, queryString);	
		
		List<HashMap<String, String>> result = getResultValues(model, results, "t1", "t2");
		
		return result;
	}	
	
	public static List<HashMap<String, String>> dr13(OntModel model){
		String queryString = ""
				+ "select distinct ?t1 ?t2\n"
				+ "where {\n"
				+ "	?t1 mlt:partitions ?t2 .\n"
				+ "}";
		
		ResultSet results = executeQuery(model, queryString);	
		
		List<HashMap<String, String>> result = getResultValues(model, results, "t1", "t2");
		
		return result;
	}	
	
	public static List<HashMap<String, String>> dr14(OntModel model){
		String queryString = ""
				+ "select distinct ?t1 ?t2\n"
				+ "where {\n"
				+ "	?t1 mlt:completelyCharacterizes ?t2 .\n"
				+ "	?t1 mlt:disjointlyCharacterizes ?t2 .\n"
				+ "}";
		
		ResultSet results = executeQuery(model, queryString);	
		
		List<HashMap<String, String>> result = getResultValues(model, results, "t1", "t2");
		
		return result;
	}
	
	public static List<HashMap<String, String>> dr15(OntModel model){
		String queryString = ""
				+ "select distinct ?t\n"
				+ "where {\n"
				+ "	?t rdfs:subClassOf+ mlt:TokenIndividual .\n"
				+ "	minus{\n"
				+ "		?t rdf:type mlt:1stOrderClass . \n"
				+ "	}\n"
				+ "}";
		
		ResultSet results = executeQuery(model, queryString);	
		
		List<HashMap<String, String>> result = getResultValues(model, results, "t");
		
		return result;
	}
	
	public static List<HashMap<String, String>> dr16(OntModel model){
		String queryString = ""
				+ "select distinct ?t\n"
				+ "where {\n"
				+ "	?t rdf:type mlt:1stOrderClass .\n"
				+ "	minus{\n"
				+ "		?t rdfs:subClassOf mlt:TokenIndividual . \n"
				+ "	}\n"
				+ "}";
		
		ResultSet results = executeQuery(model, queryString);	
		
		List<HashMap<String, String>> result = getResultValues(model, results, "t");
		
		return result;
	}	
	
	public static List<HashMap<String, String>> dr17(OntModel model){
		String queryString = ""
				+ "select distinct ?t\n"
				+ "where {\n"
				+ "	?t rdfs:subClassOf+ mlt:1stOrderClass .\n"
				+ "	minus{\n"
				+ "		?t rdf:type mlt:2ndOrderClass . \n"
				+ "	}\n"
				+ "}";
		
		ResultSet results = executeQuery(model, queryString);	
		
		List<HashMap<String, String>> result = getResultValues(model, results, "t");
		
		return result;
	}
	
	public static List<HashMap<String, String>> dr18(OntModel model){
		String queryString = ""
				+ "select distinct ?t\n"
				+ "where {\n"
				+ "	?t rdf:type mlt:2ndOrderClass .\n"
				+ "	minus{\n"
				+ "		?t rdfs:subClassOf mlt:1stOrderClasss . \n"
				+ "	}\n"
				+ "}";
		
		ResultSet results = executeQuery(model, queryString);	
		
		List<HashMap<String, String>> result = getResultValues(model, results, "t");
		
		return result;
	}
	
	public static List<HashMap<String, String>> dr19(OntModel model){
		String queryString = ""
				+ "select distinct ?t\n"
				+ "where {\n"
				+ "	?t rdfs:subClassOf+ mlt:2ndOrderClass .\n"
				+ "	minus{\n"
				+ "		?t rdf:type mlt:3rdOrderClass . \n"
				+ "	}\n"
				+ "}";
		
		ResultSet results = executeQuery(model, queryString);	
		
		List<HashMap<String, String>> result = getResultValues(model, results, "t");
		
		return result;
	}
	
	public static List<HashMap<String, String>> dr20(OntModel model){
		String queryString = ""
				+ "select distinct ?t\n"
				+ "where {\n"
				+ "	?t rdf:type mlt:3rdOrderClass .\n"
				+ "	minus{\n"
				+ "		?t rdfs:subClassOf mlt:2ndOrderClass . \n"
				+ "	}\n"
				+ "}";
		
		ResultSet results = executeQuery(model, queryString);	
		
		List<HashMap<String, String>> result = getResultValues(model, results, "t");
		
		return result;
	}
	
	public static List<HashMap<String, String>> dr21(OntModel model){
		String queryString = ""
				+ "select distinct ?t3 ?t4\n"
				+ "where {\n"
				+ "	?t2 rdfs:subClassOf+ ?t1 .\n"
				+ "	?t4 mlt:isPowertypeOf ?t2 .\n"
				+ "	?t3 mlt:isPowertypeOf ?t1 .\n"
				+ "	minus{\n"
				+ "		?t4 rdfs:subClassOf ?t3 . \n"
				+ "	}\n"
				+ "}";
		
		ResultSet results = executeQuery(model, queryString);	
		
		List<HashMap<String, String>> result = getResultValues(model, results, "t3", "t4");
		
		return result;
	}
	
	public static List<HashMap<String, String>> dr22(OntModel model) {
		String queryString = ""
				+ "SELECT DISTINCT *\n"
				+ "WHERE {\n"
				+ "	?t1 mlt:isSubordinateTo ?t2 .\n"
				+ "	?t2 mlt:characterizes ?t3 .\n"
				+ "}";
		
		ResultSet results = executeQuery(model, queryString);	
		
		List<HashMap<String, String>> result = getResultValues(model, results, "t1", "t3");
		
		return result;
	}
	
	public static List<HashMap<String, String>> dr23(OntModel model){
		String queryString = ""
				+ "select distinct ?t2 ?t3\n"
				+ "where {\n"
				+ "	?t2 mlt:isPowertypeOf ?t1 .\n"
				+ "	?t3 mlt:characterizes ?t1 .\n"
				+ "	minus{\n"
				+ "		?t3 rdfs:subClassOf ?t2 . \n"
				+ "	}\n"
				+ "}";
		
		ResultSet results = executeQuery(model, queryString);	
		
		List<HashMap<String, String>> result = getResultValues(model, results, "t2", "t3");
		
		return result;
	}
	
	public static List<HashMap<String, String>> getD4andD5(OntModel model){
		String queryString = ""
				+ "select distinct ?t1 ?t2 ?t3\n"
				+ "where {\n"
				+ "	?t3 rdf:type ?t1 .\n"
				+ "	?t3 rdfs:subClassOf+ ?t2 .\n"
				+ " filter(?t2 != ?t3) . "
				+ "}";
		
		ResultSet results = executeQuery(model, queryString);	
		
		List<HashMap<String, String>> result = getResultValues(model, results, "t1", "t2", "t3");
		
		return result;
	}
}
