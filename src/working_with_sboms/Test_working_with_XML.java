package working_with_sboms;
//package working_with_XML;
//import static org.junit.jupiter.api.Assertions.*;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//
//class Test_working_with_XML {
//	static List<Komponente> array1 = new ArrayList_diff_toString<Komponente>();
//	static List<Komponente> array2 = new ArrayList_diff_toString<Komponente>();
//	
//	static List<Komponente> array_diff = new ArrayList_diff_toString<Komponente>();
//	
//	Methods_helper_class chc = new Methods_helper_class();
//
//	@BeforeAll
//	static void setUpBeforeClass() throws Exception {
//		String bom_ref1[] = {"bomref1","bomref2", "bomref3","bomref4","bomref5","bomref6", "bomref7", "bomref8"};
//		String bom_ref2[] = {"bomref1","bomref22", "bomref3","bomref4","bomref5","bomref6","bomref7","bomref88", "bomref9"};
//		String name1[] = {"name1","name2", "name3","name4","name5","name6", "name7", "name8"};
//		String name2[] = {"name1","name2", "name33","name4","name5","name6","name7","name88", "name9"};
//		String version1[] = {"version1","version2", "version3","version4","version5","version6","version7","version8"};
//		String version2[] = {"version1","version2", "version3","version44","version5","version6","version7","version88","version9"};
//		String purl1[] = {"purl1","purl2", "purl3","purl4","purl5","purl6","purl7","purl8"};
//		String purl2[] = {"purl1","purl2", "purl3","purl4","purl55","purl6","purl7","purl88","purl9"};
//		String sbom1[] = {"sbom1","sbom1", "sbom1","sbom1","sbom1","sbom1","sbom1","sbom1"};
//		String sbom2[] = {"sbom2","sbom2", "sbom2","sbom2","sbom2","sbom2","sbom2","sbom2","sbom2"};
//		
//		int i = 0;
//		for(String s : bom_ref1) {
//			String[] args = {bom_ref1[i], name1[i], version1[i], purl1[i], sbom1[i]};
//			array1.add(new Komponente(args));
//			i++;
//		}
//		
//		i = 0;
//		for(String s : bom_ref2) {
//			String[] args = {bom_ref2[i], name2[i], version2[i], purl2[i], sbom2[i]};
//			array2.add(new Komponente(args));
//			i++;
//		}
//		
//		
//		
//		String bom_ref[] = {"bomref2", "bomref3","bomref4","bomref5", "bomref8",
//				"bomref22", "bomref3","bomref4","bomref5","bomref88", "bomref9"};
//		
//		String name[] = {"name2", "name3","name4","name5", "name8", 
//				"name2", "name33","name4","name5","name88", "name9"};
//
//		String version[] = {"version2", "version3","version4","version5", "version8", 
//				"version2", "version3","version44","version5","version88","version9"};
//
//		String purl[] = {"purl2", "purl3","purl4","purl5","purl8", 
//				"purl2", "purl3","purl4","purl55","purl88","purl9"};
//
//		String sbom[] = {"sbom1", "sbom1","sbom1","sbom1","sbom1", 
//				"sbom2", "sbom2","sbom2","sbom2","sbom2","sbom2"};
//
//		i = 0;
//		for(String s : bom_ref) {
//			String[] args = {bom_ref[i], name[i], version[i], purl[i], sbom[i]};
//			array_diff.add(new Komponente(args));
//			i++;
//		}
//	}
//
//	@Test
//	void test_compare() {
//		assertEquals(array_diff, chc.compare(array1, array2));
//	}
//
//}
