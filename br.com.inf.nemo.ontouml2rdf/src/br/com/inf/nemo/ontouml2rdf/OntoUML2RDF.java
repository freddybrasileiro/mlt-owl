package br.com.inf.nemo.ontouml2rdf;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Set;

import net.menthor.common.transformation.OWLTransformationOptions;
import RefOntoUML.Association;
import RefOntoUML.Property;
import RefOntoUML.Type;
import RefOntoUML.parser.OntoUMLParser;
import br.ufes.inf.nemo.ufo_rdf.vocabulary.MLT;
import br.ufes.inf.nemo.ufo_rdf.vocabulary.UFO;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.vocabulary.RDFS;

public class OntoUML2RDF {
	OWLTransformationOptions owlOptions;
	String ontologyIRI;
	String ontologyNS;
	OntoUMLParser ontoParser;
	OntModel rdfModel;
	
	public OntoUML2RDF(OWLTransformationOptions owlOptions, RefOntoUML.Package ontModel, String ontologyIRI) {
		this.ontoParser = new OntoUMLParser(ontModel);
		this.rdfModel = ModelFactory.createOntologyModel( OntModelSpec.RDFS_MEM );
		this.ontologyIRI = ontologyIRI;
		this.owlOptions = owlOptions;
		this.ontologyNS = ontologyIRI + "#";
	}
	
	public String transform(){
		rdfModel.setNsPrefix(UFO.getPrefix(), UFO.getURI());
		rdfModel.setNsPrefix(MLT.getPrefix(), MLT.getURI());
		
		Set<RefOntoUML.Class> lstClasses = ontoParser.getAllInstances(RefOntoUML.Class.class);
		for (RefOntoUML.Class cls : lstClasses) {
			String stereotype = ontoParser.getStereotype(cls);
			Resource stereotypeRsrc = UFO.resource(stereotype);
			rdfModel.createResource(ontologyIRI+ "#" + cls.getName(), stereotypeRsrc);
		}
		
		Set<RefOntoUML.Association> lstAssociations = ontoParser.getAllInstances(RefOntoUML.Association.class);
		for (RefOntoUML.Association assoc : lstAssociations) {
			String stereotype = ontoParser.getStereotype(assoc);
			Resource stereotypeRsrc = UFO.resource(stereotype);
			Resource assocRrsc = rdfModel.createResource(ontologyNS + assoc.getName(), stereotypeRsrc);
			Type source = assoc.getMemberEnd().get(0).getType();
			Resource domainRsrc = rdfModel.createResource(ontologyNS + source.getName());
			Type range = assoc.getMemberEnd().get(1).getType();
			Resource rangeRsrc = rdfModel.createResource(ontologyNS + range.getName());
			
			Statement domainStatement = rdfModel.createStatement(assocRrsc, RDFS.domain, domainRsrc);
			rdfModel.add(domainStatement);
			Statement rangeStatement = rdfModel.createStatement(assocRrsc, RDFS.range, rangeRsrc);
			rdfModel.add(rangeStatement);
			
			
		}	
		
		return ontModelToString();
	}
	
	private String ontModelToString(){
		String syntax = "RDF/XML-ABBREV";
		StringWriter out = new StringWriter();
		rdfModel.write(out, syntax);
		String result = out.toString();
		return result;
	}
}
