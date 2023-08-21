package working_with_sboms;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class Methods_helper_class {
	
	public void executeCSVtransfer(String filepath, String bom_name, String filetype) throws IOException {
		switch (filetype) {
		case "xml":
			sbom2csv_xml(filepath, bom_name);
			break;
		case "json":
			sbom2csv_json(filepath, bom_name);
			break;
		}
	}

	private void sbom2csv_json(String filepath, String bom_name) throws FileNotFoundException {
		System.out.println(">>Transferring components of " + bom_name + "...");
		StringBuilder csv = new StringBuilder();
		csv.append("\n \n \nIDs of " + bom_name + ":\n");
		String jsonString = "";

		File file = new File(filepath);
		Scanner sc = new Scanner(file);

		while (sc.hasNextLine()) {
			jsonString += sc.nextLine() + "\n";
		}
		sc.close();

		JSONObject bom = new JSONObject(jsonString);

		JSONArray packages = bom.getJSONArray("componentsFound");
		for(int i = 0; i < packages.length(); i++)
		{
			JSONObject entry = packages.getJSONObject(i);
			JSONObject component = entry.getJSONObject("component");
			csv.append(component.getString("id")).append("\n");

		}
		PrintWriter out = new PrintWriter(filepath + bom_name + "converted2.csv");
		out.println(csv);
		out.close();

		System.out.println(">>Finished SBOM2csv transfer");
	}

	private void sbom2csv_xml(String filepath, String bom_name) throws IOException {
		System.out.println(">>Transferring components of " + bom_name + " to csv...");
        StringBuilder csv = new StringBuilder();
        csv.append("Dependencies of " + bom_name + ":\n");
		 DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		
		try {
			 
	          // optional, but recommended
	          // process XML securely, avoid attacks like XML External Entities (XXE)
	          dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

	          // parse XML file
	          DocumentBuilder db = dbf.newDocumentBuilder();

	          Document doc = db.parse(new File(filepath));

	          // optional, but recommended
	          // http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
	          doc.getDocumentElement().normalize();


	          // get <staff>
	          NodeList list = doc.getElementsByTagName("dependencies");

	          for(int temp = 0; temp < list.getLength(); temp++) {

	              Node node = list.item(temp);

	              if(node.getNodeType() == Node.ELEMENT_NODE) {
	                  
	                List<Node> dependencies = new ArrayList<Node>();
	                
	                for(Node child = node.getFirstChild(); child != null; child = child.getNextSibling())
	        	    {
	        	        if(child instanceof Element && "dependency".equals(child.getNodeName())) dependencies.add(child);
	        	    }
	                  
	                  for (Node topDependency : dependencies) {

	    	              if(topDependency.getNodeType() == Node.ELEMENT_NODE) {

	    	                  Element topDependencyElement = (Element) topDependency;
	    	                  
	    	                  csv.append(topDependencyElement.getAttributes().getNamedItem("ref").getTextContent()).append("\n");
	    	                  
	    	                  List<Node> dependenciesOfTopDependency = new ArrayList<Node>();
	    		                
	    		                for(Node child = topDependency.getFirstChild(); child != null; child = child.getNextSibling())
	    		        	    {
	    		        	        if(child instanceof Element && "dependency".equals(child.getNodeName())) dependenciesOfTopDependency.add(child);
	    		        	    }
	    	                  
	    	                  for(Node subDependency : dependenciesOfTopDependency) {
	    	                	  
	    	                	  if(subDependency.getNodeType() == Node.ELEMENT_NODE) {

	    	    	                  Element subDependencyElement = (Element) subDependency;
	    	    	                  
	    	    	                  csv.append(",").append(subDependencyElement.getAttributes().getNamedItem("ref").getTextContent()).append("\n");
	    	                	  }
	    	                  }
	    	              }
	                  }
	              }
	          }
	          
	          csv.append("\n \n \nPURLs of " + bom_name + ":\n");

			  try {
	          	list = doc.getElementsByTagName("component");

	          	for(int temp = 0; temp < list.getLength(); temp++) {

					Node node = list.item(temp);

					if (node.getNodeType() == Node.ELEMENT_NODE) {

						Element element = (Element) node;

						// get text

						csv.append(element.getAttributes().getNamedItem("bom-ref").getTextContent()).append("\n");
					}
				}
			  } catch (NullPointerException e) {
				  list = doc.getElementsByTagName("purl");

				  for(int temp = 0; temp < list.getLength(); temp++) {

					  Node node = list.item(temp);

					  if (node.getNodeType() == Node.ELEMENT_NODE) {

						  Element element = (Element) node;

						  csv.append(element.getTextContent()).append("\n");
					  }
				  }
			  }
	              
		} catch(ParserConfigurationException | SAXException | IOException e) {
	          e.printStackTrace();
	    }
		
		PrintWriter out = new PrintWriter(filepath + bom_name + "converted2.csv");
		out.println(csv);
		out.close();
		
		System.out.println(">>Finished SBOM2csv transfer");
	}
	
	private List<Komponente> double_compare(List<Komponente> array1, List<Komponente> array2) {
		/*  Compares the components of the given lists of components. Each component of list1, which is not
		 * 	present in list2 will be added and vice versa. 
         *  
         *  Input:  array1           	-List to compare
         *  		array2			 	-List which array1 is compared with
         * 
         *  Return: diff_components   	-List with every component which is in list1 but not in list2
         */
		System.out.println(">>Comparing elements...");
		List<Komponente> diff_components = new ArrayList_diff_toString<Komponente>();
		boolean found = false;
		for(Komponente c1 : array1) {
			for(Komponente c2 : array2) {
				if(c1.compare(c2)) {
					found = true;
					break;
				} else {
					found = false;
					continue; 
				}
			}
			if(!found) {
				diff_components.add(c1);
			}
		}
		for(Komponente c2 : array2) {
			for(Komponente c1 : array1) {
				if(c2.compare(c1)) {
					found = true;
					break;
				} else {
					found = false;
					continue; 
				}
			}
			if(!found) {
				boolean found2 = false;
				for(Komponente c : diff_components) {
					if(c2.compare(c)) {
						found2 = true;
						break;
					}
				}
				if(!found2) {
					diff_components.add(c2);
				}
			}
		}
		return diff_components;
	}
	
	
	private List<Komponente> single_compare(List<Komponente> array1, List<Komponente> array2, boolean onlynames) {
		/*  Compares the components of the given lists of components. Each component of list1, which is not
		 * 	present in list2 will be added in the returned diff_components list. If onlynames flag is true,
		 * 	only the names will be used in the comparison.
         *  
         *  Input:  array1           	-List to compare
         *  		array2			 	-List which array1 is compared with
         *  		onlynames		 	-Flag to compare only the names of the components with each other
         * 
         *  Return: diff_components   	-List with every component which is in list1 but not in list2
         */
		
		System.out.println(">>Comparing elements...");
		List<Komponente> diff_components = new ArrayList_diff_toString<Komponente>();
		boolean found = false;
		
		if(!onlynames) {
			for(Komponente c1 : array1) {
				for(Komponente c2 : array2) {
					if(c1.compare(c2)) {
						found = true;
						break;
					} else {
						found = false;
						continue; 
					}
				}
				if(!found) {
					diff_components.add(c1);
				}
			}
		} else if(onlynames) {
			for(Komponente c1 : array1) {
				for(Komponente c2 : array2) {
					if(c1.compareNames(c2)) {
						found = true;
						break;
					} else {
						found = false;
						continue; 
					}
				}
				if(!found) {
					diff_components.add(c1);
				}
			}
		}
		
		
		return diff_components;
	}
	
	
	public List<Komponente> xml_parse_components_to_List(String Filename, String bom_name) {
		System.out.println(">>Parsing components of " + bom_name + "...");
		 List<Komponente> components = new ArrayList_diff_toString<Komponente>();
		 DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		
		try {
			 
	          // optional, but recommended
	          // process XML securely, avoid attacks like XML External Entities (XXE)
	          dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

	          // parse XML file
	          DocumentBuilder db = dbf.newDocumentBuilder();

	          Document doc = db.parse(new File(Filename));

	          // optional, but recommended
	          // http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
	          doc.getDocumentElement().normalize();


	          // get <staff>
	          NodeList list = doc.getElementsByTagName("component");

	          for(int temp = 0; temp < list.getLength(); temp++) {

	              Node node = list.item(temp);

	              if(node.getNodeType() == Node.ELEMENT_NODE) {

	                  Element element = (Element) node;
	                  
	                  String[] List_args = new String[5];

	                  // get text
					  try {
						  List_args[0] = element.getAttributes().getNamedItem("bom-ref").getTextContent();
					  } catch (NullPointerException e) {
						  List_args[0] = "none";
					  }
	                  List_args[1]  = element.getElementsByTagName("name").item(0).getTextContent();
	                  try {
	                  List_args[2] = element.getElementsByTagName("version").item(0).getTextContent();
	                  } catch (NullPointerException e) {
	                	  List_args[2] = "none";
	                  }
	                  try {
	                	  List_args[3] = element.getElementsByTagName("purl").item(0).getTextContent();
	              		} catch (NullPointerException e) {
	              			List_args[3] = "none";
	              		}
	                  List_args[4] = bom_name;

	                 components = add_after_contain_check(List_args, components);
	          }
	        }

	      } catch(ParserConfigurationException | SAXException | IOException e) {
	          e.printStackTrace();
	      }
		System.out.println(">>Finished parsing of components of " + bom_name);
		
		return components;
	}
	
	
	public List<Komponente> json_parse_components_to_List(String Filename, String bom_name) throws IOException {
		System.out.println(">>Parsing components of " + bom_name + "...");
		List<Komponente> components = new ArrayList_diff_toString<Komponente>();
		String jsonString = "";
		
	    File file = new File(Filename);
	    Scanner sc = new Scanner(file);
	 
	    while (sc.hasNextLine()) {
	      jsonString += sc.nextLine() + "\n";
	  }
	  sc.close();
	        
	            JSONObject bom = new JSONObject(jsonString);
	            
	            JSONArray packages = bom.getJSONArray("componentsFound");
	            for(int i = 0; i < packages.length(); i++)
	            {
					JSONObject entry = packages.getJSONObject(i);
					JSONObject component = entry.getJSONObject("component");
	            	String[] List_args = new String[5];
	                List_args[0] = component.getString("id");
	                List_args[1] = component.getString("artifactId");
	            try {
	                List_args[2] = component.getString("version");
	            } catch (JSONException e) {
              	  List_args[2] = "none";
                }

				List_args[3] = "none";
	                List_args[4] = bom_name;
	                
	                components = add_after_contain_check(List_args, components);
	            }
	    		System.out.println(">>Finished parsing of components of " + bom_name);
	            return components;
	}
	
	
	private List<Komponente> add_after_contain_check(String List_args[], List<Komponente> components) {
		boolean found = false;
        
        for(Komponente k:components) {
      	  if(k.getBom_ref().equals(List_args[0])) {
      		  found = true;
      		  continue;
      	  }
      	  if(k.getName().equals(List_args[1]) && k.getVersion().equals(List_args[2]) && k.getPurl().equals(List_args[3])) {
      		  found = true;
      		  continue;
      	  }
        }
        if(!found) {
      	  components.add(new Komponente(List_args));
        }
        return components;
	}
	
	
	public void print_double_diff(List<Komponente> bom1, String bom_name1,  List<Komponente> bom2, String bom_name2) throws IOException {
		 String out = "";
		 String br = "\n";
		 
		 out += "Anzahl von Komponenten in " + bom_name1 + ": " + bom1.size() + br + 
				 "Anzahl von Komponenten in " + bom_name2 + ": " + bom2.size() + br;
			     
			     
			     out += "\n--------------------------------------------------" + br + br;
			     
			     List<Komponente> diff = this.double_compare(bom1, bom2);
			     Collections.sort(diff);
			     
			     out += "Anzahl unterschiedlicher Komponenten: " + diff.size() + br +
			     "\nAusgabe der unterschiedlichen Komponenten:" + br +
			     "\n" + diff + br;  
			     
			     log(out, bom_name1, bom_name2);
	}
	
	public void print_single_diff(List<Komponente> bom1, String bom_name1,  List<Komponente> bom2, String bom_name2, boolean onlynames) throws IOException {
		 String out = "";
		 String br = "\n";
		 
		 out += "Anzahl von Komponenten in " + bom_name1 + ": " + bom1.size() + br + 
				 "Anzahl von Komponenten in " + bom_name2 + ": " + bom2.size() + br;
			     
			     
			     out += "\n--------------------------------------------------" + br + br;
			     
			     List<Komponente> diff = this.single_compare(bom1, bom2, onlynames);
			     Collections.sort(diff);
			     
			     out += "Anzahl unterschiedlicher Komponenten: " + diff.size() + br +
			     "\nAusgabe der unterschiedlichen Komponenten von " + bom_name1 + " zu " + bom_name2 + ":" + br +
			     "\n" + diff + br;  
			     
			     log(out, bom_name1, bom_name2);
	}
	
	
	private void log(String message, String name1, String name2) throws IOException { 
		 System.out.println(">>Printing...");
		 String uniqueID = UUID.randomUUID().toString();
	      PrintWriter out = new PrintWriter(new FileWriter(("diff_" + name1 + "--" + name2 + "_" + uniqueID + ".txt"), true), true);
	      out.write(message);
	      out.close();
	      System.out.println(">>All done!");
	    }
	
}
