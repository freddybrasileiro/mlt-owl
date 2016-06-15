package br.ufes.inf.nemo.mlt.web.reasoner;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import br.ufes.inf.nemo.mlt.web.reasoner.exceptions.MltException;
import br.ufes.inf.nemo.mlt.web.reasoner.owl.OwlUtil;
import br.ufes.inf.nemo.mlt.web.vocabulary.MLT;

import com.hp.hpl.jena.ontology.OntModel;

public class MltReasoner {
	private OwlUtil owlUtil;
	
	public MltReasoner(String owlFileName) throws FileNotFoundException {
		this.owlUtil = new OwlUtil(owlFileName);		
	}
	
	public MltReasoner(OntModel owlModel) {
		this.owlUtil = new OwlUtil(owlModel);
	}
	
	public OntModel getOwlModel(){
		return owlUtil.getOwlModel();
	}

	public void run() throws MltException, OWLOntologyCreationException, IOException, OWLOntologyStorageException {
		owlUtil.validate();
		runInferences();
		checkMltConstraints();
		owlUtil.validate();
		owlUtil.printNoStatements();
	}
	
	public void runInferences() throws OWLOntologyCreationException, OWLOntologyStorageException, IOException {
		MltReasonerInferences mltRsnrInf = new MltReasonerInferences(owlUtil);
		mltRsnrInf.createStatementsByInferences();
		
		if(!mltRsnrInf.getInferenceLogMsg().isEmpty())
			System.out.println(mltRsnrInf.getInferenceLogMsg());
//		if(!mltRsnrInf.getDuplicatedInferenceLogMsg().isEmpty())
//			System.out.println(mltRsnrInf.getDuplicatedInferenceLogMsg());
//		if(!mltRsnrInf.getLogMsg().isEmpty())
//			System.out.println(mltRsnrInf.getLogMsg());
		
	}

	private void checkMltConstraints(){
		MltReasonerConsistencies mltRsnrCons = new MltReasonerConsistencies(owlUtil);
		try{
			mltRsnrCons.checkConsistency();
			
			checkBaseAndSubTypesFromDifferentOrders();
		}catch(MltException e){
			System.out.println();
		}
		if(!mltRsnrCons.getLogMsg().isEmpty())
			System.out.println(mltRsnrCons.getLogMsg());
	}

	private void checkBaseAndSubTypesFromDifferentOrders() throws MltException {
		List<HashMap<String, String>> baseAndSubTypesFromDifferentOrders = owlUtil.getBaseAndSubTypesFromDifferentOrders();
		
		String owlModelNs = owlUtil.getOwlModelPrefix();
		String excptMsg = "";
		
		for (HashMap<String, String> baseAndSubTypesFromDifferentOrder : baseAndSubTypesFromDifferentOrders) {
			String baseTypeHO = baseAndSubTypesFromDifferentOrder.get("baseTypeHO").replace(MLT.getURI(), "");
			String subTypeHO = baseAndSubTypesFromDifferentOrder.get("subTypeHO").replace(MLT.getURI(), "");
			String baseType = baseAndSubTypesFromDifferentOrder.get("baseType").replace(owlModelNs, "");
			String subType = baseAndSubTypesFromDifferentOrder.get("subType").replace(owlModelNs, "");
			
			excptMsg += "\nThe classes " + subType + "(rdf:type " + subTypeHO + ") and " + baseType + "(rdf:type " + baseTypeHO + " cannot be from different order, since " + subType + " is subClassOf " + baseType;
		}
		
		if(baseAndSubTypesFromDifferentOrders.size() > 0)
			throw new MltException(excptMsg);
	}
	
	
}
