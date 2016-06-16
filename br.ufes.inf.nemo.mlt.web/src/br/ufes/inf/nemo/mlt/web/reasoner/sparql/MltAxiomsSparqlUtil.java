package br.ufes.inf.nemo.mlt.web.reasoner.sparql;
import java.util.HashMap;
import java.util.List;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.ResultSet;


public class MltAxiomsSparqlUtil extends MltSparqlUtil{
	public static List<HashMap<String, String>> getA1(OntModel model){
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
	
	public static List<HashMap<String, String>> getA3_1(OntModel model){
		String queryString = ""
				+ "select distinct ?x\n"
				+ "where {\n"
				+ "	?t rdf:type mlt:1stOrderClass .\n"
				+ "	?x rdf:type ?t .\n"
				+ "	minus{\n"
				+ "		?x rdf:type mlt:TokenIndividual . \n"
				+ "	}\n"
				+ "}";
		
		ResultSet results = executeQuery(model, queryString);	
		
		List<HashMap<String, String>> result = getResultValues(model, results, "x");
		
		return result;
	}	
	
	public static List<HashMap<String, String>> getA3_2(OntModel model){
		String queryString = ""
				+ "select distinct ?t\n"
				+ "where {\n"
				+ "	?x rdf:type ?t .\n"
				+ "	?x rdf:type mlt:TokenIndividual .\n"
				+ "	filter(?t != mlt:TokenIndividual) .\n"
				+ "	minus{\n"
				+ "		?t rdf:type mlt:1stOrderClass . \n"
				+ "	}\n"
				+ "}";
		
		ResultSet results = executeQuery(model, queryString);	
		
		List<HashMap<String, String>> result = getResultValues(model, results, "t");
		
		return result;
	}	
	
	public static List<HashMap<String, String>> getA4_1(OntModel model){
		String queryString = ""
				+ "select distinct ?t1\n"
				+ "where {\n"
				+ "	?t rdf:type mlt:2ndOrderClass . \n"
				+ "	?t1 rdf:type ?t .\n"
				+ "	minus{\n"
				+ "		?t1 rdf:type mlt:1stOrderClass . \n"
				+ "	}\n"
				+ "}";
		
		ResultSet results = executeQuery(model, queryString);	
		
		List<HashMap<String, String>> result = getResultValues(model, results, "t1");
		
		return result;
	}	
	
	public static List<HashMap<String, String>> getA4_2(OntModel model){
		String queryString = ""
				+ "select distinct ?t\n"
				+ "where {\n"
				+ "	?t1 rdf:type ?t . \n"
				+ "	?t1 rdf:type mlt:1stOrderClass .\n"
				+ "	filter(?t != mlt:TokenIndividual) .\n"
				+ "	minus{\n"
				+ "		?t rdf:type mlt:2ndOrderClass . \n"
				+ "	}\n"
				+ "}";
		
		ResultSet results = executeQuery(model, queryString);	
		
		List<HashMap<String, String>> result = getResultValues(model, results, "t");
		
		return result;
	}	
	
	public static List<HashMap<String, String>> getA5_1(OntModel model){
		String queryString = ""
				+ "select distinct ?t1\n"
				+ "where {\n"
				+ "	?t rdf:type mlt:3rdOrderClass . \n"
				+ "	?t1 rdf:type ?t .\n"
				+ "	minus{\n"
				+ "		?t1 rdf:type mlt:2ndOrderClass . \n"
				+ "	}\n"
				+ "}";
		
		ResultSet results = executeQuery(model, queryString);	
		
		List<HashMap<String, String>> result = getResultValues(model, results, "t1");
		
		return result;
	}	
	
	public static List<HashMap<String, String>> getA5_2(OntModel model){
		String queryString = ""
				+ "select distinct ?t\n"
				+ "where {\n"
				+ "	?t1 rdf:type ?t . \n"
				+ "	?t1 rdf:type mlt:2ndOrderClass .\n"
				+ "	filter(?t != mlt:TokenIndividual) .\n"
				+ "	minus{\n"
				+ "		?t rdf:type mlt:3rdOrderClass . \n"
				+ "	}\n"
				+ "}";
		
		ResultSet results = executeQuery(model, queryString);	
		
		List<HashMap<String, String>> result = getResultValues(model, results, "t");
		
		return result;
	}	
	
	public static List<HashMap<String, String>> getD1(OntModel model){
		String queryString = ""
				+ "select distinct ?e ?t2 \n"
				+ "where {\n"
				+ "	?t1 rdfs:subClassOf* ?t2 .\n"
				+ "	?e rdf:type ?t1 \n"
				+ "	minus{\n"
				+ "		?e rdf:type ?t2 . \n"
				+ "	}\n"
				+ "}";
		
		ResultSet results = executeQuery(model, queryString);	
		
		List<HashMap<String, String>> result = getResultValues(model, results, "t2", "e");
		
		return result;
	}	
	
	public static List<HashMap<String, String>> getD4_1(OntModel model){
		String queryString = ""
				+ "select distinct ?t2 ?t3\n"
				+ "where {\n"
				+ "	?t1 mlt:isPowertypeOf ?t2 .\n"
				+ "	?t3 rdf:type ?t1 .\n"
				+ "	?t1 rdf:type ?t1Type . \n"
				+ "	filter(?t1Type != mlt:TokenIndividual) . \n"
				+ "	minus{\n"
				+ "		?t3 rdfs:subClassOf ?t2 . \n"
				+ "	}\n"
				+ "}";
		
		ResultSet results = executeQuery(model, queryString);	
		
		List<HashMap<String, String>> result = getResultValues(model, results, "t2", "t3");
		
		return result;
	}		
	
	public static List<HashMap<String, String>> getD4_2(OntModel model){
		String queryString = ""
				+ "select distinct ?t1 ?t3\n"
				+ "where {\n"
				+ "	?t1 mlt:isPowertypeOf ?t2 .\n"
				+ "	?t3 rdfs:subClassOf* ?t2 .\n"
				+ "	?t1 rdf:type ?t1Type . \n"
				+ "	filter(?t1Type != mlt:TokenIndividual) . \n"
				+ "	minus{\n"
				+ "		?t3 rdf:type ?t1 . \n"
				+ "	}\n"
				+ "}";
		
		ResultSet results = executeQuery(model, queryString);	
		
		List<HashMap<String, String>> result = getResultValues(model, results, "t1", "t3");
		
		return result;
	}		
	
	public static List<HashMap<String, String>> getD5(OntModel model){
		String queryString = ""
				+ "select distinct ?t2 ?t3\n"
				+ "where {\n"
				+ "	?t1 mlt:characterizes ?t2 .\n"
				+ "	?t3 rdf:type ?t1 .\n"
				+ "	?t1 rdf:type ?t1Type . \n"
				+ "	filter(?t1Type != mlt:TokenIndividual) . \n"
				+ "	minus{\n"
				+ "		?t3 rdfs:subClassOf ?t2 . \n"
				+ "	}\n"
				+ "}";
		
		ResultSet results = executeQuery(model, queryString);	
		
		List<HashMap<String, String>> result = getResultValues(model, results, "t2", "t3");
		
		return result;
	}	
	
	public static List<HashMap<String, String>> getD6(OntModel model){
		String queryString = ""
				+ "select distinct ?t1 ?t2\n"
				+ "where {\n"
				+ "	?t1 mlt:completelyCharacterizes ?t2 .\n"
				+ "	minus{\n"
				+ "		?t1 mlt:characterizes ?t2 . \n"
				+ "	}\n"
				+ "}";
		
		ResultSet results = executeQuery(model, queryString);	
		
		List<HashMap<String, String>> result = getResultValues(model, results, "t1", "t2");
		
		return result;
	}	
	
	public static List<HashMap<String, String>> getD7(OntModel model){
		String queryString = ""
				+ "select distinct ?t1 ?t2\n"
				+ "where {\n"
				+ "	?t1 mlt:disjointlyCharacterizes ?t2 .\n"
				+ "	minus{\n"
				+ "		?t1 mlt:characterizes ?t2 . \n"
				+ "	}\n"
				+ "}";
		
		ResultSet results = executeQuery(model, queryString);	
		
		List<HashMap<String, String>> result = getResultValues(model, results, "t1", "t2");
		
		return result;
	}	
	
	public static List<HashMap<String, String>> getD8_1(OntModel model){
		String queryString = ""
				+ "select distinct ?t1 ?t2\n"
				+ "where {\n"
				+ "	?t1 mlt:partitions ?t2 .\n"
				+ "	minus{\n"
				+ "		?t1 mlt:disjointlyCharacterizes ?t2 . \n"
				+ "		?t1 mlt:completelyCharacterizes ?t2 . \n"
				+ "	}\n"
				+ "}";
		
		ResultSet results = executeQuery(model, queryString);	
		
		List<HashMap<String, String>> result = getResultValues(model, results, "t1", "t2");
		
		return result;
	}	
	
	public static List<HashMap<String, String>> getD8_2(OntModel model){
		String queryString = ""
				+ "select distinct ?t1 ?t2\n"
				+ "where {\n"
				+ "	?t1 mlt:completelyCharacterizes ?t2 .\n"
				+ "	?t1 mlt:disjointlyCharacterizes ?t2 .\n"
				+ "	minus{\n"
				+ "		?t1 mlt:partitions ?t2 . \n"
				+ "	}\n"
				+ "}";
		
		ResultSet results = executeQuery(model, queryString);	
		
		List<HashMap<String, String>> result = getResultValues(model, results, "t1", "t2");
		
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
