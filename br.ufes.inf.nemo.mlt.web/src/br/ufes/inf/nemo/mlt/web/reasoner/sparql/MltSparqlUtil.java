package br.ufes.inf.nemo.mlt.web.reasoner.sparql;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.ufes.inf.nemo.mlt.web.vocabulary.MLT;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

public class MltSparqlUtil {
	static public String w3URI  = "http://www.w3.org/";
	static public String owl = "owl";
	static public String owlURI = "http://www.w3.org/2002/07/owl#";
	static public String rdfs = "rdfs";
	static public String rdfsURI = "http://www.w3.org/2000/01/rdf-schema#";
	static public String rdf = "rdf";
	static public String rdfURI = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	static public String xml = "xsd";
	static public String xmlURI = "http://www.w3.org/2001/XMLSchema#";
	
	static public String PREFIXES = ""
			+ "PREFIX " + owl + ": <" + owlURI + ">\n"
			+ "PREFIX " + rdfs + ": <" + rdfsURI + ">\n"
			+ "PREFIX " + rdf + ": <" + rdfURI + ">\n"
			+ "PREFIX " + xml + ": <" + xmlURI + ">\n"
			+ "PREFIX " + MLT.prefix + ": <" + MLT.NS + ">\n";
	
	public static List<HashMap<String, String>> getBaseAndSubTypesFromDifferentOrders(OntModel model){
		String queryString = ""
				+ PREFIXES
				+ "SELECT DISTINCT *\n"
				+ "WHERE {\n"
				+ "	?baseType rdf:type ?baseTypeHO .  \n"
				+ "	?subType rdfs:subClassOf ?baseType .\n"
				+ "	FILTER (?baseTypeHO IN (mlt:1stOrderClass, mlt:2ndOrderClass, mlt:3rdOrderClass) ) .\n"
				+ " FILTER (?baseTypeHO NOT IN (?subTypeHO) ) .\n"
				+ "	?subType rdf:type ?subTypeHO .\n"
				+ "	FILTER (?subTypeHO IN (mlt:1stOrderClass, mlt:2ndOrderClass, mlt:3rdOrderClass) ) .\n"
				+ "}";
		ResultSet results = executeQuery(model, queryString);
		
		List<HashMap<String, String>> result = getResultValues(results, "baseType", "baseTypeHO", "subType", "subTypeHO");				
		
		return result;
	}
	
	public static List<HashMap<String, String>> getHighOrderFromByTransitivity(OntModel model){
		String queryString = ""
				+ PREFIXES
				+ "SELECT DISTINCT *\n"
				+ "WHERE {\n"
				+ "	?baseType rdf:type ?baseTypeHO .  \n"
				+ "	?subType rdfs:subClassOf ?baseType .\n"
				+ "	FILTER (?baseTypeHO IN (mlt:1stOrderClass, mlt:2ndOrderClass, mlt:3rdOrderClass) ) .\n"
				+ " FILTER (?baseType NOT IN (?subType) ) .\n"
				+ "}";
		ResultSet results = executeQuery(model, queryString);	
		
		List<HashMap<String, String>> result = getResultValues(results, "baseType", "baseTypeHO", "subType");				
		
		return result;
	}
	
	protected static ResultSet executeQuery(OntModel model, String queryString){
		if(!queryString.contains(PREFIXES)){
			queryString = PREFIXES + "\n" + queryString;
		}
		Query query = QueryFactory.create(queryString); 		
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();
		
		return results;
	}
	
	protected static List<HashMap<String, String>> getResultValues(ResultSet results, String... vars){
		List<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();	
		while (results.hasNext()) 
		{	
			HashMap<String, String> resultRow = new HashMap<String, String>();
			QuerySolution row = results.next();
			for (String var: vars) {
				resultRow.put(var, row.get(var).toString());				
			}			
			result.add(resultRow);
		}
		return result;
	}
}
