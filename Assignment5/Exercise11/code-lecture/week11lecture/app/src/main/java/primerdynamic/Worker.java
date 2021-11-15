package primerdynamic;

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
    private int singleJobWorkerId;

    
    /* --- Constructor ---------------------------------- */
    private Worker(ActorContext<IsPrime> context,
		   ActorRef<Primer.PrimerCommand> primerServer) {
	super(context);
	this.primerServer = primerServer;
	this.singleJobWorkerId = 0;
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
	final ActorRef<SingleJobWorker.IsPrime> singleJobWorker =
	    getContext().spawn(SingleJobWorker.create(),
			       getContext().getSelf().path().name()+(++singleJobWorkerId));
	singleJobWorker.tell(new SingleJobWorker.IsPrime(msg.number, primerServer));
	return this;
    }
}
