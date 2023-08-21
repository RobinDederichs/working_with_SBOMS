package working_with_sboms;

import java.util.ArrayList;
import java.util.Collection;

public class ArrayList_diff_toString<E> extends ArrayList<E> {
	
	String name_bom;


	public ArrayList_diff_toString() {
		// TODO Auto-generated constructor stub
	}

	public ArrayList_diff_toString(int initialCapacity) {
		super(initialCapacity);
		// TODO Auto-generated constructor stub
	}

	public ArrayList_diff_toString(Collection c) {
		super(c);
		// TODO Auto-generated constructor stub
	}
	
	public String toString() {
		String s = "[\n";
		for(E i : this) {
			s += i.toString();
			s += "\n";
		}
		return s + "]";
	}
	
	public void setBom_name(String name) {
		name_bom = name;
	}
	
	public String getBom_name() {
		return name_bom;
	}

}
