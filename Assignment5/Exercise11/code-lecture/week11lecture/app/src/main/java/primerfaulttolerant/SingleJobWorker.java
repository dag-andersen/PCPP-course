package primerfaulttolerant;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;

public class SingleJobWorker extends AbstractBehavior<SingleJobWorker.IsPrime> {
    /* --- Messages ------------------------------------- */
    public static final class IsPrime {
	public final int number;
	public final ActorRef<Primer.PrimerCommand> server;
	
	public IsPrime(int number, ActorRef<Primer.PrimerCommand> server) {
	    this.number = number;
	    this.server = server;
	}
    }

    
    /* --- State ---------------------------------------- */
    //empty

    
    /* --- Constructor ---------------------------------- */
    private SingleJobWorker(ActorContext<IsPrime> context) {
	super(context);
    }

    /* --- Actor initial behavior ----------------------- */
    public static Behavior<IsPrime> create() {
	return Behaviors.setup(SingleJobWorker::new);
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
	// 10% probability of crashing
	if (Math.random() < 0.1) {
	    throw new RuntimeException("Simulated random crash.");	    
	}
	
	// Normal execution
	msg.server.tell(new Primer.PrimeResult(msg.number, isPrime(msg.number)));
	getContext().getLog().info("{}: Computation finished terminating myself",
				   getContext().getSelf().path().name());
	return Behaviors.stopped();
    }

    /* --- Auxiliary functions -------------------------- */
    private static boolean isPrime(int n) {
	int k = 2;
	while (k * k <= n && n % k != 0)
	    k++;
	return n >= 2 && k * k > n;
    }
}
