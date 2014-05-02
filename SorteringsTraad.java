import java.util.concurrent.*;
class SorteringsTraad extends Thread {
    
    static int tall = 0;
    int nr;
    private Kontroll k;
    private String[] ord;
    private volatile SorteringsTraad flettePartner;
    private volatile boolean ferdig;
    private CountDownLatch barriere;
    private boolean fletteklar = false;

    SorteringsTraad(String[] ord, Kontroll k, CountDownLatch bar) {
	nr = tall;
	tall++;
	this.ord = ord;
	this.k = k;
	barriere = bar;
    }

    public void run() {
	String[] sortert = new String[ord.length];
	for (int i = 0; i < ord.length; i++) {
	    sortert[finnPlass(ord[i])] = ord[i];
	}
	ord = sortert;
	//System.out.println("Traad " + nr + " er ferdig");
	barriere.countDown();
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
	//System.out.println("Traad " + nr + " fikk Traad " + s.nr + " som partner");
	flettePartner = s;
	fletteklar = true;
    }

    //tar neste ord i hver array og sammenligner dem til det ikke er flere ord igjen
    public void flett(CountDownLatch bar) {
	if (!fletteklar) {
	    return;
	}
	//System.out.println("Fletter traad " + nr);
	int j = 0;
	int g = 0;
	String[] fletteOrd = flettePartner.getOrd();
	String[] nyArray = new String[ord.length + fletteOrd.length];
// 	System.out.println("Gammel array");
// 	for (int i = 0; i < ord.length; i++) {
// 	    System.out.println(ord[i]);
// 	}
// 	System.out.println("\n\nSkal flettes med");
// 	for (int x = 0; x < fletteOrd.length; x++) {
// 	    System.out.println(fletteOrd[x]);
// 	}
	
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
// 	System.out.println("\n\nResultat");
// 	for (int y = 0; y < nyArray.length; y++) {
// 	    System.out.println(nyArray[y]);
// 	}
// 	System.exit(0);
	ord = nyArray;
	fletteklar = false;
	bar.countDown();
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