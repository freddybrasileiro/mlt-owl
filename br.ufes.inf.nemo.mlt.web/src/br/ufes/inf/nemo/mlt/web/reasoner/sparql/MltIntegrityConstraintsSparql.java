package br.ufes.inf.nemo.mlt.web.reasoner.sparql;

import java.util.HashMap;
import java.util.List;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.ResultSet;

public class MltIntegrityConstraintsSparql extends SparqlUtil{
	public static List<HashMap<String, String>> ic1(OntModel model){
		String queryString = ""
				+ "select distinct ?x ?y\n"
				+ "where {\n"
				+ "	?x rdf:type mlt:TokenIndividual .\n"
				+ "	?y rdf:type ?x .\n"
				+ "}";
		
		ResultSet results = executeQuery(model, queryString);	
		
		List<HashMap<String, String>> result = getResultValues(model, results, "x", "y");
		
		return result;
	}
	
	public static List<HashMap<String, String>> ic3(OntModel model){
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
	
	public static List<HashMap<String, String>> ic4(OntModel model) {
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
	
	public static List<HashMap<String, String>> ic5(OntModel model){
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
	
	public static List<HashMap<String, String>> ic6(OntModel model){
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
	
	public static List<HashMap<String, String>> ic7(OntModel model){
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
	
	public static List<HashMap<String, String>> ic8(OntModel model){
		String queryString = ""
				+ "select distinct *\n"
				+ "where {\n"
				+ "	{"
				+ "		?e rdf:type mlt:TokenIndividual .\n"
				+ "		?e rdf:type mlt:1stOrderClass .\n"
				+ "	}UNION{"
				+ "		?e rdf:type mlt:TokenIndividual .\n"
				+ "		?e rdf:type mlt:2ndOrderClass .\n"
				+ "	}UNION{"
				+ "		?e rdf:type mlt:TokenIndividual .\n"
				+ "		?e rdf:type mlt:3rdOrderClass .\n"
				+ "	}UNION{"
				+ "		?e rdf:type mlt:1stOrderClass .\n"
				+ "		?e rdf:type mlt:2ndOrderClass .\n"
				+ "	}UNION{"
				+ "		?e rdf:type mlt:1stOrderClass .\n"
				+ "		?e rdf:type mlt:3rdOrderClass .\n"
				+ "	}UNION{"
				+ "		?e rdf:type mlt:2ndOrderClass .\n"
				+ "		?e rdf:type mlt:3rdOrderClass .\n"
				+ "	}"
				+ "}";
		
		ResultSet results = executeQuery(model, queryString);	
		
		List<HashMap<String, String>> result = getResultValues(model, results, "e");
		
		return result;
	}
}
