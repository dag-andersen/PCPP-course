package averagerdynamic;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;

import java.util.List;
import java.util.stream.IntStream;
import java.util.Random;

public class Averager extends AbstractBehavior<Averager.ComputeAverage> {
    /* --- Messages ------------------------------------- */
    public static final class ComputeAverage {
	public final List<Integer> numbers;
	
	public ComputeAverage(List<Integer> numbers) {
	    this.numbers = numbers;
	}
    }
    
    /* --- State ---------------------------------------- */
    // empty

    
    /* --- Constructor ---------------------------------- */
    private Averager(ActorContext<ComputeAverage> context) {
	super(context);
    }

    /* --- Actor initial behavior ----------------------- */
    public static Behavior<ComputeAverage> create() {
	return Behaviors.setup(Averager::new);
    }

    
    /* --- Message handling ----------------------------- */
    @Override
    public Receive<ComputeAverage> createReceive() {
	return newReceiveBuilder()
	    .onMessage(ComputeAverage.class, this::onComputeAverage)
	    .build();
    }

    /* --- Handlers ------------------------------------- */
    public Behavior<ComputeAverage> onComputeAverage(ComputeAverage msg) {
	int listSize = msg.numbers.size();
	if (listSize == 0) {
	    printInfo("Empty list");
	} else if (listSize == 1) {
	    printInfo("Average = "+msg.numbers.get(0).toString());
	} else {
	    final int half = msg.numbers.size()/2;
	    final List<Integer> left  = msg.numbers.subList(0,half);
	    final List<Integer> right = msg.numbers.subList(half,listSize);

	    // Debugging
	    // printInfo(getActorName() + "List to process: "+msg.numbers);
	    long initTime = System.currentTimeMillis();
	    printInfo("Expected avg: "+msg.numbers
	    	      .stream()
	    	      .mapToDouble(n -> {
			      // Artificial one milisecond delay
			      try{Thread.sleep(1);} catch (InterruptedException e) { }
			      return n/listSize;
				  })
	    	      .sum() + " | Time: "+(System.currentTimeMillis()-initTime)+"ms");

	    ActorRef<Scatterer.ScattererCommand> splitter1 =
		getContext().spawn(Scatterer.create(listSize), getActorName()+"1");
	    ActorRef<Scatterer.ScattererCommand> splitter2 =
		getContext().spawn(Scatterer.create(listSize), getActorName()+"2");
	    ActorRef<Gatherer.GathererCommand> gatherer =
		getContext().spawn(Gatherer.create(null, System.currentTimeMillis()),
				   getActorName()+"_gatherer");
	    
	    splitter1.tell(new Scatterer.ScattererCommand(left, gatherer));
	    splitter2.tell(new Scatterer.ScattererCommand(right, gatherer));
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
