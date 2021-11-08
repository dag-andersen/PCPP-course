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

	// spawn the primer server
	final ActorRef<Primer.PrimerCommand> primerServer =
	    getContext().spawn(Primer.create(), "primer_server");

	// start the server with 20 workers
	primerServer.tell(new Primer.StartPrimer(20));

	// ask primality for some 10_000 random numbers between 1 and 50_000_000
	IntStream randomNumbers = new Random().ints(1,50_000_000);
	randomNumbers
	    .limit(10_000)
	    .forEach((number) -> {
		    primerServer.tell(new Primer.CheckPrime(number));
		}); 
	return this;
    }
}
