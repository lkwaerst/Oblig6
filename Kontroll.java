import java.util.concurrent.*;
import java.util.*;
import java.io.*;
class Kontroll extends Thread{

    int antTraader;
    File inFil;
    File utFil;
    String[] alleOrd;
    SorteringsTraad[] sorteringstraader;
    private CountDownLatch barriere;

    Kontroll(String[] args) {
	try {
	    antTraader = Integer.parseInt(args[0]);
	    barriere = new CountDownLatch(antTraader);
	    sorteringstraader = new SorteringsTraad[antTraader];
	    inFil = new File(args[1]);
	    lesFil();
	    //System.out.println("lest fil");
	    utFil = new File(args[2]);
	    double startTid = System.nanoTime()/1000000;
	    fordel();
	    //System.out.println("Fordelt");
	    sorter();
	    flett();
	    double sluttTid = System.nanoTime()/1000000;
	    System.out.println("Kjoeretid: " + (sluttTid - startTid) +  " ms");
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
	int ordPerTraad = alleOrd.length / antTraader;
	SorteringsTraad forrigeTraad = new SorteringsTraad(null, this, null);
	
	for (int i = 0; i < antTraader; i++) {
	    String[] ord = new String[ordPerTraad];
	    for (int j = 0; j < ordPerTraad; j++) {
		ord[j] = alleOrd[i * ordPerTraad + j];
	    }
	    sorteringstraader[i] = new SorteringsTraad(ord, this, barriere);
	    forrigeTraad.setPartner(sorteringstraader[i]);
	    forrigeTraad = sorteringstraader[i];
	}
    }

    private void sorter() {
	boolean ferdigSortert = false;
	for (int i = 0; i < sorteringstraader.length; i++) {
	    sorteringstraader[i].start();
	}
	try {
	    barriere.await();
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
    }
	    
       
    private void flett() {
	//System.out.println("Naa skal det flettes gitt");
	while (sorteringstraader.length > 1) {
	    barriere = new CountDownLatch(sorteringstraader.length/2);
	    SorteringsTraad[] ny = new SorteringsTraad[(int)Math.ceil(sorteringstraader.length / 2.0)]; 

	    for (int i = 0; i < sorteringstraader.length; i+=2) {
		ny[(int)Math.ceil(i/2.0)] = sorteringstraader[i];
		sorteringstraader[i].flett(barriere);
		if (i < sorteringstraader.length-2) {
		    sorteringstraader[i].setPartner(sorteringstraader[i+2]);
		}
	    }
	    sorteringstraader = ny;
	    
	    //venter mellom hver fletterunde
	    try {
		barriere.await();
	    }
	    catch (Exception e) {
		e.printStackTrace();
	    }
	}
    }
    public void skriv() {
	sorteringstraader[0].skriv();
    }
}	    
	    
	