package stage1.cleaning_tool;

import java.text.Normalizer;
import java.text.Normalizer.Form;

public class Simple_Clean_Test {

	public static void main(String[] args) {
		Simple_Clean_Test cleaning_controller = new Simple_Clean_Test();
		System.out.println(cleaning_controller.remove_string_inconsistencies("hartmut k�nig �sd��d�fsd�f鴴��"));
	}
	
	public String remove_string_inconsistencies(String original) {
		String s = original;
	    s = s.replaceAll("[����]","e");
	    s = s.replaceAll("[���]","u");
	    s = s.replaceAll("[���]","i");
	    s = s.replaceAll("[���]","a");
	    s = s.replaceAll("[��]","o");

	    s = s.replaceAll("[�����]","E");
	    s = s.replaceAll("[���]","U");
	    s = s.replaceAll("[���]","I");
	    s = s.replaceAll("[���]","A");
	    s = s.replaceAll("[��]","O");
	    s = s.replaceAll("�","ae");
	    String resp = s;
		resp = resp.replace(".", "");
		resp = resp.replace("<#><#>", "");
		resp = resp.replace("<$<$<", "");
		resp = resp.replace(">*>*>", "");
		resp = resp.replace("\"", "");
		resp = resp.replace("\\?", "");
		resp = resp.replace("!", "");
		resp = resp.toLowerCase();
		resp = Normalizer.normalize(resp, Form.NFKC);
		resp = resp.replaceAll("[^\\p{ASCII}]", "");

		return resp;

	}
}
