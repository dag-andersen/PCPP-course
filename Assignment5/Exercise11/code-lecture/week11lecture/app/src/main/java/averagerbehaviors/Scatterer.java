package averagerbehaviors;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;

import java.util.List;
import java.util.stream.IntStream;
import java.util.Random;

public class Scatterer extends AbstractBehavior<Scatterer.ScattererCommand> {
    /* --- Messages ------------------------------------- */
    public static final class ScattererCommand {
	public final List<Integer> numbers;
	public final ActorRef<Gatherer.GathererCommand> gatherer;
	
	public ScattererCommand(List<Integer> numbers,
			       ActorRef<Gatherer.GathererCommand> gatherer) {
	    this.numbers = numbers;
	    this.gatherer = gatherer;
	}
    }
    
    /* --- State ---------------------------------------- */
    private final int averageSize;

    
    /* --- Constructor ---------------------------------- */
    private Scatterer(ActorContext<ScattererCommand> context, int averageSize) {
	super(context);
	this.averageSize = averageSize;
    }

    /* --- Actor initial behavior ----------------------- */
    public static Behavior<ScattererCommand> create(int averageSize) {
	return Behaviors.setup(context -> new Scatterer(context,averageSize));
    }

    
    /* --- Message handling ----------------------------- */
    @Override
    public Receive<ScattererCommand> createReceive() {
	return newReceiveBuilder()
	    .onMessage(ScattererCommand.class, this::onScattererCommand)
	    .build();
    }

    /* --- Handlers ------------------------------------- */
    public Behavior<ScattererCommand> onScattererCommand(ScattererCommand msg) {
	int listSize = msg.numbers.size();
	if (listSize == 1) {
	    // Debugging
	    // printInfo(getActorName() + ": send " + msg.numbers.get(0).toString() +
	    // 	      ". Gatherer: " + msg.gatherer.path().name());
	    
	    // Only for comparing performance
	    try{Thread.sleep(1);} catch (InterruptedException e) { }
	    msg.gatherer.tell(new Gatherer.GathererCommand((double) msg.numbers.get(0) / averageSize));
	} else {
	    final int half = msg.numbers.size()/2;
	    List<Integer> left  = msg.numbers.subList(0,half);
	    List<Integer> right = msg.numbers.subList(half,listSize);

	    // Debugging
	    // printInfo(getActorName() + "List to process: "+msg.numbers);
	    // printInfo(getActorName() + ": split");
	   
	    ActorRef<ScattererCommand> scatterer1 =
		getContext().spawn(Scatterer.create(averageSize), getActorName()+"1");
	    ActorRef<ScattererCommand> scatterer2 =
		getContext().spawn(Scatterer.create(averageSize), getActorName()+"2");
	    ActorRef<Gatherer.GathererCommand> gatherer =
		getContext().spawn(Gatherer.create(msg.gatherer), getActorName()+"_gatherer");

	    scatterer1.tell(new ScattererCommand(left,gatherer));
	    scatterer2.tell(new ScattererCommand(right,gatherer));
	}
	return this;
    }

    /* --- Auxiliary methods----------------------------- */
    private void printInfo(String str) {
	getContext().getLog().info(str);
    }

    private String getActorName() {
	return getContext().getSelf().path().name();
    }

}
