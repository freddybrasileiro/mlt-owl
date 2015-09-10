package br.ufes.inf.nemo.mlt.web.reasoner;

import java.util.HashMap;
import java.util.List;

import br.ufes.inf.nemo.mlt.web.reasoner.exceptions.MltInconsistencyException;
import br.ufes.inf.nemo.mlt.web.reasoner.owl.OwlUtil;
import br.ufes.inf.nemo.mlt.web.reasoner.sparql.MltAxiomsSparqlUtil;
import br.ufes.inf.nemo.mlt.web.reasoner.sparql.MltTheoremsSparqlUtil;

public class MltReasonerConsistencies {
	private OwlUtil owlUtil;
	
	public MltReasonerConsistencies(OwlUtil owlUtil) {
		this.owlUtil = owlUtil;
	}
	
	public void checkConsistency() throws MltInconsistencyException{
		checkA1();
		checkT10();
		checkT11();
		checkT14();
	}

	private void checkA1() throws MltInconsistencyException {
		List<HashMap<String, String>> a1Results = MltAxiomsSparqlUtil.getA1GoingInconsistencies(owlUtil.getOwlModel());
		
		if(a1Results.size() > 0)
			throw new MltInconsistencyException("");
	}

	private void checkT10() throws MltInconsistencyException {
		List<HashMap<String, String>> t10Results = MltTheoremsSparqlUtil.getT10GoingInconsistencies(owlUtil.getOwlModel());
		
		if(t10Results.size() > 0)
			throw new MltInconsistencyException("");
	}

	private void checkT11() throws MltInconsistencyException {
		List<HashMap<String, String>> t11Results = MltTheoremsSparqlUtil.getT11GoingInconsistencies(owlUtil.getOwlModel());
		
		if(t11Results.size() > 0)
			throw new MltInconsistencyException("");
	}

	private void checkT14() throws MltInconsistencyException {
		List<HashMap<String, String>> t14Results = MltTheoremsSparqlUtil.getT14GoingInconsistencies(owlUtil.getOwlModel());
		
		if(t14Results.size() > 0)
			throw new MltInconsistencyException("");
	}
}
