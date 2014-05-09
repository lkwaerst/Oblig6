import java.util.concurrent.*;
import java.util.Arrays;
class SorteringsTraad extends Thread {
    
    static int tall = 0;
    int nr;
    private String[] ord;
    private SorteringsTraad flettePartner;
    private boolean fletteklar = false;
    private FletteBuffer fletteBuffer;
    private CountDownLatch flettetFerdig;
    private boolean lever = true;

    SorteringsTraad(String[] ord, FletteBuffer f, CountDownLatch ferdig) {
	fletteBuffer = f;
	nr = tall;
	tall++;
	this.ord = ord;
	flettetFerdig = ferdig;
	
    }

    public void run() {
	String[] sortert = new String[ord.length];
	for (int i = 0; i < ord.length; i++) {
	    sortert[finnPlass(ord[i])] = ord[i];
	}
	ord = sortert;
	fletteBuffer.add(this); //klar for fletting
	
	while (lever) {
	    fletteBuffer.vent();
	    flett();
	}
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
    public void flett() {
	if (!fletteklar) {
	    return;
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
	ord = nyArray;
	fletteklar = false;
	flettetFerdig.countDown();
	fletteBuffer.add(this);
    }

    public String[] getOrd() {
	return ord;
    }

    public void drep() {
	lever = false;
    }

    public void skriv() {
	for (int i = 0; i < ord.length; i++) {
	    System.out.println(ord[i]);
	}
	System.out.println("Antall ord: " + ord.length);
    }
}