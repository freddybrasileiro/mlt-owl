import java.util.ArrayList;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

public class WikidataUtil {
	//public static String wikidataEndPoint = "http://lod.openlinksw.com/sparql";
	public static String wikidataEndPoint = "https://query.wikidata.org/sparql";
	
	static public String directProp = "http://www.wikidata.org/prop/direct/";
	static public String wdt = "wdt";
	static public String entities = "http://www.wikidata.org/entity/";
	static public String wd = "wd";
		
	public static String PREFIXES = SparqlUtil.PREFIXES 
			+ "PREFIX " + wdt + ": <" + directProp + ">\n"
			+ "PREFIX " + wd + ": <" + entities + ">\n";		
	
	public static ArrayList<String> getInstancesOf(String classUri){
		String queryString = ""
				+ PREFIXES
				+ "SELECT DISTINCT *\n"
				+ "WHERE {\n"
				+ "	{\n"
				+ "		?instance rdf:type <" + classUri + "> . \n"
				+ "	}UNION{\n"
				+ "		?instance wdt:P31 <" + classUri + "> . \n"
				+ "	}UNION{\n"
				+ "		?instance wdt:P105 <" + classUri + "> . \n"
				+ "	}\n"
				+ "}";
		
		ResultSet results = SparqlUtil.externalQuery(queryString, wikidataEndPoint);

		ArrayList<String> allInstances = new ArrayList<String>();
		while (results.hasNext()){
			QuerySolution row = results.next();
			String instance = row.get("instance").toString();
			allInstances.add(instance);	
		}
		return allInstances;
	}
	
	public static ArrayList<String> getSuperClassesOf(String classUri){
		String queryString = ""
				+ PREFIXES
				+ "SELECT DISTINCT *\n"
				+ "WHERE {\n"
				+ "	{\n"
				+ "		<" + classUri + "> rdfs:subClassOf ?superClass . \n"
				+ "	}UNION{\n"
				+ "		<" + classUri + "> wdt:P279 ?superClass . \n"
				+ "	}UNION{\n"
				+ "		<" + classUri + "> wdt:P171 ?superClass . \n"
				+ "	}\n"
				+ "}";
		
		ResultSet results = SparqlUtil.externalQuery(queryString, wikidataEndPoint);

		ArrayList<String> allSuperClasses = new ArrayList<String>();
		while (results.hasNext()){
			QuerySolution row = results.next();
			String superClass = row.get("superClass").toString();
			allSuperClasses.add(superClass);	
		}
		return allSuperClasses;
	}

	public static String getLabel(String classUri) {
		String queryString = ""
				+ PREFIXES
				+ "SELECT DISTINCT *\n"
				+ "WHERE {\n"
				+ "	<" + classUri + "> rdfs:label ?label .\n"
				+ "	filter(lang(?label)='en') . \n"
				+ "}";
		
		ResultSet results = SparqlUtil.externalQuery(queryString, wikidataEndPoint);

		while (results.hasNext()){
			QuerySolution row = results.next();
			String label = row.get("label").toString();
			return label;
		}
		return "";
	}
}
