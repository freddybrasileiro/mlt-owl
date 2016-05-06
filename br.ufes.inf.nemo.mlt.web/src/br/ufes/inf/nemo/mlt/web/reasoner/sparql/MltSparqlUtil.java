package br.ufes.inf.nemo.mlt.web.reasoner.sparql;
import java.util.HashMap;
import java.util.List;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.ResultSet;

public class MltSparqlUtil extends SparqlUtil{
	public static List<HashMap<String, String>> getBaseAndSubTypesFromDifferentOrders(OntModel model){
		String queryString = ""
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
		
		List<HashMap<String, String>> result = getResultValues(model, results, "baseType", "baseTypeHO", "subType", "subTypeHO");				
		
		return result;
	}
	
	public static List<HashMap<String, String>> getHighOrderFromByTransitivity(OntModel model){
		String queryString = ""
				+ "SELECT DISTINCT *\n"
				+ "WHERE {\n"
				+ "	?baseType rdf:type ?baseTypeHO .  \n"
				+ "	?subType rdfs:subClassOf ?baseType .\n"
				+ "	FILTER (?baseTypeHO IN (mlt:1stOrderClass, mlt:2ndOrderClass, mlt:3rdOrderClass) ) .\n"
				+ " FILTER (?baseType NOT IN (?subType) ) .\n"
				+ "}";
		ResultSet results = executeQuery(model, queryString);	
		
		List<HashMap<String, String>> result = getResultValues(model, results, "baseType", "baseTypeHO", "subType");				
		
		return result;
	}	
	
	public static List<HashMap<String, String>> getDomainAndRangeFromMltOP(OntModel model){
		String queryString = ""
				+ "SELECT DISTINCT *\n"
				+ "WHERE {\n"
				+ "	?x ?mltOP ?y .  \n"
				+ "	FILTER (?mltOP IN (rdfs:subClassOf, mlt:isSubordinatedTo, rdf:type, mlt:isPowertypeOf, mlt:characterizes) ) .\n"
				+ "	?x rdf:type ?xType .  \n"
				+ "	FILTER (?x IN (mlt:TokenIndividual, mlt:1stOrderClass, mlt:2ndOrderClass, mlt:3rdOrderClass) ) .\n"
				+ "	?y rdf:type ?yType .  \n"
				+ "	FILTER (?y IN (mlt:TokenIndividual, mlt:1stOrderClass, mlt:2ndOrderClass, mlt:3rdOrderClass) ) .\n"
				+ "}";
		ResultSet results = executeQuery(model, queryString);	
		
		List<HashMap<String, String>> result = getResultValues(model, results, "x", "y", "mltOP", "xType", "yType");				
		
		return result;
	}	
}
