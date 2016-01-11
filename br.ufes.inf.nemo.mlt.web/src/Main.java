import java.util.Date;

import org.semanticweb.owlapi.apibinding.OWLManager;

import br.ufes.inf.nemo.mlt.web.reasoner.MltReasoner;
import br.ufes.inf.nemo.mlt.web.reasoner.owl.OwlFileUtil;
import br.ufes.inf.nemo.mlt.web.reasoner.util.FileUtil;
import br.ufes.inf.nemo.mlt.web.reasoner.util.PerformanceUtil;
public class Main {

	public static void main(String[] args) {
		String executionTime = "";
		try {
			String owlFileName = FileUtil.chooseFile("Choose an OWL file containing your model: ", "resources/examples/", ".owl", "OWL chosen file: ",0);
			Date beginDate = new Date();
			MltReasoner mltReasoner = new MltReasoner(owlFileName);
			mltReasoner.run();
			executionTime  = PerformanceUtil.getExecutionMessage(beginDate);
			
			int lastBar = owlFileName.lastIndexOf("/");
			String outName = owlFileName.substring(lastBar+1, owlFileName.length());
//			saveRdf(mltReasoner.getOwlModel(), outName);
			OwlFileUtil.saveOwlOntology(mltReasoner.getOwlModel(), outName, OWLManager.createOWLOntologyManager());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Execution finished.");
		System.out.println("Execution time: " + executionTime);
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
