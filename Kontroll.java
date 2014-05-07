import java.util.concurrent.*;
import java.util.*;
import java.io.*;
class Kontroll extends Thread{

    static int antTraader;
    File inFil;
    File utFil;
    String[] alleOrd;
    SorteringsTraad[] sorteringstraader;
    private int antFlettingger;
    private FletteBuffer fletteBuffer;
    double startTid;
    double sluttTid;

    Kontroll(String[] args) {
	try {
	    antTraader = Integer.parseInt(args[0]);
	    sorteringstraader = new SorteringsTraad[antTraader];
	    fletteBuffer = new FletteBuffer();
	    inFil = new File(args[1]);
	    lesFil();
	    utFil = new File(args[2]);
	    startTid = System.nanoTime()/1000000;
	    fordel();
	    vent();
	    skriv();
	}

	catch (FileNotFoundException e) {
	    System.out.println("Fant ikke en fil");
	}
	catch (Exception e) {
	    e.printStackTrace();
	}	
    }

    private void lesFil() throws Exception {
	Scanner les = new Scanner(inFil);
	alleOrd = new String[Integer.parseInt(les.next())];
	for (int i = 0; les.hasNext(); i++) {
	    alleOrd[i] = les.next();
	}
    }

    //oppretter traader og fordeler ord mellom dem
    private void fordel() {
	int rest = alleOrd.length % antTraader;
	int ordPerTraad = alleOrd.length / antTraader;
	
	for (int i = 0; i < antTraader; i++) {
	    String[] ord = new String[ordPerTraad];
	    if (i == antTraader - 1) {
		ord = new String[ordPerTraad + rest];
	    }
	    for (int j = 0; j < ord.length; j++) {
		ord[j] = alleOrd[i * ordPerTraad + j];
	    }
	    sorteringstraader[i] = new SorteringsTraad(ord, fletteBuffer);
	    sorteringstraader[i].start();
	}
    }	    
       
    public void skriv() {
	sluttTid = System.nanoTime() / 1000000;
	//fletteBuffer.getFerdig().skriv();
	System.out.println("Kjoeretid: " + (sluttTid - startTid) + " ms ");
    }

    private void vent() {
	while (!fletteBuffer.ferdig()) {
	    fletteBuffer.vent();
	}
    }

//     private void sjekk() {
// 	String[] sjekk = fletteBuffer.getFerdig().getOrd();

// 	String ord = sjekk[0];
// 	for (int i = 1; i < sjekk.length; i++) {
// 	    if (sjekk[i].compareTo(ord) <= 0) {
// 		System.out.println("Au da");
// 	    }
// 	}
//     }
}
	    
	