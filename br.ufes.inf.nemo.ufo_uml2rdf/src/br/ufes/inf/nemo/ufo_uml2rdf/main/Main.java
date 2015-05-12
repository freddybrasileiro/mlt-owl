package br.ufes.inf.nemo.ufo_uml2rdf.main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
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
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.OWL2;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
import com.hp.hpl.jena.vocabulary.XSD;

public class Main {
	public static String rdfs = "http://www.w3.org/2000/01/rdf-schema#";
	public static String rdf = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	public static String owl = "http://www.w3.org/2002/07/owl#";
	
	public static String nemoUrl = "http://www.nemo.inf.ufes.br/";
	
	public static String ufoPrefix = "ufo";
	public static String ufoNs = nemoUrl+ufoPrefix+"#";
	
	public static String mltPrefix = "mlt";
	public static String mltNs = nemoUrl+mltPrefix+"#";
	
	public static String weaselPrefix = "weasel";
	public static String weaselNs = nemoUrl+weaselPrefix+"#";
	
	public static String weaselUmlPrefix = "uml";
	public static String weaselUmlNs = nemoUrl+weaselUmlPrefix+"#";
	
	public static String weaselUmlInstancePrefix = "myOntology";
	public static String weaselUmlInstanceNs = nemoUrl+weaselUmlInstancePrefix+"#";
	
	
	public static void main(String[] args) {
		try {
			//String path = "C:\\Users\\fredd_000\\Documents\\Projetos\\master-thesis\\br.ufes.inf.nemo.ufo_uml2rdf\\";
			//String fileName = path+"ufo.xml";
			String fileName = "ufo2.xml";
			OntModel ontModel = ModelFactory.createOntologyModel( OntModelSpec.RDFS_MEM );
			ontModel.setNsPrefix(weaselPrefix, weaselNs);
			ontModel.setNsPrefix(ufoPrefix, ufoNs);
			ontModel.setNsPrefix(mltPrefix, mltNs);
			//Resource teste = ontModel.createResource(weaselNs, XSD.NCName);
			//ontModel.createOntology(weaselUrl);
//			ModelFactory.createon
//			String newURI="abc:def/";
//			Graph g=Factory.createDefaultGraph();
//			PrefixMapping pm=g.getPrefixMapping();
//			pm.setNsPrefix(ns,newURI);
//			Model m=ModelFactory.createModelForGraph(g);
//			assertEquals(newURI,m.getNsPrefixURI(ns));
//			assertEquals(prefix,m.getNsPrefixURI(prefix));
			
			EAXMIParser eaXMIParser = new EAXMIParser(fileName);
			
			Resource weaselNsRsrc = ontModel.createResource(weaselNs);
			
			List<Object> allClasses = eaXMIParser.getAllElements(eaXMIParser.getRoot(), ElementType.CLASS);
			for (Object classObj : allClasses) {
				
				//DeferredElementNSImpl deClass = (DeferredElementNSImpl) classObj;
				String className = ((String) eaXMIParser.getProperty(classObj, "name")).replace(" ", "_");
				//String className = deClass.getAttribute("name").replace(" ", "_");
				Resource classRsrc = ontModel.createResource(weaselNs+className, RDFS.Class);
				classRsrc.addProperty(RDFS.isDefinedBy, weaselNsRsrc);
				classRsrc.addProperty(RDFS.label, className);
				
				List<Object> gens = eaXMIParser.getAllElements(classObj, ElementType.GENERALIZATION);
				for (Object genObj : gens) {
					//DeferredElementNSImpl deGen = (DeferredElementNSImpl) genObj;
					//String generalId = deGen.getAttribute("general");
					//Map<String, Object> teste = eaXMIParser.getProperties(genObj);
					Object general = eaXMIParser.getProperty(genObj, "general");
					String generalName = ((String) eaXMIParser.getProperty(general, "name")).replace(" ", "_");
					Resource generalRsrc = ontModel.getResource(weaselNs+generalName);
//					String className = deClass.getAttribute("name").replace(" ", "_");
//					Resource classRsrc = ontModel.createResource(weaselNs+className, RDFS.Class);
					classRsrc.addProperty(RDFS.subClassOf, generalRsrc);
					
					//System.out.println();
				}
			}	
			
			List<Object> genSets = eaXMIParser.getAllElements(eaXMIParser.getRoot(), ElementType.GENERALIZATIONSET);
			for (Object genSetObj : genSets) {
				boolean isComplete = Boolean.valueOf((String) eaXMIParser.getProperty(genSetObj, "iscovering"));
				System.out.println(isComplete);
				boolean isDisjoint = Boolean.valueOf((String) eaXMIParser.getProperty(genSetObj, "isdisjoint"));
				System.out.println(isDisjoint);
				List<Object> generalizations = (List<Object>) eaXMIParser.getProperty(genSetObj, "generalization");
				
				List<Resource> specificClasses = new ArrayList<Resource>();
				Resource generalClass = null;
				for (Object gen : generalizations) {
					Object general = eaXMIParser.getProperty(gen, "general");
					String generalName = ((String) eaXMIParser.getProperty(general, "name")).replace(" ", "_");
					generalClass = ontModel.createResource(weaselNs+generalName, RDFS.Class);
					System.out.println(generalName);
					Object specific = eaXMIParser.getOwner(gen);
					String specificName = ((String) eaXMIParser.getProperty(specific, "name")).replace(" ", "_");
					System.out.println(specificName);
					Resource specificClass = ontModel.createResource(weaselNs+specificName, RDFS.Class);
					specificClasses.add(specificClass);
					System.out.println();
				}				
				
				RDFNode[] nodeList = new RDFNode[specificClasses.size()];
				for (int i = 0; i < nodeList.length; i++) {
					nodeList[i] = specificClasses.get(i);
				}
				RDFList list = ontModel.createList(nodeList);
				Property property = null;
				if(isComplete && isDisjoint){
					property = ontModel.createProperty(owl+"disjointUnionOf");
				}else if(isComplete){
					property = ontModel.createProperty(owl+"unionOf");
				}else if(isDisjoint){
					//property = ontModel.createProperty(owl+"disjointUnionOf");
				}
				if(isComplete && isDisjoint || isComplete){
					Statement t2 = ontModel.createStatement(generalClass, property, list);
					ontModel.add(t2);
				}
			}
			
			//List<Object> tagsIsDefinedBy = eaXMIParser.getAllElements(eaXMIParser.getRoot(), ElementType.ISDEFINEDBY);
			
			//ontModel.write(System.out, "RDF/XML-ABBREV");
			
			saveRdf(ontModel, ufoPrefix);
			//createweaselUml();
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
	
	public static void createweaselUml(){
		OntModel ontModel = ModelFactory.createOntologyModel( OntModelSpec.RDFS_MEM );
		ontModel.setNsPrefix(weaselUmlPrefix, weaselUmlNs);
		
		Resource gsRsrc = ontModel.createResource(weaselUmlNs+"Generalization_Set", RDFS.Class);
		
		Resource isCompleteRsrc = ontModel.createResource(weaselUmlNs+"isCovering", RDF.Property);
		Property isCompleteProp = ontModel.createProperty(weaselUmlNs+"isCovering");
		isCompleteRsrc.addProperty(RDFS.domain, gsRsrc);
		isCompleteRsrc.addProperty(RDFS.range, XSD.xboolean);
		
		Resource isDisjointRsrc = ontModel.createResource(weaselUmlNs+"isDisjoint", RDF.Property);
		Property isDisjointProp = ontModel.createProperty(weaselUmlNs+"isDisjoint");
		isDisjointRsrc.addProperty(RDFS.domain, gsRsrc);
		isDisjointRsrc.addProperty(RDFS.range, XSD.xboolean);
		
		Resource nameRsrc = ontModel.createResource(weaselUmlNs+"name", RDF.Property);
		Property nameProp = ontModel.createProperty(weaselUmlNs+"name");
		nameRsrc.addProperty(RDFS.domain, gsRsrc);
		nameRsrc.addProperty(RDFS.range, XSD.xstring);
		
		Resource gsSuperRsrc = ontModel.createResource(weaselUmlNs+"general", RDF.Property);
		Property gsSuperProp = ontModel.getProperty(weaselUmlNs+"general");
		gsSuperRsrc.addProperty(RDFS.domain, gsRsrc);
		gsSuperRsrc.addProperty(RDFS.range, RDFS.Class);
		
		Resource gsSubtypesRsrc = ontModel.createResource(weaselUmlNs+"subtypes", RDF.Property);
		Property gsSubtypesProp = ontModel.createProperty(weaselUmlNs+"subtypes");
		gsSubtypesProp.addProperty(RDFS.domain, gsRsrc);
		gsSubtypesProp.addProperty(RDFS.range, RDF.List);
		
		saveRdf(ontModel, weaselUmlPrefix);
		/////////////////////////////////////////////////////////////////
		
		ontModel = ModelFactory.createOntologyModel( OntModelSpec.RDFS_MEM );
		ontModel.setNsPrefix(weaselUmlPrefix, weaselUmlNs);
		ontModel.setNsPrefix(weaselUmlInstancePrefix, weaselUmlInstanceNs);
		ontModel.setNsPrefix(weaselPrefix, weaselNs);
		
		Resource kind = ontModel.createResource(weaselNs+"Kind");
		Resource subkind = ontModel.createResource(weaselNs+"Subkind");
		Resource phase = ontModel.createResource(weaselNs+"Phase");
		
		Resource person = ontModel.createResource(weaselUmlInstanceNs+"Person", kind);
		
		Resource man = ontModel.createResource(weaselUmlInstanceNs+"Man", subkind);
		man.addProperty(RDFS.subClassOf, person);
		Resource woman = ontModel.createResource(weaselUmlInstanceNs+"Woman", subkind);
		woman.addProperty(RDFS.subClassOf, person);
		
		Resource genderGS = ontModel.createResource(weaselUmlInstanceNs+"genderGS");
		genderGS.addProperty(RDF.type, gsRsrc);
		genderGS.addProperty(gsSuperProp, person);
		genderGS.addLiteral(isDisjointProp, false);
		genderGS.addLiteral(nameProp, "genderGS");
		genderGS.addLiteral(isCompleteProp, true);
		
		RDFNode[] nodeList = {man,woman};
		RDFList list = ontModel.createList(nodeList);
		Statement t2 = ontModel.createStatement(genderGS, gsSubtypesProp, list);
		ontModel.add(t2);
		
		Resource child = ontModel.createResource(weaselUmlInstanceNs+"child", phase);
		child.addProperty(RDFS.subClassOf, person);
		Resource teenager = ontModel.createResource(weaselUmlInstanceNs+"teenager", phase);
		teenager.addProperty(RDFS.subClassOf, person);
		Resource adult = ontModel.createResource(weaselUmlInstanceNs+"adult", phase);
		adult.addProperty(RDFS.subClassOf, person);
				
		Resource lifePhaseGS = ontModel.createResource(weaselUmlInstanceNs+"lifePhaseGS");
		lifePhaseGS.addProperty(RDF.type, gsRsrc);
		lifePhaseGS.addProperty(gsSuperProp, person);
		lifePhaseGS.addLiteral(isDisjointProp, false);
		lifePhaseGS.addLiteral(nameProp, "lifePhaseGS");
		lifePhaseGS.addLiteral(isCompleteProp, true);
		
		RDFNode[] lifePhaseNodeList = {child,teenager,adult};
		RDFList lifePhaseList = ontModel.createList(lifePhaseNodeList);
		Statement t3 = ontModel.createStatement(lifePhaseGS, gsSubtypesProp, lifePhaseList);
		ontModel.add(t3);

		
		saveRdf(ontModel, weaselUmlInstancePrefix);
		
	}
}
