package br.ufes.inf.nemo.mlt.web.reasoner.sparql;
import java.util.HashMap;
import java.util.List;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.ResultSet;


public class MltTheoremsSparqlUtil extends MltSparqlUtil {
	public static List<HashMap<String, String>> getT4Going(OntModel model){
		String queryString = ""
				+ "SELECT DISTINCT *\n"
				+ "WHERE {\n"
				+ "	?t rdf:type mlt:1stOrderClass .\n"
				+ "}";
		
		ResultSet results = executeQuery(model, queryString);	
		
		List<HashMap<String, String>> result = getResultValues(results, "t");
		
		return result;
	}	
	
	public static List<HashMap<String, String>> getT4Returning(OntModel model){
		String queryString = ""
				+ "SELECT DISTINCT *\n"
				+ "WHERE {\n"
				+ "	?t rdfs:subClassOf mlt:TokenIndividual .\n"
				+ "}";
		
		ResultSet results = executeQuery(model, queryString);	
		
		List<HashMap<String, String>> result = getResultValues(results, "t");
		
		return result;
	}
	
	public static List<HashMap<String, String>> getT5Going(OntModel model){
		String queryString = ""
				+ "SELECT DISTINCT *\n"
				+ "WHERE {\n"
				+ "	?t rdf:type mlt:2ndOrderClass .\n"
				+ "}";
		
		ResultSet results = executeQuery(model, queryString);	
		
		List<HashMap<String, String>> result = getResultValues(results, "t");
		
		return result;
	}
	
	public static List<HashMap<String, String>> getT5Returning(OntModel model){
		String queryString = ""
				+ "SELECT DISTINCT *\n"
				+ "WHERE {\n"
				+ "	?t rdfs:subClassOf mlt:1stOrderClass .\n"
				+ "}";
		
		ResultSet results = executeQuery(model, queryString);	
		
		List<HashMap<String, String>> result = getResultValues(results, "t");
		
		return result;
	}
	
	public static List<HashMap<String, String>> getT6Going(OntModel model){
		String queryString = ""
				+ "SELECT DISTINCT *\n"
				+ "WHERE {\n"
				+ "	?t rdf:type mlt:3rdOrderClass .\n"
				+ "}";
		
		ResultSet results = executeQuery(model, queryString);	
		
		List<HashMap<String, String>> result = getResultValues(results, "t");
		
		return result;
	}
	
	public static List<HashMap<String, String>> getT6Returning(OntModel model){
		String queryString = ""
				+ "SELECT DISTINCT *\n"
				+ "WHERE {\n"
				+ "	?t rdfs:subClassOf mlt:2ndOrderClass .\n"
				+ "}";
		
		ResultSet results = executeQuery(model, queryString);	
		
		List<HashMap<String, String>> result = getResultValues(results, "t");
		
		return result;
	}
	
	public static List<HashMap<String, String>> getT10GoingInconsistencies(OntModel model){
		String queryString = ""
				+ "SELECT DISTINCT *\n"
				+ "WHERE {\n"
				+ "	?p mlt:isPowertypeOf ?t .\n"
				+ "	?p1 mlt:isPowertypeOf ?t .\n"
				+ "	FILTER (?p NOT IN (?p1)) .\n"
				+ "}";
		
		ResultSet results = executeQuery(model, queryString);	
		
		List<HashMap<String, String>> result = getResultValues(results, "t", "p", "p1");
		
		return result;
	}
	
	public static List<HashMap<String, String>> getT11GoingInconsistencies(OntModel model){
		String queryString = ""
				+ "SELECT DISTINCT *\n"
				+ "WHERE {\n"
				+ "	?p mlt:isPowertypeOf ?t .\n"
				+ "	?p mlt:isPowertypeOf ?t1 .\n"
				+ "	FILTER (?t NOT IN (?t1)) .\n"
				+ "}";
		
		ResultSet results = executeQuery(model, queryString);	
		
		List<HashMap<String, String>> result = getResultValues(results, "t", "p", "p1");
		
		return result;
	}
	
	public static List<HashMap<String, String>> getT12Going(OntModel model){
		String queryString = ""
				+ "SELECT DISTINCT *\n"
				+ "WHERE {\n"
				+ "	?t2 rdfs:subClassOf ?t1 .\n"
				+ "	?t4 mlt:isPowerTypeOf ?t2 .\n"
				+ "	?t3 mlt:isPowerTypeOf ?t1 .\n"
				+ "}";
		
		ResultSet results = executeQuery(model, queryString);	
		
		List<HashMap<String, String>> result = getResultValues(results, "t1", "t2", "t3", "t4");
		
		return result;
	}
	
	public static List<HashMap<String, String>> getT13Going(OntModel model){
		String queryString = ""
				+ "SELECT DISTINCT *\n"
				+ "WHERE {\n"
				+ "	?t2 mlt:isPowerTypeOf ?t1 .\n"
				+ "	?t3 mlt:characterizes ?t1 .\n"
				+ "}";
		
		ResultSet results = executeQuery(model, queryString);	
		
		List<HashMap<String, String>> result = getResultValues(results, "t1", "t2", "t3");
		
		return result;
	}
	
	public static List<HashMap<String, String>> getT14GoingInconsistencies(OntModel model){
		String queryString = ""
				+ "SELECT DISTINCT *\n"
				+ "WHERE {\n"
				+ "	?t1 mlt:partitions ?t3 .\n"
				+ "	?t2 mlt:partitions ?t3 .\n"
				+ "	?t1 mlt:properSpecializes ?t2 .\n"
				+ "}";
		
		ResultSet results = executeQuery(model, queryString);	
		
		List<HashMap<String, String>> result = getResultValues(results, "t1", "t2", "t3");
		
		return result;
	}
}
