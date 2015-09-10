package br.ufes.inf.nemo.mlt.web.reasoner;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;

import br.ufes.inf.nemo.mlt.web.reasoner.exceptions.MltException;
import br.ufes.inf.nemo.mlt.web.reasoner.owl.OwlUtil;
import br.ufes.inf.nemo.mlt.web.vocabulary.MLT;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Statement;

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
	
	public void runInferences() {
		MltReasonerInferences mltRsnrInf = new MltReasonerInferences(owlUtil);
		List<Statement> stmts = mltRsnrInf.getStatementsByInferences();
		owlUtil.createStatements(stmts);
		
		inferHighOrdersByTransitivity();
	}
	
	private void inferHighOrdersByTransitivity(){
		List<HashMap<String, String>> trasitiveHOs = owlUtil.getHighOrderFromByTransitivity();
		
		for (HashMap<String, String> trasitiveHO : trasitiveHOs) {
			String subTypeURI = trasitiveHO.get("subType");
			String baseTypeHoURI = trasitiveHO.get("baseTypeHO");
			
			owlUtil.createIof(subTypeURI, baseTypeHoURI);
		}
	}

	public void run() throws MltException {
		checkMltConstraints();
		runInferences();
	}

	private void checkMltConstraints() throws MltException {
		MltReasonerConsistencies mltRsnrCons = new MltReasonerConsistencies(owlUtil);
		mltRsnrCons.checkConsistency();
		
		checkBaseAndSubTypesFromDifferentOrders();
	}

	private void checkBaseAndSubTypesFromDifferentOrders() throws MltException {
		List<HashMap<String, String>> baseAndSubTypesFromDifferentOrders = owlUtil.getBaseAndSubTypesFromDifferentOrders();
		
		String owlModelNs = owlUtil.getOwlModelPrefix();
		String excptMsg = "";
		
		for (HashMap<String, String> baseAndSubTypesFromDifferentOrder : baseAndSubTypesFromDifferentOrders) {
			String baseTypeHO = baseAndSubTypesFromDifferentOrder.get("baseTypeHO").replace(MLT.NS, "");
			String subTypeHO = baseAndSubTypesFromDifferentOrder.get("subTypeHO").replace(MLT.NS, "");
			String baseType = baseAndSubTypesFromDifferentOrder.get("baseType").replace(owlModelNs, "");
			String subType = baseAndSubTypesFromDifferentOrder.get("subType").replace(owlModelNs, "");
			
			excptMsg += "\nThe classes " + subType + "(rdf:type " + subTypeHO + ") and " + baseType + "(rdf:type " + baseTypeHO + " cannot be from different order, since " + subType + " is subClassOf " + baseType;
		}
		
		if(baseAndSubTypesFromDifferentOrders.size() > 0)
			throw new MltException(excptMsg);
	}
	
	
}
