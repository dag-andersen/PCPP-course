package primer;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;

public class Worker extends AbstractBehavior<Worker.IsPrime> {
    /* --- Messages ------------------------------------- */
    public static final class IsPrime {
	public final int number;
	
	public IsPrime(int number) {
	    this.number = number;
	}
    }

    
    /* --- State ---------------------------------------- */
    private final ActorRef<Primer.PrimerCommand> primerServer;

    
    /* --- Constructor ---------------------------------- */
    private Worker(ActorContext<IsPrime> context,
		   ActorRef<Primer.PrimerCommand> primerServer) {
	super(context);
	this.primerServer = primerServer;
    }

    /* --- Actor initial behavior ----------------------- */
    public static Behavior<IsPrime> create(ActorRef<Primer.PrimerCommand> primerServer) {
	return Behaviors.setup((context) -> new Worker(context, primerServer));
    }

    
    /* --- Message handling ----------------------------- */
    @Override
    public Receive<IsPrime> createReceive() {
	return newReceiveBuilder()
	    .onMessage(IsPrime.class, this::onIsPrime)
	    .build();
    }

    /* --- Handlers ------------------------------------- */
    public Behavior<IsPrime> onIsPrime(IsPrime msg) {
	primerServer.tell(new Primer.PrimeResult(msg.number, isPrime(msg.number)));
	return this;
    }

    /* --- Auxiliary functions -------------------------- */
    private static boolean isPrime(int n) {
	int k = 2;
	while (k * k <= n && n % k != 0)
	    k++;
	return n >= 2 && k * k > n;
    }
}
