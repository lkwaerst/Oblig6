//beholder for alle traader som er klare til aa flettes
class FletteBuffer {

    private SorteringsTraad[] klareTraader = new SorteringsTraad[2];
    private int antall;
    private int antFlettinger;
    private volatile boolean ferdig = false;


    //legger traaden inn i beholderen, notifyer sorteringstraader om 2 traader er klare for fletting
    public synchronized void add(SorteringsTraad s, boolean flettet) {
	klareTraader[antall++] = s;
	//System.out.println("Traad " + s.nr + "venter paa aa flette");

	if (flettet) {
	    antFlettinger++;
	}
	if (antFlettinger == Kontroll.antTraader - 1) {
	    ferdig = true;
	}
	
	if (antall > 1) {
	    //System.out.println("Naa flettes " + klareTraader[0].nr + " med " + klareTraader[1].nr);
	    klareTraader[0].setPartner(klareTraader[1]);
	    antall -= 2;
	}
	notifyAll();
    }

    public synchronized void vent() {
	try {
	    wait();
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public boolean ferdig() {
	return ferdig;
    }

    public SorteringsTraad getFerdig() {
	return klareTraader[0];
    }
}