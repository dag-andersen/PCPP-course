package primerfaulttolerant;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;
import akka.actor.typed.Terminated;
import akka.actor.typed.ChildFailed;

import java.util.Map;
import java.util.HashMap;

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
    Map<ActorRef<SingleJobWorker.IsPrime>,Integer> jobsInProgress;

    
    /* --- Constructor ---------------------------------- */
    private Worker(ActorContext<IsPrime> context,
		   ActorRef<Primer.PrimerCommand> primerServer) {
	super(context);
	this.primerServer = primerServer;
	this.singleJobWorkerId = 0;
	this.jobsInProgress = new HashMap<ActorRef<SingleJobWorker.IsPrime>,Integer>();
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
	    // Here order matters `ChildFailed extends Terminated`
	    .onSignal(ChildFailed.class, this::onChildFailed)
	    .onSignal(Terminated.class, this::onTerminated)
	    .build();
    }

    /* --- Handlers ------------------------------------- */
    public Behavior<IsPrime> onIsPrime(IsPrime msg) {
	// Create a new single job worker
	final ActorRef<SingleJobWorker.IsPrime> singleJobWorker =
	    getContext().spawn(SingleJobWorker.create(),
			       getContext().getSelf().path().name()+(++singleJobWorkerId));
	// Watch it in case it crashes
	getContext().watch(singleJobWorker);
	// Tell the worker to check the number
	singleJobWorker.tell(new SingleJobWorker.IsPrime(msg.number, primerServer));
	// Add the pair (singlejobworker, number to check) to the map
	jobsInProgress.put(singleJobWorker, msg.number);
	return this;
    }    


    public Behavior<IsPrime> onChildFailed(ChildFailed msg) {
	// The message contains the information of the actor that crashed
	// (it is automatically added by akka, and can be accessed with `getRef()`)
	ActorRef<Void> crashedChild = msg.getRef();
	// Retreive the task the crashed worker was checking
	Integer nonProcessedNumber = jobsInProgress.remove(crashedChild);
	if (nonProcessedNumber != null) {
	    // Create a new single job worker
	    final ActorRef<SingleJobWorker.IsPrime> newJobWorker =
	    	getContext().spawn(SingleJobWorker.create(),
	    			   getContext().getSelf().path().name()+(++singleJobWorkerId));
	    // Watch it in case it crashes (it can be that the replacement also crashes)
	    getContext().watch(newJobWorker);
	    // Tell the worker to check the number
	    newJobWorker.tell(new SingleJobWorker.IsPrime(nonProcessedNumber, primerServer));
	    // Add the pair (singlejobworker, number to check) to the map
	    jobsInProgress.put(newJobWorker, nonProcessedNumber);
	    getContext().getLog().info("{}: Worker {} crashed trying to check if {} is prime due to {}\nNew worker {} retrying to compute the task.",
				       getContext().getSelf().path().name(),
				       crashedChild.path().name(),nonProcessedNumber, msg.cause(),
				       newJobWorker.path().name());
				       
	} else {
	    // Never going to be executed
	    getContext().getLog().info("{}: No job from worker {} found.",
				       getContext().getSelf().path().name(),
				       crashedChild.path().name());
	}
	return this;	
    }

    public Behavior<IsPrime> onTerminated(Terminated msg) {
	// The message contains the information of the actor that terminated
	// (it is automatically added by akka, and can be accessed with `getRef()`)
	if(jobsInProgress.remove(msg.getRef()) != null) {
	    getContext().getLog().info("{}: {} terminated normally.",
				       getContext().getSelf().path().name(),
				       msg.getRef().path().name());	    
	} else {
	    // Never going to be executed
	    getContext().getLog().info("{}: No job from worker {} found.",
				       getContext().getSelf().path().name(),
				       msg.getRef().path().name());
	}

	return this;	
    }
}
