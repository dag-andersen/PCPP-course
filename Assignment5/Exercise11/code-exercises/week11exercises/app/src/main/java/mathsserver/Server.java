package mathsserver;

// Hint: The imports below may give you hints for solving the exercise.
//       But feel free to change them.

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.ChildFailed;
import akka.actor.typed.Terminated;
import akka.actor.typed.javadsl.*;

import java.util.Queue;
import java.util.List;
import java.util.LinkedList;
import java.util.stream.IntStream;

import mathsserver.Task;
import mathsserver.Task.BinaryOperation;

public class Server extends AbstractBehavior<Server.ServerCommand> {
    /* --- Messages ------------------------------------- */
    public interface ServerCommand { }
    
    public static final class ComputeTasks implements ServerCommand {
	public final List<Task> tasks;
	public final ActorRef<Client.ClientCommand> client;

	public ComputeTasks(List<Task> tasks,
				 ActorRef<Client.ClientCommand> client) {
	    this.tasks  = tasks;
	    this.client = client;
	}
    }

    public static final class WorkDone implements ServerCommand {
	ActorRef<Worker.WorkerCommand> worker;

	public WorkDone(ActorRef<Worker.WorkerCommand> worker) {
	    this.worker = worker;
	}
    }
    
    /* --- State ---------------------------------------- */
    // To be implemented
    
    

    /* --- Constructor ---------------------------------- */
    private Server(ActorContext<ServerCommand> context,
		   int minWorkers, int maxWorkers) {
    	super(context);
	// To be implemented
	
    }


    /* --- Actor initial state -------------------------- */
    public static Behavior<ServerCommand> create(int minWorkers, int maxWorkers) {
    	return Behaviors.setup(context -> new Server(context, minWorkers, maxWorkers));
    }
    

    /* --- Message handling ----------------------------- */
    @Override
    public Receive<ServerCommand> createReceive() {
    	return newReceiveBuilder()
    	    .onMessage(ComputeTasks.class, this::onComputeTasks)
    	    .onMessage(WorkDone.class, this::onWorkDone)
	    // To be extended
    	    .build();
    }


    /* --- Handlers ------------------------------------- */
    public Behavior<ServerCommand> onComputeTasks(ComputeTasks msg) {
	// To be implemented
    	return this;
    }

    public Behavior<ServerCommand> onWorkDone(WorkDone msg) {
	// To be implemented
	return this;	
    }
}
