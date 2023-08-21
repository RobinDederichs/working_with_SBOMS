package working_with_sboms;

public class Komponente implements Comparable<Komponente> {
	
	static int number;
	
	private String bom_ref;
	private String name;
	private String version;
	private String purl;
	private String sbom;
	
	Komponente(String[] args) {
		bom_ref = args[0];
		name = args[1];
		version = args[2];
		purl = args[3];
		sbom = args[4];
		number = 1;
	}

	public String getBom_ref() {
		return bom_ref;
	}

	public String getName() {
		return name;
	}

	public String getVersion() {
		return version;
	}

	public String getPurl() {
		return purl;
	}

	public String getSbom() {
		return sbom;
	}
	
	public boolean compare(Komponente c) {
		if(this.name.equals(c.getName()) && this.version.equals(c.getVersion())) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean compareNames(Komponente c) {
		if(this.name.equals(c.getName())) {
			return true;
		} else {
			return false;
		}
	}
	
	public String toString() {
		return "" + number++ + "{Name: " + this.name + ", Version: " + this.version + ", Purl: " + this.purl +
				", SBOM: " + this.sbom + "}";
	}

	@Override
	public int compareTo(Komponente k) {
		if(this.name.equals(k.getName())) {
			return 0;
		} else {
			return this.name.compareToIgnoreCase(k.getName());
		}
			
	}
}
