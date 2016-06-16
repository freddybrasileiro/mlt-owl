package br.ufes.inf.nemo.mlt.web.reasoner.sparql;
import java.util.HashMap;
import java.util.List;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.ResultSet;


public class MltTheoremsSparqlUtil extends MltSparqlUtil {
	public static List<HashMap<String, String>> getT7_1(OntModel model){
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
	
	public static List<HashMap<String, String>> getT7_2(OntModel model){
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
	
	public static List<HashMap<String, String>> getT8_1(OntModel model){
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
	
	public static List<HashMap<String, String>> getT8_2(OntModel model){
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
	
	public static List<HashMap<String, String>> getT9_1(OntModel model){
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
	
	public static List<HashMap<String, String>> getT9_2(OntModel model){
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
	
	public static List<HashMap<String, String>> getT13(OntModel model){
		String queryString = ""
				+ "select distinct *\n"
				+ "where {\n"
				+ "	?p mlt:isPowertypeOf ?t .\n"
				+ "	?p1 mlt:isPowertypeOf ?t .\n"
				+ "	FILTER (?p != ?p1) .\n"
				+ "}";
		
		ResultSet results = executeQuery(model, queryString);	
		
		List<HashMap<String, String>> result = getResultValues(model, results, "t", "p", "p1");
		
		return result;
	}
	
	public static List<HashMap<String, String>> getT14(OntModel model){
		String queryString = ""
				+ "select distinct *\n"
				+ "where {\n"
				+ "	?p mlt:isPowertypeOf ?t .\n"
				+ "	?p mlt:isPowertypeOf ?t1 .\n"
				+ "	FILTER (?t != ?t1) .\n"
				+ "}";
		
		ResultSet results = executeQuery(model, queryString);	
		
		List<HashMap<String, String>> result = getResultValues(model, results, "t", "p", "t1");
		
		return result;
	}
	
	public static List<HashMap<String, String>> getT15(OntModel model){
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
	
	public static List<HashMap<String, String>> getT17(OntModel model){
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
	
	public static List<HashMap<String, String>> getT18(OntModel model){
		String queryString = ""
				+ "select distinct *\n"
				+ "where {\n"
				+ "	?t1 mlt:partitions ?t3 .\n"
				+ "	?t2 mlt:partitions ?t3 .\n"
				+ "	?t1 rdfs:subClassOf ?t2 .\n"
				+ "	filter(?t1 != ?t2) .\n"
				+ "}";
		
		ResultSet results = executeQuery(model, queryString);	
		
		List<HashMap<String, String>> result = getResultValues(model, results, "t1", "t2", "t3");
		
		return result;
	}
	
	public static List<HashMap<String, String>> getT5(OntModel model){
		String queryString = ""
				+ "SELECT DISTINCT *\n"
				+ "WHERE {\n"
				+ "	?x rdf:type ?y .\n"
				+ "	?y rdf:type ?x .\n"
				+ "}";
		
		ResultSet results = executeQuery(model, queryString);	
		
		List<HashMap<String, String>> result = getResultValues(model, results, "x", "y");
		
		return result;
	}

	public static List<HashMap<String, String>> getT6(OntModel model) {
		String queryString = ""
				+ "SELECT DISTINCT *\n"
				+ "WHERE {\n"
				+ "	?x rdf:type ?y .\n"
				+ "	?y rdf:type ?z .\n"
				+ "	?x rdf:type ?z .\n"
				+ "}";
		
		ResultSet results = executeQuery(model, queryString);	
		
		List<HashMap<String, String>> result = getResultValues(model, results, "x", "y", "z");
		
		return result;
	}

	public static List<HashMap<String, String>> getT16(OntModel model) {
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
}
