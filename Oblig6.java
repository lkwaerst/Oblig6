class Oblig6 {
    
    public static void main(String[] args) {

// 	for (int i = 1; i < 101; i++) {
// 	    System.out.println("\n\nAntall traader: " + i);
// 	    String a = String.valueOf(i);
// 	    String[] meh = {a, "names.txt", "out.txt"};
// 	    new Kontroll(meh);
// 	}
// 	System.exit(0);
	
	if (args.length == 3) {
	    new Kontroll(args);
	}

	else {
	    System.out.println("Trenger 3 argumenter");
	}
    }
}