import java.util.HashMap;

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
	
	public static HashMap<String, String> inferHighOrderType(OntModel model){
		System.out.println("\nExecuting inferHighOrderType()...");
		String queryString = ""
				+ PREFIXES
				+ "SELECT DISTINCT *\n"
				+ "WHERE {\n"
				+ "	?baseType rdf:type ?baseTypeHO .  \n"
				+ "	?subType rdfs:subClassOf ?baseType .\n"
				+ "	OPTIONAL{\n"
				+ "		?subType rdf:type ?subTypeHO .\n"
				+ "		FILTER (?subTypeHO IN (mlt:1stOrderClass, mlt:2ndOrderClass, mlt:3rdOrderClass) ) .\n"
				+ "	}\n"
				+ "	FILTER (?baseTypeHO IN (mlt:1stOrderClass, mlt:2ndOrderClass, mlt:3rdOrderClass) ) .\n"
				+ "}";
		Query query = QueryFactory.create(queryString); 		
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();		
		HashMap<String, String> result = new HashMap<String, String>();				
		while (results.hasNext()) 
		{			
			QuerySolution row = results.next();
		    result.put("baseType", row.get("baseType").toString());
		    result.put("baseTypeHO", row.get("baseTypeHO").toString());
		    result.put("subType", row.get("subType").toString());
		    String subTypeHO;
			if(row.get("subTypeHO") == null){
		    	subTypeHO = "";
		    }else{
		    	subTypeHO = row.get("subTypeHO").toString();
		    }
		    
		    result.put("subTypeHO", subTypeHO);
		}
		return result;
	}
}
