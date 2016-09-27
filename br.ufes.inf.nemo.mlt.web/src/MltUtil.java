
public class MltUtil {
	static public void printTriple(String s, String p, String o, String modelPrefix){
		s = s.replace(modelPrefix, "");
		p = p.replace(modelPrefix, "");
		o = o.replace(modelPrefix, "");
		System.out.print("[");
		System.out.print(s);
		System.out.print(", ");
		System.out.print(p);
		System.out.print(", ");
		System.out.print(o);
		System.out.println("]");
	}
}
