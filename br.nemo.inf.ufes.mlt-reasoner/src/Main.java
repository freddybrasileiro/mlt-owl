

public class Main {

	public static void main(String[] args) {
		try {
			String owlFileName = FileUtil.chooseFile("Choose an OWL file containing your model: ", "resources/examples/", ".owl", "OWL chosen file: ",0);
			
			OwlUtil owlUtil = new OwlUtil(owlFileName);
//			owlUtil.runInferences();
			
						
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
