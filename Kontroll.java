import java.util.concurrent.*;
import java.util.*;
import java.io.*;
class Kontroll extends Thread{

    private static int antTraader;
    private File inFil;
    private File utFil;
    private String[] alleOrd;
    private SorteringsTraad[] sorteringstraader;
    private FletteBuffer fletteBuffer;
    private CountDownLatch flettetFerdig;   //brukes til å få maintraaden til aa vente paa fletting

    Kontroll(String[] args) {
	try {
	    antTraader = Integer.parseInt(args[0]);
	    sorteringstraader = new SorteringsTraad[antTraader];
	    flettetFerdig = new CountDownLatch(antTraader - 1);
	    fletteBuffer = new FletteBuffer();
	    inFil = new File(args[1]);
	    utFil = new File(args[2]);

	    lesFil();
	    fordel();
	    flettetFerdig.await(); 
	    skriv();
	    System.exit(0);
	}

	catch (FileNotFoundException e) {
	    System.out.println("Fant ikke en fil");
	}
	
	catch (IOException e) {
	    System.out.println("IO feil");
	    e.printStackTrace();
	}
	catch (ArrayIndexOutOfBoundsException e) {
	    System.out.println("Feil ant ord i filen");
	}
	catch (FeilOrdTallException e) {
	    System.out.println("Feil ant ord i filen");
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
	if (alleOrd[alleOrd.length - 1] == null) {
	    throw new FeilOrdTallException();
	}
    }

    //oppretter traader og fordeler ord mellom dem
    private void fordel() {
	int rest = alleOrd.length % antTraader;
	int ordPerTraad = alleOrd.length / antTraader;
	
	for (int i = 0; i < antTraader; i++) {
	    String[] ord = new String[ordPerTraad];
	    //dumper resten paa siste traaden
	    if (i == antTraader - 1) {
		ord = new String[ordPerTraad + rest];
	    }
	    for (int j = 0; j < ord.length; j++) {
		ord[j] = alleOrd[i * ordPerTraad + j];
	    }
	    sorteringstraader[i] = new SorteringsTraad(ord, fletteBuffer, flettetFerdig);
	    sorteringstraader[i].start();
	}
    }      
    
    //skriver ordene til foerste traad i bufferen til utfilen
    public void skriv() throws Exception {
	PrintWriter skriv = new PrintWriter(new FileWriter(utFil));
	String[] ord = fletteBuffer.getFerdig().getOrd();
	
	for (int i = 0; i < ord.length; i++) {
	    skriv.println(ord[i]);
	}
	skriv.close();
    }
}


class FeilOrdTallException extends Exception {
}
	    
	