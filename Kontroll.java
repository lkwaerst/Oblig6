import java.util.*;
import java.io.*;
class Kontroll extends Thread{

    int antTraader;
    File inFil;
    File utFil;
    String[] alleOrd;
    SorteringsTraad[] sorteringstraader;

    Kontroll(String[] args) {
	try {
	    antTraader = Integer.parseInt(args[0]);
	    sorteringstraader = new SorteringsTraad[antTraader];
	    inFil = new File(args[1]);
	    lesFil();
	    utFil = new File(args[2]);
	    fordel();
	    sorter();
	    flett();
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
	SorteringsTraad forrigeTraad = new SorteringsTraad(null, this);
	
	for (int i = 0; i < antTraader; i++) {
	    String[] ord = new String[ordPerTraad];
	    for (int j = 0; j < ordPerTraad; j++) {
		ord[j] = alleOrd[i * ordPerTraad + j];
	    }
	    sorteringstraader[i] = new SorteringsTraad(ord, this);
	    forrigeTraad.setPartner(sorteringstraader[i]);
	}
    }

    private void sorter() {
	boolean ferdigSortert = false;
	for (int i = 0; i < sorteringstraader.length; i++) {
	    sorteringstraader[i].start();
	}
	int teller = 0;
	while (!ferdigSortert) {
	    try {
		wait();
	    }
	    catch (Exception e) {
		e.printStackTrace();
	    }
	    System.out.println(teller++);
	    if (teller == antTraader) {
		ferdigSortert = true;
	    }
	}
	    
	
    }
    private void flett() {
	while (sorteringstraader.length > 1) {
	    SorteringsTraad[] ny = new SorteringsTraad[(int)Math.ceil(sorteringstraader.length / 2.0)]; 
	    SorteringsTraad forrigeTraad = new SorteringsTraad(null, this);

	    for (int i = 0; i < sorteringstraader.length; i+=2) {
		ny[i/2] = sorteringstraader[i];
		sorteringstraader[i].flett();
		forrigeTraad.setPartner(sorteringstraader[i]);
	    }
	    sorteringstraader = ny;
	}
	skriv();
    }

    public void skriv() {
	sorteringstraader[0].skriv();
    }
}	    
	    
	