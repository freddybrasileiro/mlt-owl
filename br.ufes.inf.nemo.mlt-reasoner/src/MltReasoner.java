import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;

import br.ufes.inf.nemo.ufo_rdf.vocabulary.MLT;

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
	
	public void runInferences() {
		inferHighOrdersByTransitivity();
	}
	
	private void inferHighOrdersByTransitivity(){
		List<HashMap<String, String>> trasitiveHOs = SparqlUtil.getHighOrderFromByTransitivity(owlUtil.getOwlModel());
		
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
		checkBaseAndSubTypesFromDifferentOrders();
	}

	private void checkBaseAndSubTypesFromDifferentOrders() throws MltException {
		List<HashMap<String, String>> baseAndSubTypesFromDifferentOrders = owlUtil.getBaseAndSubTypesFromDifferentOrders();
		
		String owlModelNs = owlUtil.getOwlModel().getNsPrefixURI("");
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
	
	//∀x iof(x,Individual)↔∄y iof(y,x) (A1)
	//∀t iof(t,1stOT)↔(∀x iof(x,t)→iof(x,Individual)) (A2)
	//∀t iof(t,2ndOT)↔(∀t′ iof(t′,t)→iof(t′,1stOT)) (A3)
	//∀t iof(t,3rdOT)↔(∀t′ iof(t′,t)→iof(t′,2ndOT)) (A4)
	//∀x (iof(x,Individual)∨iof(x,1stOT)∨iof(x,2ndOT)∨iof(x,3rdOT))∨(x=3rdOT) (A5)
	//∄x (iof(x,Individual)∧iof(x,1stOT))∨(iof(x,Individual)∧iof(x,2ndOT))∨ (iof(x,Individual)∧iof(x,3rdOT))∨(iof(x,1stOT)∧iof(x,2ndOT))∨(iof(x,1stOT)∧iof(x,3rdOT))∨(iof(x,2ndOT)∧iof(x,3rdOT)) (A6)
	//∀t1,t2 (¬iof(t1,Individual)∧¬iof(t2,Individual))→((t1=t2)↔(∀e iof(e,t1)↔iof(e,t2))) (A7)
	//∀t1,t2 specializes(t1,t2)↔(¬iof(t1,Individual)∧¬iof(t2,Individual)∧(∀e iof(e,t1)→iof(e,t2))) (A8)
	//∀ t1,t2 properSpecializes(t1,t2)↔(specializes(t1,t2)∧t1≠t2) (A9)
	//∀t1,t2 isSubordinateTo (t1,t2)↔ (¬iof(t1,Individual)∧(∀t3 iof(t3,t1)→(∃t4 iof(t4,t2)∧properSpecializes(t3,t4)))) (A10)
	//∀t1,t2 isPowertypeOf(t1,t2)↔(¬iof(t1,Individual)∧(∀t3 iof(t3,t1)↔specializes(t3,t2))) (A11)
	//∀t1,t2 characterizes (t1,t2)↔(¬iof(t1,Individual)∧(∀t3 iof(t3,t1)→properSpecializes(t3,t2))) (A12)
	//∀t1,t2 completelyCharacterizes(t1,t2)↔(characterizes(t1,t2)∧(∀e iof(e,t2)→∃t3 (iof(e,t3)∧iof(t3,t1)))) (A13)
	//∀t1,t2 disjointlyCharacterizes (t1,t2)↔ (characterizes(t1,t2)∧∀e,t3,t4 ((iof(t3,t1)∧iof(t4,t1)∧iof(e,t3)∧iof(e,t4))→t3=t4))) (A14)
	//∀t1,t2 partitions(t1,t2)↔(completelyCharacterizes(t1,t2)∧disjointlyCharacterizes(t1,t2)) (A15)
	
	//iof(Individual, 1stOT) (T1)
	//iof(1stOT, 2ndOT) (T2)
	//iof(2ndOT, 3rdOT) (T3)
	//∀t iof(t,1stOT)↔specializes(t,Individual) (T4)
	//∀t iof(t,2ndOT)↔specializes(t,1stOT) (T5)
	//∀t iof(t,3rdOT)↔specializes(t,2ndOT) (T6)
	//isPowertypeOf(1stOT,Individual) (T7)
	//isPowertypeOf(2ndOT,1stOT) (T8)
	//isPowertypeOf(2ndOT,3rdOT) (T9)
	//∀p,t isPowertypeOf(p,t)→ ∄p′ (p≠p′)⋀isPowertypeOf(p′,t) (T10)
	//∀p,t isPowertypeOf(p,t)→ ∄t′ (t≠t′)⋀isPowertypeOf(p,t′) (T11)
	//∀t1,t2,t3,t4(specializes(t2,t1)∧isPowertypeOf(t4,t2)∧isPowertypeOf(t3,t1))→specializes(t4,t3) (T12)
	//∀t1,t2,t3 (isPowertypeOf(t2,t1)∧characterizes(t3,t1))→properSpecializes(t3,t2) (T13)
	//∀ t1,t2,t3 (partitions(t1,t3)∧partitions(t2,t3))→¬properSpecializes(t1,t2) (T14)
	
}
