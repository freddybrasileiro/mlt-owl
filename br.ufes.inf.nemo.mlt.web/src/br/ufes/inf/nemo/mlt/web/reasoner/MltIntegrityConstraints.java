package br.ufes.inf.nemo.mlt.web.reasoner;

import java.util.HashMap;
import java.util.List;

import br.ufes.inf.nemo.mlt.web.reasoner.exceptions.MltInconsistencyException;
import br.ufes.inf.nemo.mlt.web.reasoner.owl.OwlUtil;
import br.ufes.inf.nemo.mlt.web.reasoner.sparql.MltDerivationRulesSparql;
import br.ufes.inf.nemo.mlt.web.reasoner.sparql.MltIntegrityConstraintsSparql;
import br.ufes.inf.nemo.mlt.web.reasoner.sparql.MltSparqlUtil;
import br.ufes.inf.nemo.mlt.web.util.MltUtil;
import br.ufes.inf.nemo.mlt.web.vocabulary.MLT;

import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.vocabulary.OWL2;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

public class MltIntegrityConstraints {
	private OwlUtil owlUtil;
	private String logMsg = "";
	
	public MltIntegrityConstraints(OwlUtil owlUtil) {
		this.owlUtil = owlUtil;
	}
	
	public String getLogMsg() {
		if(!logMsg.isEmpty()){
			return "CONSISTENCY MESSAGES\n" + logMsg;
		}
		return logMsg;
	}
	
	public void checkConsistency() throws MltInconsistencyException{
		checkIC1();
		checkD4andD5();
		checkIC3();
		checkIC4();
		checkIC5();
		checkIC6();
		checkIC7();
		checkDomainsAndRanges();
		checkA6();		
	}

	private void checkIC1() {
		List<HashMap<String, String>> a1Results = MltIntegrityConstraintsSparql.ic1(owlUtil.getOwlModel());
		
		if(a1Results.size() > 0){
			System.out.println("Inconsistencies by IC1 (A1)");
			for (HashMap<String, String> hashMap : a1Results) {
				MltUtil.printTriple(hashMap.get("x"), "rdf:type", "mlt:TokenIndividual", owlUtil.getOwlModelPrefix());
				MltUtil.printTriple(hashMap.get("y"), "rdf:type", hashMap.get("x"), owlUtil.getOwlModelPrefix());
				
				System.out.println();
			}
		}
	}
	
	private void checkIC3() {
		List<HashMap<String, String>> t5Results = MltIntegrityConstraintsSparql.ic3(owlUtil.getOwlModel());
		
		if(t5Results.size() > 0){
			System.out.println("Inconsistencies by IC3 (T5)");
			for (HashMap<String, String> hashMap : t5Results) {
				if(hashMap.get("x").contains(MLT.getURI())) continue;
				if(hashMap.get("y").contains(MLT.getURI())) continue;
				MltUtil.printTriple(hashMap.get("x"), "rdf:type", hashMap.get("y"), owlUtil.getOwlModelPrefix());
				MltUtil.printTriple(hashMap.get("y"), "rdf:type", hashMap.get("x"), owlUtil.getOwlModelPrefix());
				
				System.out.println();
			}
		}
	}
	
	private void checkIC4() {
		List<HashMap<String, String>> t6Results = MltIntegrityConstraintsSparql.ic4(owlUtil.getOwlModel());
		
		if(t6Results.size() > 0){
			System.out.println("Inconsistencies by IC4 (T6)");
			for (HashMap<String, String> hashMap : t6Results) {
				if(!hashMap.get("x").contains(MLT.getURI())
						&& !hashMap.get("y").contains(MLT.getURI())
						&& !hashMap.get("z").contains(MLT.getURI())) continue;
				//if(hashMap.get("x").contains(MLT.getURI())) continue;
				//if(hashMap.get("y").contains(MLT.getURI())) continue;
//				if(hashMap.get("z").contains(MLT.getURI())) continue;
				MltUtil.printTriple(hashMap.get("x"), "rdf:type", hashMap.get("y"), owlUtil.getOwlModelPrefix());
				MltUtil.printTriple(hashMap.get("y"), "rdf:type", hashMap.get("z"), owlUtil.getOwlModelPrefix());
				MltUtil.printTriple(hashMap.get("x"), "rdf:type", hashMap.get("z"), owlUtil.getOwlModelPrefix());				
				System.out.println();
			}
		}
	}
	
	private void checkIC5() throws MltInconsistencyException {
		List<HashMap<String, String>> t10Results = MltIntegrityConstraintsSparql.ic5(owlUtil.getOwlModel());
		
		//TODO
		if(t10Results.size() > 0)
			throw new MltInconsistencyException("");
	}

	private void checkIC6() throws MltInconsistencyException {
		List<HashMap<String, String>> t11Results = MltIntegrityConstraintsSparql.ic6(owlUtil.getOwlModel());
		
		//TODO
		if(t11Results.size() > 0)
			throw new MltInconsistencyException("");
	}

	private void checkIC7() throws MltInconsistencyException {
		List<HashMap<String, String>> t14Results = MltIntegrityConstraintsSparql.ic7(owlUtil.getOwlModel());
		
		//TODO
		if(t14Results.size() > 0)
			throw new MltInconsistencyException("");
	}

	//TODO
	private void checkD4andD5() {
		List<HashMap<String, String>> results = MltDerivationRulesSparql.getD4andD5(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : results) {
			String t1 = hashMap.get("t1");
			String t2 = hashMap.get("t2");
//			String t3 = hashMap.get("t3");
			
			Resource t1Rsrc = owlUtil.getOwlModel().createResource(t1);
			Resource t2Rsrc = owlUtil.getOwlModel().createResource(t2);
			
			boolean isPowertypeOf = MltSparqlUtil.ask(owlUtil.getOwlModel(), t1Rsrc, MLT.isPowertypeOf, t2Rsrc);
			boolean characterizes = MltSparqlUtil.ask(owlUtil.getOwlModel(), t1Rsrc, MLT.characterizes, t2Rsrc);
			List<Statement> t1IsPowertypeOf = owlUtil.getOwlModel().listStatements(t1Rsrc, MLT.isPowertypeOf, (RDFNode)null).toList();
			List<Statement> t2IsBaseType = owlUtil.getOwlModel().listStatements(null, MLT.isPowertypeOf, t2Rsrc).toList();
			
			t1 = t1.replace(owlUtil.getOwlModelPrefix(), "").replace(MLT.getURI(), "mlt:");
			t2 = t2.replace(owlUtil.getOwlModelPrefix(), "").replace(MLT.getURI(), "mlt:");
			if(!isPowertypeOf && !characterizes && t1IsPowertypeOf.size() == 0 && t2IsBaseType.size() == 0){
				logMsg += "Following A11 and A12, [" + t1 + ", mlt:isPowertypeOf, " + t2 + "] or [" + t1 + ", mlt:characterizes, " + t2 + "]\n";
			}
		}
	}
	
	//TODO
	private void checkDomainsAndRanges() {
		List<HashMap<String, String>> results = MltSparqlUtil.getDomainAndRangeFromMltOP(owlUtil.getOwlModel());
		
		for (HashMap<String, String> hashMap : results) {
			String mltOP = hashMap.get("mltOP").replace(MLT.getURI(), "").replace(RDFS.getURI(), "").replace(RDF.getURI(), "");
			String xType = hashMap.get("xType");
			String yType = hashMap.get("yType");
			String x = hashMap.get("x").replace(MLT.getURI(), "mlt:");
			String y = hashMap.get("y").replace(MLT.getURI(), "mlt:");
			
			switch (mltOP) {
				case "subClassOf":
					if(!xType.equals(yType)
							|| xType.equals(MLT.TokenIndividual.toString()) || 
							yType.equals(MLT.TokenIndividual.toString())){
						logMsg += x + " rdfs:subClassOf " + y + ": The domain and range of rdfs:subClassOf must be types of the same order (instances of 1stOT, 2ndOT or 3rdOT).\n";
					}
					break;
				case "isSubordinatedTo":
					if(!xType.equals(yType) 
							|| xType.equals(MLT.TokenIndividual.toString()) 
							|| yType.equals(MLT.TokenIndividual.toString())
							|| xType.equals(MLT._1stOrderClass.toString()) 
							|| yType.equals(MLT._1stOrderClass.toString())){
						logMsg += x + " mlt:isSubordinatedTo " + y + ": The domain and range of mlt:isSubordinatedTo must be higher-order types of the same order (instances of 2ndOT or 3rdOT).\n";
					}
					break;
				case "type":
					if(!(
							(xType.equals(MLT._1stOrderClass.toString()) && yType.equals(MLT.TokenIndividual.toString()))
							||(xType.equals(MLT._2ndOrderClass.toString()) && yType.equals(MLT._1stOrderClass.toString()))
							||(xType.equals(MLT._3rdOrderClass.toString()) && yType.equals(MLT._2ndOrderClass.toString()))
						)){
						logMsg += x + " rdf:type " + y + ": The domain and range of rdf:type must be elements of adjacent levels.\n";
					}
					break;
				case "isPowertypeOf":
				case "characterizes":
					if(!(
							(xType.equals(MLT._2ndOrderClass.toString()) && yType.equals(MLT._1stOrderClass.toString()))
							||(xType.equals(MLT._3rdOrderClass.toString()) && yType.equals(MLT._2ndOrderClass.toString()))
						)){
						logMsg += x + " -> " + y + ": The domain and range of mlt:isPowertypeOf and mlt:characterizes must be types of adjacent levels (2ndOT->1stOT or 3rdOT->2ndOT ).\n";
					}
					break;
				}
		}		
	}

	//TODO
	private void checkA6(){
		List<Resource> subjects = owlUtil.getOwlModel().listSubjects().toList();
		
		for (Resource subject : subjects) {
			if(		subject.getNameSpace() == null || //ignoring classes generate, for example, by intersections
					!subject.getNameSpace().equals(owlUtil.getOwlModelPrefix()) //ignoring the classes that are external to the current model
				) {
				continue;
			}
			List<Statement> indiv = owlUtil.getOwlModel().listStatements(subject, RDF.type, MLT.TokenIndividual).toList();
			if(indiv.size() > 0) continue;
			List<Statement> _1st = owlUtil.getOwlModel().listStatements(subject, RDF.type, MLT._1stOrderClass).toList();
			if(_1st.size() > 0) continue;
			List<Statement> _2nd = owlUtil.getOwlModel().listStatements(subject, RDF.type, MLT._2ndOrderClass).toList();
			if(_2nd.size() > 0) continue;
			List<Statement> _3rd = owlUtil.getOwlModel().listStatements(subject, RDF.type, MLT._3rdOrderClass).toList();
			if(_3rd.size() > 0) continue;
			List<Statement> objectProperty = owlUtil.getOwlModel().listStatements(subject, RDF.type, OWL2.ObjectProperty).toList();
			if(objectProperty.size() > 0) continue;
			List<Statement> ontology = owlUtil.getOwlModel().listStatements(subject, RDF.type, OWL2.Ontology).toList();
			if(ontology.size() > 0) continue;
			logMsg += "Warning: \"" + subject.getLocalName() + "\" should be instance of one MLT basic types. Two possible reasons: the Knowlodge base is incomplete or you a higher order type.\n";
		}		
	}
}
