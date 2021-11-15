package primer;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;

import java.util.stream.IntStream;
import java.util.Random;

public class Guardian extends AbstractBehavior<Guardian.KickOff> {
    /* --- Messages ------------------------------------- */
    public static final class KickOff { }

    /* --- State ---------------------------------------- */
    // empty

    
    /* --- Constructor ---------------------------------- */
    private Guardian(ActorContext<KickOff> context) {
	super(context);
    }

    /* --- Actor initial behavior ----------------------- */
    public static Behavior<KickOff> create() {
	return Behaviors.setup(Guardian::new);
    }

    
    /* --- Message handling ----------------------------- */
    @Override
    public Receive<KickOff> createReceive() {
	return newReceiveBuilder()
	    .onMessage(KickOff.class, this::onKickOff)
	    .build();
    }

    /* --- Handlers ------------------------------------- */
    public Behavior<KickOff> onKickOff(KickOff msg) {

	// Number of primes to check
	final int N = 10_000;
	//final int N = 1_000_000;

	// spawn the primer server	
	final ActorRef<Primer.PrimerCommand> primerServer =
	    getContext().spawn(Primer.create(N), "primer_server");

	// start the server with 16 workers
	primerServer.tell(new Primer.StartPrimer(16));

	// ask primality for N random numbers
	IntStream randomNumbers = new Random(42).ints(1_000_000_000,Integer.MAX_VALUE);
	randomNumbers
	    .limit(N)
	    .forEach((number) -> {
		    primerServer.tell(new Primer.CheckPrime(number));
		}); 
	return this;
    }
}
