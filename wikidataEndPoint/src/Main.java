import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

public class Main {
	
	static public void main(String...argv)
    {
		Model newModel = ModelFactory.createDefaultModel();
		InputStream in1 = FileManager.get().open( "input.owl" );
        if (in1 == null) {
            throw new IllegalArgumentException( "File: not found");
        }
        
        // read the RDF/XML file
        newModel.read(in1, "");
        
		getBiologicalTaxonomicHierarchy(newModel);
		
		try {
			OwlFileUtil.saveOwlOntology(newModel, "output.owl");
		} catch (Exception e) {
			// TODO Auto-generated catch block
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
		getTaxonomicHierarchyAbove(classUri, newModel, alreadyVisited, 0);	
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
		
		if(classUri.equals("https://www.wikidata.org/wiki/Q20739486") || classUri.equals("https://www.wikidata.org/wiki/Q140") || classUri.equals("https://www.wikidata.org/wiki/Q7432")){
			System.out.println();
		}
//		System.out.println(".");
		String label = WikidataUtil.getLabel(classUri);
		label = label.replace("@en", "");
		if(!label.equals("")){
			System.out.print(label);
			Statement labelStmt = newModel.createStatement(newModel.createResource(classUri), RDFS.label, newModel.createLiteral(label));
			newModel.add(labelStmt);
		}
		else{
			return;
		}
		
		ArrayList<String> superClasses = WikidataUtil.getSuperClassesOf(classUri);
		
		for (String superClassUri : superClasses) {
			Statement subClassOfStmt = newModel.createStatement(newModel.createResource(classUri), RDFS.subClassOf, newModel.createResource(superClassUri));
			newModel.add(subClassOfStmt);
		}
		
		//get all instances
		ArrayList<String> instances = WikidataUtil.getInstancesOf(classUri);
		
		level++;
		String tabs = "";
		for (int j = 0; j < level; j++) {
			tabs+="\t";
		}
		//for (int i = 0; i < 100 && i < instances.size(); i++) {
		for (int i = 0; i < instances.size(); i++) {
			
			System.out.println();
			System.out.print(tabs);
			System.out.print("(level " + level + ") ");
			System.out.print(instances.indexOf(instances.get(i)) + " of " + instances.size());
			System.out.print(" - ");
//			System.out.println(".");
			Statement iofStmt = newModel.createStatement(newModel.createResource(instances.get(i)), RDF.type, newModel.createResource(classUri));
			newModel.add(iofStmt);
			
			//call recursion
			getTaxonomicHierarchyAbove(instances.get(i), newModel, alreadyVisited, level);
		}
	}
}