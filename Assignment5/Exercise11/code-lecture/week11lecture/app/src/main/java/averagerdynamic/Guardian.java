package averagerdynamic;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;

import java.util.List;
import java.util.stream.Collectors;
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

	// spawn the averager server
	final ActorRef<Averager.ComputeAverage> averager =
	    getContext().spawn(Averager.create(), "averager_server");

	// Generate a list of random integers of size N
	final int N = 1_000;
	List<Integer> numbers = new Random()
	    .ints(0,Integer.MAX_VALUE) // Generate a stream of positive integers
	    .limit(N) // Take N
	    .boxed()  // Wrap them into a Stream<Integer>
	    .collect(Collectors.toList()); // Convert them to a List<Integer>
	averager.tell(new Averager.ComputeAverage(numbers));
	return this;
    }
}
