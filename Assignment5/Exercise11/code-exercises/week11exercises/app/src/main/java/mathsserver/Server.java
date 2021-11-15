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
	HashMap<ActorRef<Worker.WorkerCommand>, Task> workers = new HashMap<>();
	ArrayList<TaskAndClient> pendingTasks = new ArrayList<>();
	int minWorkers;
	int maxWorkers;

	private class TaskAndClient {
		Task task;
		ActorRef<Client.ClientCommand> client;

		public TaskAndClient(Task task, ActorRef<Client.ClientCommand> client) {
			this.task = task;
			this.client = client;
		}
	}

	/* --- Constructor ---------------------------------- */
	private Server(ActorContext<ServerCommand> context, int minWorkers, int maxWorkers) {
		super(context);
		this.minWorkers = minWorkers;
		this.maxWorkers = maxWorkers;
		fillMinWorkers();
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
				.onSignal(Terminated.class, this::onTerminated)
				// To be extended
				.build();
	}

	/* --- Handlers ------------------------------------- */
	public Behavior<ServerCommand> onComputeTasks(ComputeTasks msg) {
		for (var task : msg.tasks) {
			var worker = GetFreeWorker(getContext());
			if (worker != null) {
				worker.tell(new Worker.ComputeTask(task, msg.client));
			} else {
				pendingTasks.add(new TaskAndClient(task, msg.client));
			}
		}
		return this;
	}

	public Behavior<ServerCommand> onWorkDone(WorkDone msg) {

		var taskAndClient = pendingTasks.remove(0);

		if (taskAndClient != null) {
			msg.worker.tell(new Worker.ComputeTask(taskAndClient.task, taskAndClient.client));
		} else {
			workers.remove(msg.worker);
			fillMinWorkers();
		}
		return this;
	}

	public Behavior<ServerCommand> onChildFailed(ChildFailed msg) {
		fillMinWorkers();
		return this;
	}

	public Behavior<ServerCommand> onTerminated(Terminated msg) {
		// Do nothing
		return this;
	}

	/* --- Helpers -------------------------------------- */
	public ActorRef<Worker.WorkerCommand> GetFreeWorker(ActorContext<ServerCommand> context) {
		var x = workers.entrySet().stream().filter(entry -> entry.getValue() == null).findFirst();
		if (x.isPresent())
			return x.get().getKey();
		else if (workers.size() < maxWorkers) {
			ActorRef<Worker.WorkerCommand> worker = context.spawn(Worker.create(context.getSelf()),
					"worker-" + workers.size());
			workers.put(worker, null);
			return worker;
		}
		return null;
	}

	public void fillMinWorkers() {
		getContext().getLog().info("{}: Filling idle workers", getContext().getSelf().path().name());
		while (workers.size() < minWorkers) {
			ActorRef<Worker.WorkerCommand> worker = getContext().spawn(Worker.create(getContext().getSelf()),
					"worker-" + workers.size());
			workers.put(worker, null);
		}
	}
}
