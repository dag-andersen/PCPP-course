package mathsserver;

// Hint: The imports below may give you hints for solving the exercise.
//       But feel free to change them.

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;
import akka.actor.typed.Terminated;
import akka.actor.typed.ChildFailed;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Server extends AbstractBehavior<Server.ServerCommand> {
	/* --- Messages ------------------------------------- */
	public interface ServerCommand {
	}

	public static final class ComputeTasks implements ServerCommand {
		public final List<Task> tasks;
		public final ActorRef<Client.ClientCommand> client;

		public ComputeTasks(List<Task> tasks, ActorRef<Client.ClientCommand> client) {
			this.tasks = tasks;
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
	private class TaskAndClient {
		Task task;
		ActorRef<Client.ClientCommand> client;

		public TaskAndClient(Task task, ActorRef<Client.ClientCommand> client) {
			this.task = task;
			this.client = client;
		}
	}

	HashMap<ActorRef<Worker.WorkerCommand>, Task> activeWorkers;
	LinkedList<ActorRef<Worker.WorkerCommand>> idleWorkers;
	LinkedList<TaskAndClient> pendingTasks; 
	int minWorkers;
	int maxWorkers;

	/* --- Constructor ---------------------------------- */
	private Server(ActorContext<ServerCommand> context, int minWorkers, int maxWorkers) {
		super(context);
		this.activeWorkers = new HashMap<>();
		this.idleWorkers = new LinkedList<>();
		this.pendingTasks = new LinkedList<>();
		this.minWorkers = minWorkers;
		this.maxWorkers = maxWorkers;

		while(idleWorkers.size() < minWorkers) {
			var worker = spawnWorker("worker-" + (idleWorkers.size() + activeWorkers.size()));
			idleWorkers.add(worker);
		}
	}

	private ActorRef<Worker.WorkerCommand> spawnWorker(String name) { 
		ActorRef<Worker.WorkerCommand> worker = getContext().spawn(Worker.create(getContext().getSelf()), name);
		getContext().watch(worker);
		return worker;
	}

	/* --- Actor initial state -------------------------- */
	public static Behavior<ServerCommand> create(int minWorkers, int maxWorkers) {
		return Behaviors.setup(context -> new Server(context, minWorkers, maxWorkers));
	}

	/* --- Message handling ----------------------------- */
	@Override
	public Receive<ServerCommand> createReceive() {
		return newReceiveBuilder().onMessage(ComputeTasks.class, this::onComputeTasks)
				.onMessage(WorkDone.class, this::onWorkDone)
				.onSignal(ChildFailed.class, this::onChildFailed)
				.build();
	}

	/* --- Handlers ------------------------------------- */

	public Behavior<ServerCommand> onChildFailed(ChildFailed msg) {
		// The message contains the information of the actor that crashed
		// (it is automatically added by akka, and can be accessed with `getRef()`)
		ActorRef<Void> crashedChild = msg.getRef();
		// Retreive the task the crashed worker was checking
		Task nonProcessedTask = activeWorkers.remove(crashedChild);

		var worker = spawnWorker(msg.getRef().path().name());
		idleWorkers.add(worker);

		getContext().getLog().info("{}: Worker {} crashed trying to compute {} due to {}\nNew worker {} spawned and added to idleWorkers.",
		getContext().getSelf().path().name(),
		crashedChild.path().name(),nonProcessedTask, msg.cause(),
		worker.path().name());

		return this;	
	}

	public Behavior<ServerCommand> onComputeTasks(ComputeTasks msg) {
		for (var task : msg.tasks) {
			if (idleWorkers.size() > 0) {
				var worker = idleWorkers.pop();
				worker.tell(new Worker.ComputeTask(task, msg.client));
				activeWorkers.put(worker, task);
			} else {
				if (activeWorkers.size() < maxWorkers) {
					var worker = spawnWorker("worker-" + (idleWorkers.size() + activeWorkers.size()));
					worker.tell(new Worker.ComputeTask(task, msg.client));
					activeWorkers.put(worker, task);
				} else {
					pendingTasks.add(new TaskAndClient(task, msg.client));
				}
			}
		}
		return this;
	}

	public Behavior<ServerCommand> onWorkDone(WorkDone msg) {
		activeWorkers.remove(msg.worker);
		if (pendingTasks.size() > 0) {
			var taskAndClient = pendingTasks.pop();
			msg.worker.tell(new Worker.ComputeTask(taskAndClient.task, taskAndClient.client));
			activeWorkers.put(msg.worker, taskAndClient.task);
		} else {
			idleWorkers.add(msg.worker);
		}
		return this;
	}
}
