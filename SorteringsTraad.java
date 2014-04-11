class SorteringsTraad extends Thread {
    
    private String[] ord;
    private volatile SorteringsTraad flettePartner;
    private volatile boolean ferdig;

    SorteringsTraad(String[] ord) {
	this.ord = ord;
    }

    public void run() {
	String[] sortert = new String[ord.length];
	for (int i = 0; i < ord.length; i++) {
	    sortert[finnPlass(ord[i])] = ord[i];
	}
	ferdig = true;
    }

    //finner hvilken plass et ord skal vaere paa ved aa telle ant compareTo kall som er > 0
    private int finnPlass(String a) {
	int plass = 0;
	for (int i = 0; i < ord.length; i++) {
	    if(a.compareTo(ord[i]) > 0) {
		plass++;
	    }
	}
	return plass;
    }

    public void setPartner(SorteringsTraad s) {
	flettePartner = s;
    }

    public void flett() {
	while (!flettePartner.erFerdig()) {
	    try {
		Thread.sleep(1000);
	    }
	    catch (Exception e) {
		e.printStackTrace();
	    }
	}
	int j = 0;
	int g = 0;
	String[] fletteOrd = flettePartner.getOrd();
	String[] nyArray = new String[ord.length + fletteOrd.length];
	
	for (int i = 0; i < nyArray.length; i++) {
	    if (g == fletteOrd.length || (j < ord.length && ord[j].compareTo(fletteOrd[g]) < 0)) {
		nyArray[i] = ord[j];
		j++;
	    }
	    else {
		nyArray[i] = fletteOrd[g];
		g++;
	    }
	}	
    }

    public boolean erFerdig() {
	return ferdig;
    }

    public String[] getOrd() {
	return ord;
    }

    public void skriv() {
	for (int i = 0; i < ord.length; i++) {
	    System.out.println(ord[i]);
	}
    }
}