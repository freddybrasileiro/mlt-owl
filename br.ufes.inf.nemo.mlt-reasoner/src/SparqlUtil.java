import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.ufes.inf.nemo.ufo_rdf.vocabulary.MLT;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;


public class SparqlUtil {
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
		System.out.println("\nExecuting getBaseAndSubTypesFromDifferentOrders()...");
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
		Query query = QueryFactory.create(queryString); 		
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();		
		
//		ResultSetFormatter.out(results, model);
		
		List<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();				
		while (results.hasNext()) 
		{			
			HashMap<String, String> resultRow = new HashMap<String, String>();
			QuerySolution row = results.next();
			resultRow.put("baseType", row.get("baseType").toString());
			resultRow.put("baseTypeHO", row.get("baseTypeHO").toString());
			resultRow.put("subType", row.get("subType").toString());
			resultRow.put("subTypeHO", row.get("subTypeHO").toString());
			
			result.add(resultRow);
		}
		return result;
	}
	
	public static List<HashMap<String, String>> getHighOrderFromByTransitivity(OntModel model){
		System.out.println("\nExecuting getHighOrderFromByTransitivity()...");
		String queryString = ""
				+ PREFIXES
				+ "SELECT DISTINCT *\n"
				+ "WHERE {\n"
				+ "	?baseType rdf:type ?baseTypeHO .  \n"
				+ "	?subType rdfs:subClassOf ?baseType .\n"
				+ "	FILTER (?baseTypeHO IN (mlt:1stOrderClass, mlt:2ndOrderClass, mlt:3rdOrderClass) ) .\n"
				+ " FILTER (?baseType NOT IN (?subType) ) .\n"
				+ "}";
		Query query = QueryFactory.create(queryString); 		
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();		
		
		List<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();				
		while (results.hasNext()) 
		{			
			HashMap<String, String> resultRow = new HashMap<String, String>();
			QuerySolution row = results.next();
			resultRow.put("baseType", row.get("baseType").toString());
			resultRow.put("baseTypeHO", row.get("baseTypeHO").toString());
			resultRow.put("subType", row.get("subType").toString());
			
			result.add(resultRow);
		}
		return result;
	}
	
	public static List<HashMap<String, String>> getA1AxiomInconsistencies(OntModel model){
		System.out.println("\nExecuting getA1AxiomInconsistencies()...");
		String queryString = ""
				+ PREFIXES
				+ "SELECT DISTINCT *\n"
				+ "WHERE {\n"
				+ "	?x rdf:type  mlt:TokenIndividual .\n"
				+ "	?y rdf:type ?x .\n"
				+ "}";
		Query query = QueryFactory.create(queryString); 		
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();		
		
		List<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();				
		while (results.hasNext()) 
		{			
			HashMap<String, String> resultRow = new HashMap<String, String>();
			QuerySolution row = results.next();
			resultRow.put("x", row.get("x").toString());
			resultRow.put("y", row.get("y").toString());
			
			result.add(resultRow);
		}
		return result;
	}
	
	
}
