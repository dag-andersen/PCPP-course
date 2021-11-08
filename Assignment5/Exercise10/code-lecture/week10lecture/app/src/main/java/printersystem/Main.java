package printersystem;

import akka.actor.typed.ActorSystem;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
    
	// actor system
	final ActorSystem<Printer.PrinterCommand> guardian =
	    ActorSystem.create(Printer.create(), "printer_server");

	// sending some messages	
	guardian.tell(new Printer.StoreString("string 1"));
	guardian.tell(new Printer.StoreString("string 2"));
	guardian.tell(new Printer.PrintWithoutStoring("string 3"));
	guardian.tell(new Printer.PrintHistory());

	// wait until user presses enter
	try {
	    System.out.println(">>> Press ENTER to exit <<<");
	    System.in.read();
	}
	catch (IOException e) {
	    System.out.println("Error " + e.getMessage());
	    e.printStackTrace();
	} finally {
	    guardian.terminate();
	}
    
    }
    
}
