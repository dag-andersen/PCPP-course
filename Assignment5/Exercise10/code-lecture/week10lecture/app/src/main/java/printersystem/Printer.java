package printersystem;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;

import java.util.List;
import java.util.ArrayList;

public class Printer extends AbstractBehavior<Printer.PrinterCommand> {
    /* --- Messages ------------------------------------- */
    public interface PrinterCommand { }

    public static final class StoreString implements PrinterCommand {
	public final String string;

	public StoreString(String string) {
	    this.string = string;
	}	
    }

    public static final class PrintHistory implements PrinterCommand { }
    
    public static final class PrintWithoutStoring implements PrinterCommand {
	public final String string;

	public PrintWithoutStoring(String string) {
	    this.string = string;
	}
    }

    /* --- State ---------------------------------------- */
    private final List<String> history;


    /* --- Constructor ---------------------------------- */
    private Printer(ActorContext<PrinterCommand> context) {
	super(context);
	this.history = new ArrayList<String>();
    }

    /* --- Actor initial behavior ----------------------- */
    public static Behavior<PrinterCommand> create() {
	return Behaviors.setup(Printer::new);
    }

    /* --- Message handling ----------------------------- */
    @Override
    public Receive<PrinterCommand> createReceive() {
	return newReceiveBuilder()
	    .onMessage(StoreString.class, this::onStoreString)
	    .onMessage(PrintHistory.class, this::onPrintHistory)
	    .onMessage(PrintWithoutStoring.class, this::onPrintWithoutStoring)
	    .build();
    }

    /* --- Handlers ------------------------------------- */
    public Behavior<PrinterCommand> onStoreString(StoreString msg) {
	getContext().getLog().info("Storing string: {}",msg.string);
	history.add(msg.string);
	return this;
    }

    public Behavior<PrinterCommand> onPrintHistory(PrintHistory msg) {
	getContext().getLog().info("History of received strings:");
	history
	    .stream()
	    .forEach((string) -> {
		    getContext().getLog().info(string);
		});
	return this;
    }

    public Behavior<PrinterCommand> onPrintWithoutStoring(PrintWithoutStoring msg) {
	getContext().getLog().info("Printing string: {}", msg.string);
	return this;
    }
}

