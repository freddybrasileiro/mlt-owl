package br.ufes.inf.nemo.ufo_uml2rdf.main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.StringWriter;
import java.util.List;

import net.menthor.xmi2ontouml.util.ElementType;
import net.menthor.xmi2ontouml.xmiparser.impl.EAXMIParser;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFList;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
import com.hp.hpl.jena.vocabulary.XSD;

public class Main {
	public static String rdfs = "http://www.w3.org/2000/01/rdf-schema#";
	public static String nemoUrl = "http://www.nemo.inf.ufes.br/";
	
	public static String foxPrefix = "fox";
	public static String foxNs = nemoUrl+foxPrefix+"#";
	
	public static String foxUmlPrefix = "uml";
	public static String foxUmlNs = nemoUrl+foxUmlPrefix+"#";
	
	public static String foxUmlInstancePrefix = "myOntology";
	public static String foxUmlInstanceNs = nemoUrl+foxUmlInstancePrefix+"#";
	
	
	public static void main(String[] args) {
		try {
			String path = "C:\\Users\\fredd_000\\Documents\\Projetos\\master-thesis\\br.ufes.inf.nemo.ufo_uml2rdf\\";
			String fileName = path+"ufo.xml";
			OntModel ontModel = ModelFactory.createOntologyModel( OntModelSpec.RDFS_MEM );
			ontModel.setNsPrefix(foxPrefix, foxNs);
			//Resource teste = ontModel.createResource(foxNs, XSD.NCName);
			//ontModel.createOntology(foxUrl);
//			ModelFactory.createon
//			String newURI="abc:def/";
//			Graph g=Factory.createDefaultGraph();
//			PrefixMapping pm=g.getPrefixMapping();
//			pm.setNsPrefix(ns,newURI);
//			Model m=ModelFactory.createModelForGraph(g);
//			assertEquals(newURI,m.getNsPrefixURI(ns));
//			assertEquals(prefix,m.getNsPrefixURI(prefix));
			
			EAXMIParser eaXMIParser = new EAXMIParser(fileName);
			
			Resource foxNsRsrc = ontModel.createResource(foxNs);
			
			List<Object> allClasses = eaXMIParser.getAllElements(eaXMIParser.getRoot(), ElementType.CLASS);
			for (Object classObj : allClasses) {
				
				//DeferredElementNSImpl deClass = (DeferredElementNSImpl) classObj;
				String className = ((String) eaXMIParser.getProperty(classObj, "name")).replace(" ", "_");
				//String className = deClass.getAttribute("name").replace(" ", "_");
				Resource classRsrc = ontModel.createResource(foxNs+className, RDFS.Class);
				classRsrc.addProperty(RDFS.isDefinedBy, foxNsRsrc);
				classRsrc.addProperty(RDFS.label, className);
				
				List<Object> gens = eaXMIParser.getAllElements(classObj, ElementType.GENERALIZATION);
				for (Object genObj : gens) {
					//DeferredElementNSImpl deGen = (DeferredElementNSImpl) genObj;
					//String generalId = deGen.getAttribute("general");
					//Map<String, Object> teste = eaXMIParser.getProperties(genObj);
					Object general = eaXMIParser.getProperty(genObj, "general");
					String generalName = ((String) eaXMIParser.getProperty(general, "name")).replace(" ", "_");
					Resource generalRsrc = ontModel.getResource(foxNs+generalName);
//					String className = deClass.getAttribute("name").replace(" ", "_");
//					Resource classRsrc = ontModel.createResource(foxNs+className, RDFS.Class);
					classRsrc.addProperty(RDFS.subClassOf, generalRsrc);
					
					//System.out.println();
				}
			}	
			
			List<Object> genSets = eaXMIParser.getAllElements(eaXMIParser.getRoot(), ElementType.GENERALIZATIONSET);
			for (Object genSetObj : genSets) {
				boolean isComplete = Boolean.valueOf((String) eaXMIParser.getProperty(genSetObj, "iscovering"));
				boolean isDisjoint = Boolean.valueOf((String) eaXMIParser.getProperty(genSetObj, "isdisjoint"));
				
				List<Object> generalizations = (List<Object>) eaXMIParser.getProperty(genSetObj, "generalization");
				
				for (Object gen : generalizations) {
					Object generalObj = eaXMIParser.getProperty(gen, "general");
					Object specObj = eaXMIParser.getOwner(gen);
					//System.out.println();
				}
				
				//System.out.println();
			}
			
			//ontModel.write(System.out, "RDF/XML-ABBREV");
			
			saveRdf(ontModel, foxPrefix);
			createFoxUml();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void saveRdf(OntModel ontModel, String rdfName){
		System.out.println("Saving RDF");
		String syntax = "RDF/XML-ABBREV";
		StringWriter out = new StringWriter();
		ontModel.write(out, syntax);
		String result = out.toString();
		File arquivo = new File(rdfName + ".rdf");  // Chamou e nomeou o arquivo txt.  
		if(arquivo.exists()){
			arquivo.delete();
		}
		try{
			FileOutputStream fos = new FileOutputStream(arquivo);  // Perceba que estamos instanciando uma classe aqui. A FileOutputStream. Pesquise sobre ela!  
			fos.write(result.getBytes());    
			fos.close();  // Fecha o arquivo. Nunca esquecer disso.  
		}catch(Exception e){
			e.printStackTrace();
		}
		ontModel.write(System.out, "RDF/XML-ABBREV");
	}
	
	public static void createFoxUml(){
		OntModel ontModel = ModelFactory.createOntologyModel( OntModelSpec.RDFS_MEM );
		ontModel.setNsPrefix(foxUmlPrefix, foxUmlNs);
		
		Resource gsRsrc = ontModel.createResource(foxUmlNs+"Generalization_Set", RDFS.Class);
		
		Resource isCompleteRsrc = ontModel.createResource(foxUmlNs+"isCovering", RDF.Property);
		Property isCompleteProp = ontModel.createProperty(foxUmlNs+"isCovering");
		isCompleteRsrc.addProperty(RDFS.domain, gsRsrc);
		isCompleteRsrc.addProperty(RDFS.range, XSD.xboolean);
		
		Resource isDisjointRsrc = ontModel.createResource(foxUmlNs+"isDisjoint", RDF.Property);
		Property isDisjointProp = ontModel.createProperty(foxUmlNs+"isDisjoint");
		isDisjointRsrc.addProperty(RDFS.domain, gsRsrc);
		isDisjointRsrc.addProperty(RDFS.range, XSD.xboolean);
		
		Resource nameRsrc = ontModel.createResource(foxUmlNs+"name", RDF.Property);
		Property nameProp = ontModel.createProperty(foxUmlNs+"name");
		nameRsrc.addProperty(RDFS.domain, gsRsrc);
		nameRsrc.addProperty(RDFS.range, XSD.xstring);
		
		Resource gsSuperRsrc = ontModel.createResource(foxUmlNs+"general", RDF.Property);
		Property gsSuperProp = ontModel.getProperty(foxUmlNs+"general");
		gsSuperRsrc.addProperty(RDFS.domain, gsRsrc);
		gsSuperRsrc.addProperty(RDFS.range, RDFS.Class);
		
		Resource gsSubtypesRsrc = ontModel.createResource(foxUmlNs+"subtypes", RDF.Property);
		Property gsSubtypesProp = ontModel.createProperty(foxUmlNs+"subtypes");
		gsSubtypesProp.addProperty(RDFS.domain, gsRsrc);
		gsSubtypesProp.addProperty(RDFS.range, RDF.List);
		
		saveRdf(ontModel, foxUmlPrefix);
		/////////////////////////////////////////////////////////////////
		
		ontModel = ModelFactory.createOntologyModel( OntModelSpec.RDFS_MEM );
		ontModel.setNsPrefix(foxUmlPrefix, foxUmlNs);
		ontModel.setNsPrefix(foxUmlInstancePrefix, foxUmlInstanceNs);
		ontModel.setNsPrefix(foxPrefix, foxNs);
		
		Resource kind = ontModel.createResource(foxNs+"Kind");
		Resource subkind = ontModel.createResource(foxNs+"Subkind");
		Resource phase = ontModel.createResource(foxNs+"Phase");
		
		Resource person = ontModel.createResource(foxUmlInstanceNs+"Person", kind);
		
		Resource man = ontModel.createResource(foxUmlInstanceNs+"Man", subkind);
		man.addProperty(RDFS.subClassOf, person);
		Resource woman = ontModel.createResource(foxUmlInstanceNs+"Woman", subkind);
		woman.addProperty(RDFS.subClassOf, person);
		
		Resource genderGS = ontModel.createResource(foxUmlInstanceNs+"genderGS");
		genderGS.addProperty(RDF.type, gsRsrc);
		genderGS.addProperty(gsSuperProp, person);
		genderGS.addLiteral(isDisjointProp, false);
		genderGS.addLiteral(nameProp, "genderGS");
		genderGS.addLiteral(isCompleteProp, true);
		
		RDFNode[] nodeList = {man,woman};
		RDFList list = ontModel.createList(nodeList);
		Statement t2 = ontModel.createStatement(genderGS, gsSubtypesProp, list);
		ontModel.add(t2);
		
		Resource child = ontModel.createResource(foxUmlInstanceNs+"child", phase);
		child.addProperty(RDFS.subClassOf, person);
		Resource teenager = ontModel.createResource(foxUmlInstanceNs+"teenager", phase);
		teenager.addProperty(RDFS.subClassOf, person);
		Resource adult = ontModel.createResource(foxUmlInstanceNs+"child", phase);
		adult.addProperty(RDFS.subClassOf, person);
				
		Resource lifePhaseGS = ontModel.createResource(foxUmlInstanceNs+"lifePhaseGS");
		lifePhaseGS.addProperty(RDF.type, gsRsrc);
		lifePhaseGS.addProperty(gsSuperProp, person);
		lifePhaseGS.addLiteral(isDisjointProp, false);
		lifePhaseGS.addLiteral(nameProp, "lifePhaseGS");
		lifePhaseGS.addLiteral(isCompleteProp, true);
		
		RDFNode[] lifePhaseNodeList = {child,teenager,adult};
		RDFList lifePhaseList = ontModel.createList(lifePhaseNodeList);
		Statement t3 = ontModel.createStatement(lifePhaseGS, gsSubtypesProp, lifePhaseList);
		ontModel.add(t3);

		
		saveRdf(ontModel, foxUmlInstancePrefix);
		
	}
}
