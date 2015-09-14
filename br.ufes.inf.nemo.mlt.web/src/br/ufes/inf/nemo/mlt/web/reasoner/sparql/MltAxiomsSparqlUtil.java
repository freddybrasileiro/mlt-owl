package br.ufes.inf.nemo.mlt.web.reasoner.sparql;
import java.util.HashMap;
import java.util.List;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.ResultSet;


public class MltAxiomsSparqlUtil extends MltSparqlUtil{
	public static List<HashMap<String, String>> getA1GoingInconsistencies(OntModel model){
		String queryString = ""
				+ "SELECT DISTINCT *\n"
				+ "WHERE {\n"
				+ "	?x rdf:type mlt:TokenIndividual .\n"
				+ "	?y rdf:type ?x .\n"
				+ "}";
		
		ResultSet results = executeQuery(model, queryString);	
		
		List<HashMap<String, String>> result = getResultValues(model, results, "x", "y");
		
		return result;
	}
	
	public static List<HashMap<String, String>> getA2Going(OntModel model){
		String queryString = ""
				+ "SELECT DISTINCT *\n"
				+ "WHERE {\n"
				+ "	?t rdf:type mlt:1stOrderClass .\n"
				+ "	?x rdf:type ?t .\n"
				+ "}";
		
		ResultSet results = executeQuery(model, queryString);	
		
		List<HashMap<String, String>> result = getResultValues(model, results, "x", "t");
		
		return result;
	}	
	
	public static List<HashMap<String, String>> getA2Returning(OntModel model){
		String queryString = ""
				+ "SELECT DISTINCT *\n"
				+ "WHERE {\n"
				+ "	?x rdf:type ?t .\n"
				+ "	?x rdf:type mlt:TokenIndividual .\n"
				+ "}";
		
		ResultSet results = executeQuery(model, queryString);	
		
		List<HashMap<String, String>> result = getResultValues(model, results, "x", "t");
		
		return result;
	}	
	
	public static List<HashMap<String, String>> getA3Going(OntModel model){
		String queryString = ""
				+ "SELECT DISTINCT *\n"
				+ "WHERE {\n"
				+ "	?t rdf:type mlt:2ndOrderClass . \n"
				+ "	?t1 rdf:type ?t .\n"
				+ "}";
		
		ResultSet results = executeQuery(model, queryString);	
		
		List<HashMap<String, String>> result = getResultValues(model, results, "t", "t1");
		
		return result;
	}	
	
	public static List<HashMap<String, String>> getA3Returning(OntModel model){
		String queryString = ""
				+ "SELECT DISTINCT *\n"
				+ "WHERE {\n"
				+ "	?t1 rdf:type ?t . \n"
				+ "	?t1 rdf:type mlt:1stOrderClass .\n"
				+ "}";
		
		ResultSet results = executeQuery(model, queryString);	
		
		List<HashMap<String, String>> result = getResultValues(model, results, "t", "t1");
		
		return result;
	}	
	
	public static List<HashMap<String, String>> getA4Going(OntModel model){
		String queryString = ""
				+ "SELECT DISTINCT *\n"
				+ "WHERE {\n"
				+ "	?t rdf:type mlt:3rdOrderClass . \n"
				+ "	?t1 rdf:type ?t .\n"
				+ "}";
		
		ResultSet results = executeQuery(model, queryString);	
		
		List<HashMap<String, String>> result = getResultValues(model, results, "t", "t1");
		
		return result;
	}	
	
	public static List<HashMap<String, String>> getA4Returning(OntModel model){
		String queryString = ""
				+ "SELECT DISTINCT *\n"
				+ "WHERE {\n"
				+ "	?t1 rdf:type ?t . \n"
				+ "	?t1 rdf:type mlt:2ndOrderClass .\n"
				+ "}";
		
		ResultSet results = executeQuery(model, queryString);	
		
		List<HashMap<String, String>> result = getResultValues(model, results, "t", "t1");
		
		return result;
	}	
	
	public static List<HashMap<String, String>> getA8Going(OntModel model){
		String queryString = ""
				+ "SELECT DISTINCT *\n"
				+ "WHERE {\n"
				+ "	?t1 rdfs:subClassOf* ?t2 .\n"
				+ "	?e rdf:type ?t1 \n"
				+ "}";
		
		ResultSet results = executeQuery(model, queryString);	
		
		List<HashMap<String, String>> result = getResultValues(model, results, "t1", "t2", "e");
		
		return result;
	}	
	
	public static List<HashMap<String, String>> getA9Going(OntModel model){
		String queryString = ""
				+ "SELECT DISTINCT *\n"
				+ "WHERE {\n"
				+ "	?t1 mlt:properSpecializes ?t2 .\n"
				+ "}";
		
		ResultSet results = executeQuery(model, queryString);	
		
		List<HashMap<String, String>> result = getResultValues(model, results, "t1", "t2");
		
		return result;
	}	
	
	public static List<HashMap<String, String>> getA9Returning(OntModel model){
		String queryString = ""
				+ "SELECT DISTINCT *\n"
				+ "WHERE {\n"
				+ "	?t1 rdfs:subClassOf* ?t2 .\n"
				+ "	?t1 owl:differentFrom ?t2 . \n"
				+ "}";
		
		ResultSet results = executeQuery(model, queryString);	
		
		List<HashMap<String, String>> result = getResultValues(model, results, "t1", "t2");
		
		return result;
	}
	
	public static List<HashMap<String, String>> getA10Returning(OntModel model){
		String queryString = ""
				+ "SELECT DISTINCT *\n"
				+ "WHERE {\n"
				+ "	?t3 rdf:type ?t1 . \n"
				+ "	?t4 rdf:type ?t2 . \n"
				+ "	?t3 mlt:properSpecializes ?t4 . \n"
				+ "}";
		
		ResultSet results = executeQuery(model, queryString);	
		
		List<HashMap<String, String>> result = getResultValues(model, results, "t1", "t2", "t3", "t4");
		
		return result;
	}	
	
	public static List<HashMap<String, String>> getA11Going1(OntModel model){
		String queryString = ""
				+ "SELECT DISTINCT *\n"
				+ "WHERE {\n"
				+ "	?t1 mlt:isPowertypeOf ?t2 .\n"
				+ "	?t3 rdf:type ?t1 .\n"
				+ "}";
		
		ResultSet results = executeQuery(model, queryString);	
		
		List<HashMap<String, String>> result = getResultValues(model, results, "t1", "t2", "t3");
		
		return result;
	}		
	
	public static List<HashMap<String, String>> getA11Going2(OntModel model){
		String queryString = ""
				+ "SELECT DISTINCT *\n"
				+ "WHERE {\n"
				+ "	?t1 mlt:isPowertypeOf ?t2 .\n"
				+ "	?t3 rdfs:subClassOf* ?t2 .\n"
				+ "}";
		
		ResultSet results = executeQuery(model, queryString);	
		
		List<HashMap<String, String>> result = getResultValues(model, results, "t1", "t2", "t3");
		
		return result;
	}		
	
	public static List<HashMap<String, String>> getA12Going(OntModel model){
		String queryString = ""
				+ "SELECT DISTINCT *\n"
				+ "WHERE {\n"
				+ "	?t1 mlt:characterizes ?t2 .\n"
				+ "	?t3 rdf:type ?t1 .\n"
				+ "}";
		
		ResultSet results = executeQuery(model, queryString);	
		
		List<HashMap<String, String>> result = getResultValues(model, results, "t1", "t2", "t3");
		
		return result;
	}	
	
	public static List<HashMap<String, String>> getA14Going1(OntModel model){
		String queryString = ""
				+ "SELECT DISTINCT *\n"
				+ "WHERE {\n"
				+ "	?t1 mlt:disjointlyCharacterizes ?t2 .\n"
				+ "}";
		
		ResultSet results = executeQuery(model, queryString);	
		
		List<HashMap<String, String>> result = getResultValues(model, results, "t1", "t2");
		
		return result;
	}	
	
	public static List<HashMap<String, String>> getA14Going2(OntModel model){
		String queryString = ""
				+ "SELECT DISTINCT *\n"
				+ "WHERE {\n"
				+ "	?t1 mlt:disjointlyCharacterizes ?t2 .\n"
				+ "	?t3 rdf:type ?t1 .\n"
				+ "	?t4 rdf:type ?t1 .\n"
				+ "	?e rdf:type ?t3 .\n"
				+ "	?e rdf:type ?t4 .\n"
				+ "}";
		
		ResultSet results = executeQuery(model, queryString);	
		
		List<HashMap<String, String>> result = getResultValues(model, results, "t1", "t2", "t3", "t4", "e");
		
		return result;
	}	
	
	public static List<HashMap<String, String>> getA15Going(OntModel model){
		String queryString = ""
				+ "SELECT DISTINCT *\n"
				+ "WHERE {\n"
				+ "	?t1 mlt:partitions ?t2 .\n"
				+ "}";
		
		ResultSet results = executeQuery(model, queryString);	
		
		List<HashMap<String, String>> result = getResultValues(model, results, "t1", "t2");
		
		return result;
	}	
	
	public static List<HashMap<String, String>> getA15Returning(OntModel model){
		String queryString = ""
				+ "SELECT DISTINCT *\n"
				+ "WHERE {\n"
				+ "	?t1 mlt:completelyCharacterizes ?t2 .\n"
				+ "	?t1 mlt:disjointlyCharacterizes ?t2 .\n"
				+ "}";
		
		ResultSet results = executeQuery(model, queryString);	
		
		List<HashMap<String, String>> result = getResultValues(model, results, "t1", "t2");
		
		return result;
	}	
}
