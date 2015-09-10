import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import br.ufes.inf.nemo.mlt.web.reasoner.MltReasoner;
import br.ufes.inf.nemo.mlt.web.reasoner.util.FileUtil;

import com.hp.hpl.jena.ontology.OntModel;

public class Main {

	public static void main(String[] args) {
		try {
			String owlFileName = FileUtil.chooseFile("Choose an OWL file containing your model: ", "resources/examples/", ".owl", "OWL chosen file: ",0);
			MltReasoner mltReasoner = new MltReasoner(owlFileName);
			mltReasoner.run();
			
			int lastBar = owlFileName.lastIndexOf("/");
			String outName = owlFileName.substring(lastBar+1, owlFileName.length());
			saveRdf(mltReasoner.getOwlModel(), outName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Done");
	}
	
	public static void saveRdf(OntModel ontModel, String owlFileName) throws IOException, OWLOntologyCreationException, OWLOntologyStorageException{
		String syntax = "RDF/XML-ABBREV";
		StringWriter out = new StringWriter();
		ontModel.write(out, syntax);
		String result = out.toString();
		File arquivo = new File("resources/output/"+"temp-"+owlFileName);  
		if(arquivo.exists()){
			arquivo.delete();
		}
		FileOutputStream fos = new FileOutputStream(arquivo);  
		fos.write(result.getBytes());    
		
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLOntology ontology = manager.loadOntologyFromOntologyDocument(arquivo);
				
		File arquivoOwlApi = new File("resources/output/"+owlFileName);  
		if(arquivoOwlApi.exists()){
			arquivoOwlApi.delete();
		}
		FileOutputStream fosOwlApi = new FileOutputStream(arquivoOwlApi);
		
		manager.saveOntology(ontology, fosOwlApi);
		
		fosOwlApi.close();
		fos.close();
	}
}
