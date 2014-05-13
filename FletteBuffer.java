//beholder for alle traader som er klare til aa flettes
class FletteBuffer {

    private SorteringsTraad[] klareTraader = new SorteringsTraad[2];
    private int antall;


    //legger traaden inn i beholderen, notifyer sorteringstraader om 2 traader er klare for fletting
    public synchronized void add(SorteringsTraad s) {
	klareTraader[antall++] = s;
	
	if (antall > 1) {
	    klareTraader[0].setPartner(klareTraader[1]);
	    antall -= 2;
	    klareTraader[1].drep();
	}
	notifyAll();
    }

    //alle som skal vekkes naar en ny traad legges inn i bufferen venter her
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