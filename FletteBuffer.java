//beholder for alle traader som er klare til aa flettes
class FletteBuffer {

    private SorteringsTraad[] klareTraader = new SorteringsTraad[2];
    private int antall;
    private volatile boolean ferdig = false;


    //legger traaden inn i beholderen, notifyer sorteringstraader om 2 traader er klare for fletting
    public synchronized void add(SorteringsTraad s) {
	klareTraader[antall++] = s;
	//System.out.println("Traad " + s.nr + "venter paa aa flette");
	
	if (antall > 1) {
	    //System.out.println("Naa flettes " + klareTraader[0].nr + " med " + klareTraader[1].nr);
	    klareTraader[0].setPartner(klareTraader[1]);
	    antall -= 2;
	    klareTraader[1].drep();
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

    public SorteringsTraad getFerdig() {
	return klareTraader[0];
    }
}