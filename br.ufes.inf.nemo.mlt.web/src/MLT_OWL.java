import java.util.Date;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
import com.hp.hpl.jena.vocabulary.XSD;

import br.ufes.inf.nemo.mlt.web.reasoner.MltReasoner;
import br.ufes.inf.nemo.mlt.web.reasoner.owl.OwlFileUtil;
import br.ufes.inf.nemo.mlt.web.reasoner.util.FileUtil;
import br.ufes.inf.nemo.mlt.web.reasoner.util.PerformanceUtil;
import br.ufes.inf.nemo.mlt.web.vocabulary.MLT;
public class MLT_OWL {

	public static void main(String[] args) {
		String executionTime = "";
		try {
			String owlFileName = FileUtil.chooseFile("Choose an OWL file containing your model: ", "resources/examples/", ".owl", "OWL chosen file: ",0);
			Date beginDate = new Date();
			MltReasoner mltReasoner = new MltReasoner(owlFileName);
			
			mltReasoner.run();
			
			executionTime  = PerformanceUtil.getExecutionMessage(beginDate);
			System.out.println("Execution time: " + executionTime);
			int lastBar = owlFileName.lastIndexOf("/");
			String outName = owlFileName.substring(lastBar+1, owlFileName.length());

			OwlFileUtil.saveOwlOntology(mltReasoner.getOwlModel(), outName);
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("Execution finished.");
		System.out.println("Execution time: " + executionTime);
	}
	
	public static void getA3_2(OntModel model){
		String owl = "owl";
		String rdfs = "rdfs";
		String rdf = "rdf";
		String xml = "xsd";
		
		String queryString = ""
				+ "PREFIX " + owl + ": <" + OWL.getURI() + ">\n"
				+ "PREFIX " + rdfs + ": <" + RDFS.getURI() + ">\n"
				+ "PREFIX " + rdf + ": <" + RDF.getURI() + ">\n"
				+ "PREFIX " + xml + ": <" + XSD.getURI() + ">\n"
				+ "PREFIX " + MLT.getPrefix() + ": <" + MLT.getURI() + ">\n"
				+ "CONSTRUCT   { \n"
				+ "?t rdf:type mlt:1stOrderClass \n"
				+ "}\n"
				+ "where {\n"
				+ "	?x rdf:type ?t .\n"
				+ "	?x rdf:type mlt:TokenIndividual .\n"
				+ "	filter(?t != mlt:TokenIndividual) .\n"
				+ "	minus{\n"
				+ "		?t rdf:type mlt:1stOrderClass . \n"
				+ "	}\n"
				+ "}";
		
		Query query = QueryFactory.create(queryString); 		
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		Model newModel = qe.execConstruct();
				
		model.add(newModel.listStatements().toList());
		
	}	
	
//	public static void saveRdf(OntModel ontModel, String owlFileName) throws IOException, OWLOntologyCreationException, OWLOntologyStorageException{
//		String syntax = "RDF/XML-ABBREV";
//		StringWriter outJenaStringWriter = new StringWriter();
//		
//		//ontModel is written in outStringWriter
//		ontModel.write(outJenaStringWriter, syntax);
//		//get outStringWriter as String
//		String result = outJenaStringWriter.toString();
//		
//		//a temp file is created
//		File tempJenaFile = new File("resources/output/"+"temp-"+owlFileName);  
//		if(tempJenaFile.exists()){
//			tempJenaFile.delete();
//		}
//		//jena is used to write in the file
//		FileOutputStream fosJena = new FileOutputStream(tempJenaFile);  
//		fosJena.write(result.getBytes());    
//		
//		//start creating a model using OWL API
//		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
//		OWLOntology ontology = manager.loadOntologyFromOntologyDocument(tempJenaFile);
//		tempJenaFile.delete();
//
//		//a new file is created
//		File arquivoOwlApi = new File("resources/output/"+owlFileName);  
//		if(arquivoOwlApi.exists()){
//			arquivoOwlApi.delete();
//		}		
//		FileOutputStream fosOwlApi = new FileOutputStream(arquivoOwlApi);
//		
//		//the OWL Ontology is writen in the FOS
//		manager.saveOntology(ontology, fosOwlApi);
//		
//		fosOwlApi.close();
//		fosJena.close();
//	}
}
