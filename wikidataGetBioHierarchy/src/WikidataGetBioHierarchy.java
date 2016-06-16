import java.util.ArrayList;
import java.util.HashMap;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;

public class WikidataGetBioHierarchy {
	
	static public void main(String...argv)
    {
		Model newModel = ModelFactory.createDefaultModel();
//		InputStream in1 = FileManager.get().open( "input.owl" );
//        if (in1 == null) {
//            throw new IllegalArgumentException( "File: not found");
//        }
//        
//        // read the RDF/XML file
//        newModel.read(in1, "");
        
		newModel.setNsPrefix("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
		newModel.setNsPrefix("owl", "http://www.w3.org/2002/07/owl#");
		newModel.setNsPrefix("xsd", "http://www.w3.org/2001/XMLSchema#");
		newModel.setNsPrefix("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
		newModel.setNsPrefix("wd", "http://www.wikidata.org/entity/");
		newModel.setNsPrefix("wikidata", "https://www.wikidata.org/");
		newModel.setNsPrefix("", "https://www.wikidata.org/");
		
		Resource ontology = newModel.createResource("https://www.wikidata.org/");
		Statement stmt = newModel.createStatement(ontology, RDF.type, OWL.Ontology);
		newModel.add(stmt);
		
		getBiologicalTaxonomicHierarchy(newModel);
		
		try {
			OwlFileUtil.saveOwlOntology(newModel, "output.owl");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Done!");
    }
	
	/**
	 * This function get all taxonomic hierarchy of the model,
	 * i.e., get all classes and their subclasses considering owl:Thing (including it) as the root
	 */
	public static void getBiologicalTaxonomicHierarchy(Model newModel){
		String classUri = "http://www.wikidata.org/entity/Q427626";
		HashMap<String, String> alreadyVisited = new HashMap<String, String>();
		getTaxonomicHierarchyAbove(classUri, newModel, alreadyVisited, 3);	
		System.out.println("Visited entities: " + alreadyVisited.size());
	}
	
	/**
	 * This function print a given class name, get all its subclass
	 * and for each subclass call itself recursively.
	 * 
	 * The parameter "tabs" is a trick to print the taxonomic hierarchy nestled
	 * The algorith works like traveling a n-ary tree. See the original post in:
	 * http://stackoverflow.com/questions/5987867/traversing-a-n-ary-tree-without-using-recurrsion
	 * @param alreadyVisited 
	 */
	public static void getTaxonomicHierarchyAbove(String classUri, Model newModel, HashMap<String, String> alreadyVisited, int level){
		if(alreadyVisited.containsKey(classUri)){
			return;
		}else{
			alreadyVisited.put(classUri, classUri);
		}
		
//		if(classUri.equals("https://www.wikidata.org/wiki/Q20739486") || classUri.equals("https://www.wikidata.org/wiki/Q140") || classUri.equals("https://www.wikidata.org/wiki/Q7432")){
//			System.out.println();
//		}

//		String label = WikidataUtil.getLabel(classUri);
//		label = label.replace("@en", "");
//		if(!label.equals("")){
//			System.out.print(label);
//			Statement labelStmt = newModel.createStatement(newModel.createResource(classUri), RDFS.label, newModel.createLiteral(label));
//			newModel.add(labelStmt);
//		}
//		else{
//			return;
//		}
		
//		ArrayList<String> superClasses = WikidataUtil.getSuperClassesOf(classUri);
//		
//		System.out.print(" ("+ superClasses.size() + " superclass(es))");
//		
//		for (String superClassUri : superClasses) {
//			Statement subClassOfStmt = newModel.createStatement(newModel.createResource(classUri), RDFS.subClassOf, newModel.createResource(superClassUri));
//			newModel.add(subClassOfStmt);
//		}
		
		//get all instances
		ArrayList<String> instances = WikidataUtil.getInstancesOf(classUri);
		
		level--;
		String tabs = "";
		for (int j = level; j < 3; j++) {
			tabs+="\t";
		}
		
//		for (int i = 0; i < 100 && i < instances.size(); i++) {
		for (int i = 0; i < instances.size(); i++) {
			
			System.out.println();
			System.out.print(tabs);
			System.out.print("(" + level + " order) ");
			System.out.print((instances.indexOf(instances.get(i))+1) + " of " + instances.size());
			System.out.print(" - ");
//			System.out.println(".");
			Statement iofStmt = newModel.createStatement(newModel.createResource(instances.get(i)), RDF.type, newModel.createResource(classUri));
			newModel.add(iofStmt);
			
			//call recursion
			getTaxonomicHierarchyAbove(instances.get(i), newModel, alreadyVisited, level);
		}
	}
}