package working_with_sboms;

import java.io.*;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.parser.ParseException;

public class translate_sbom_to_java {

	public static void main(String[] args) throws IOException, ParseException {
		
		System.out.println("Enter path of SBOM to work with:");
        Scanner scanner = new Scanner(System.in);

        String filepath1 = scanner.nextLine();
        

        String path1;
        Pattern pattern = Pattern.compile("^.*[/\\\\]");
        Matcher matcher = pattern.matcher(filepath1);
        matcher.find();
        path1 = matcher.group(0);

        pattern = Pattern.compile("[^/\\\\]*$");
        matcher = pattern.matcher(filepath1);
        matcher.find();
        String filename1 = matcher.group(0);
        String[] splittedFilename1 = filename1.split("\\.");
        filename1 = splittedFilename1[0];
        String filetype1 = splittedFilename1[1];	 
		 
		 
		 Methods_helper_class mhc = new Methods_helper_class();
		 List<Komponente> sbom1;
		 
		 switch (filetype1) {
		 case "xml":
			 sbom1  = mhc.xml_parse_components_to_List(filepath1, filename1);
			 break;
		 case "json":
			 sbom1  = mhc.json_parse_components_to_List(filepath1, filename1);
			 break;
		 default:
			 System.out.println("ERROR: File is not of type xml or json");
			 throw new IllegalArgumentException();
		 }
		 
	     
		 boolean exit = false;
		 while (!exit) {
		 System.out.println("\nEnter operation you want to perform: \n"
		 		+ "-Type 'singeldiff' if you want a listing of all components of SBOM1 which are not present in SBOM2 \n"
		 		+ "-Type 'diff' if you want a listing of all components which are not present in both SBOMs \n"
		 		+ "-Type 'sbom2csv' if you want a csv listing of all purls of SBOM \n"
		 		+ "-Type 'exit' if you want to end this programm \n");
		 

	     String command = scanner.nextLine();
	     
	     String filepath2;
	     String path2;
	     String filename2;
	     String[] splittedFilename2;
	     String filetype2;
	     List<Komponente> sbom2;
	     
	     switch (command) {
	     case "singlediff":
	    	 System.out.println("Enter path of the second SBOM to work with:");
	         

	         filepath2 = scanner.nextLine();
	         
	         pattern = Pattern.compile("^.*[/\\\\]");
	         matcher = pattern.matcher(filepath2);
	         matcher.find();
	         path2 = matcher.group(0);

	         pattern = Pattern.compile("[^/\\\\]*$");
	         matcher = pattern.matcher(filepath2);
	         matcher.find();
	         filename2 = matcher.group(0);
	         splittedFilename2 = filename2.split("\\.");
	         filename2 = splittedFilename2[0];
	         filetype2 = splittedFilename2[1];
	         
	         switch (filetype2) {
			 case "xml":
				 sbom2  = mhc.xml_parse_components_to_List(filepath2, filename2);
				 break;
			 case "json":
				 sbom2  = mhc.json_parse_components_to_List(filepath2, filename2);
				 break;
			 default:
				 System.out.println("ERROR: File is not of type xml or json");
				 throw new IllegalArgumentException();
			 }
	         
	         
	    	 mhc.print_single_diff(sbom1, filename1, sbom2, filename2, false);
	    	 break;
	     case "diff":
	    	 System.out.println("Enter path of the second SBOM to work with:"); 

	         filepath2 = scanner.nextLine();
	        
	         pattern = Pattern.compile("^.*[/\\\\]");
	         matcher = pattern.matcher(filepath2);
	         matcher.find();
	         path2 = matcher.group(0);

	         pattern = Pattern.compile("[^/\\\\]*$");
	         matcher = pattern.matcher(filepath2);
	         matcher.find();
	         filename2 = matcher.group(0);
	         splittedFilename2 = filename2.split("\\.");
	         filename2 = splittedFilename2[0];
	         filetype2 = splittedFilename2[1];
	         
	         
	         switch (filetype2) {
			 case "xml":
				 sbom2  = mhc.xml_parse_components_to_List(filepath2, filename2);
				 break;
			 case "json":
				 sbom2  = mhc.json_parse_components_to_List(filepath2, filename2);
				 break;
			 default:
				 System.out.println("ERROR: File is not of type xml or json");
				 throw new IllegalArgumentException();
			 }
	         
	         
	    	 mhc.print_double_diff(sbom1, filename1, sbom2, filename2);
	    	 break;
	     case "sbom2csv":
	    	 mhc.executeCSVtransfer(filepath1, filename1, filetype1);
	     	 break;
	     case "exit":
	    	 exit = true;
	    	 break;
	     default:
	    	 System.out.println("ERROR: Unknown command: '" + command + "'");
	    	 break;
	     }
		 }
		 scanner.close();
		 System.out.println(">>Exited!");
	}
}
