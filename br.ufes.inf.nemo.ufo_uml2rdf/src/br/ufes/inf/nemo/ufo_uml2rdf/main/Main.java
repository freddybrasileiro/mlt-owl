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
import com.hp.hpl.jena.vocabulary.OWL2;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
import com.hp.hpl.jena.vocabulary.XSD;

public class Main {
	public static String rdfs = RDFS.getURI();
	public static String rdf = RDF.getURI();
	public static String owl = OWL2.getURI();
	
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
//			ontModel.setNsPrefix(weaselPrefix, weaselNs);
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
			
			Resource ufoNsRsrc = ontModel.createResource(ufoNs);
			
			/**
			 * Getting all classes to map for RDFS.Class
			 * */
			List<Object> allClasses = eaXMIParser.getAllElements(eaXMIParser.getRoot(), ElementType.CLASS);
			for (Object classObj : allClasses) {
				
				String className = ((String) eaXMIParser.getProperty(classObj, "name")).replace(" ", "_");
				String ns;
				if(className.contains("OT") || className.contains("Individual")){
					ns = mltNs;
				}else{
					ns = ufoNs;
				}
				Resource classRsrc = ontModel.createResource(ns+className, RDFS.Class);
				classRsrc.addProperty(RDFS.isDefinedBy, ufoNsRsrc);
				classRsrc.addProperty(RDFS.label, className);

				/**
				 * Getting all generalizations of a Class, to map for RDFS.subClassOf
				 * */
				List<Object> gens = eaXMIParser.getAllElements(classObj, ElementType.GENERALIZATION);
				for (Object genObj : gens) {
					Object general = eaXMIParser.getProperty(genObj, "general");
					String generalName = ((String) eaXMIParser.getProperty(general, "name")).replace(" ", "_");
					if(generalName.contains("OT") || generalName.contains("Individual")){
						ns = mltNs;
					}else{
						ns = ufoNs;
					}
					Resource generalRsrc = ontModel.getResource(ns+generalName);
					classRsrc.addProperty(RDFS.subClassOf, generalRsrc);
				}
			}	
			
			/**
			 * Getting all generalization sets (GS), to map GS properties (isComplete and isDisjoint) for OWL.disjointUnionOf, OWL.unionOf or ...
			 * */
			List<Object> genSets = eaXMIParser.getAllElements(eaXMIParser.getRoot(), ElementType.GENERALIZATIONSET);
			for (Object genSetObj : genSets) {
				boolean isComplete = Boolean.valueOf((String) eaXMIParser.getProperty(genSetObj, "iscovering"));
				boolean isDisjoint = Boolean.valueOf((String) eaXMIParser.getProperty(genSetObj, "isdisjoint"));
				
				/**
				 * Getting generalizations of a GS
				 * */
				@SuppressWarnings("unchecked")
				List<Object> generalizations = (List<Object>) eaXMIParser.getProperty(genSetObj, "generalization");
				Resource generalClass = null;
				RDFNode[] nodeList = new RDFNode[generalizations.size()];
				int i = 0;
				for (Object gen : generalizations) {
					Object general = eaXMIParser.getProperty(gen, "general");
					String generalName = ((String) eaXMIParser.getProperty(general, "name")).replace(" ", "_");
					String ns;
					if(generalName.contains("OT") || generalName.contains("Individual")){
						ns = mltNs;
					}else{
						ns = ufoNs;
					}
					generalClass = ontModel.createResource(ns+generalName, RDFS.Class);
					Object specific = eaXMIParser.getOwner(gen);
					String specificName = ((String) eaXMIParser.getProperty(specific, "name")).replace(" ", "_");
					if(specificName.contains("OT") || specificName.contains("Individual")){
						ns = mltNs;
					}else{
						ns = ufoNs;
					}
					Resource specificClass = ontModel.createResource(ns+specificName, RDFS.Class);
					/**
					 * Here, the I make a RDFNode[] with the specific classes of the GS				 *  
					 */
					nodeList[i] = specificClass;
					i++;
				}				
				
				RDFList list = ontModel.createList(nodeList);
				Property property = null;
				if(isComplete && isDisjoint){
					property = ontModel.createProperty(OWL2.disjointUnionOf.toString());
				}else if(isComplete){
					property = ontModel.createProperty(OWL2.unionOf.toString());
				}else if(isDisjoint){
					property = ontModel.createProperty(OWL2.AllDisjointClasses.toString());
				}
				
				/**
				 * this part must be fixed... this IF condition cannot exist...
				 * this is only here because the uncertainty of how to map the Disjointness
				 */
				Statement t2;
				if(!isComplete && isDisjoint){
					Resource blank = ontModel.createResource();
					t2 = ontModel.createStatement(blank, property, list);
				}else{
					t2 = ontModel.createStatement(generalClass, property, list);					
				}
				ontModel.add(t2);
				
				
			}
			
			ArrayList<String> assocNames = new ArrayList<String>();
			List<Object> associations = eaXMIParser.getAllElements(eaXMIParser.getRoot(), ElementType.ASSOCIATION);
			for (Object assoc : associations) {
				String name = String.valueOf((String) eaXMIParser.getProperty(assoc, "name"));
				if(!assocNames.contains(name)){
					assocNames.add(name);
					//criar relação
				}
			}
			
			List<Object> dependencies = eaXMIParser.getAllElements(eaXMIParser.getRoot(), ElementType.DEPENDENCY);			
//			associations.addAll(dependencies);
			for (Object assoc : dependencies) {
				String name = String.valueOf((String) eaXMIParser.getProperty(assoc, "name"));
				
				String supplierId = String.valueOf((String) eaXMIParser.getProperty(assoc, "supplier"));
				Object supplier = eaXMIParser.getElementById(supplierId);
				String supplierName = ((String) eaXMIParser.getProperty(supplier, "name")).replace(" ", "_");
				String ns;
				if(supplierName.contains("OT") || supplierName.contains("Individual")){
					ns = mltNs;
				}else{
					ns = ufoNs;
				}
				Resource class1 = ontModel.createResource(ns+supplierName, RDFS.Class);
				
				String clientId = String.valueOf((String) eaXMIParser.getProperty(assoc, "client"));
				Object client = eaXMIParser.getElementById(clientId);
				String clientName = ((String) eaXMIParser.getProperty(client, "name")).replace(" ", "_");
				if(clientName.contains("OT") || clientName.contains("Individual")){
					ns = mltNs;
				}else{
					ns = ufoNs;
				}
				Resource class2 = ontModel.createResource(ns+clientName, RDFS.Class);
				
				Resource restriction = ontModel.createResource(OWL2.Restriction);;
				
				Resource objectProperty = ontModel.createResource(mltNs+name, OWL2.ObjectProperty);
				Statement stmt1 = ontModel.createStatement(restriction, OWL2.onProperty, objectProperty);
				ontModel.add(stmt1);
				Statement stmt2 = ontModel.createStatement(restriction, OWL2.qualifiedCardinality, ontModel.createTypedLiteral(1));
				ontModel.add(stmt2);
				Statement stmt3 = ontModel.createStatement(restriction, OWL2.onClass, class2);
				ontModel.add(stmt3);
				Statement stmt4 = ontModel.createStatement(class1, RDFS.subClassOf, restriction);
				ontModel.add(stmt4);				
			}
			
			/**
			 * Here, I need to discuss with JP to define how tag different parts
			 * of the model with different URIs
			 */
			//List<Object> tagsIsDefinedBy = eaXMIParser.getAllElements(eaXMIParser.getRoot(), ElementType.ISDEFINEDBY);
			
			saveRdf(ontModel, ufoPrefix);
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
