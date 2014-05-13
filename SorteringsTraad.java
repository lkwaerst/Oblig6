import java.util.concurrent.*;
class SorteringsTraad extends Thread {
    
    private String[] ord;
    private SorteringsTraad flettePartner;
    private boolean fletteklar = false;   //hindrer traader som ikke skal flettes i aa flettes
    private FletteBuffer fletteBuffer;
    private CountDownLatch flettetFerdig;
    private boolean lever = true;

    SorteringsTraad(String[] ord, FletteBuffer f, CountDownLatch ferdig) {
	fletteBuffer = f;
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

    //forteller traaden hvilken traad den skal flette med
    public void setPartner(SorteringsTraad s) {
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
	fletteBuffer.add(this); //klar for ny fletting
    }

    public String[] getOrd() {
	return ord;
    }

    public void drep() {
	lever = false;
    }
}