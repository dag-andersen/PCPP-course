package broadcaster;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;

import java.util.List;
import java.util.ArrayList;

public class Observable extends AbstractBehavior<Observable.ObservableCommand> {
    /* --- Messages ------------------------------------- */
    public interface ObservableCommand { }

    public static final class Subscribe implements ObservableCommand {
	public final ActorRef<Observer.ObserverCommand> ref;

	public Subscribe(ActorRef<Observer.ObserverCommand> ref) {
	    this.ref = ref;
	}
    }
    
    public static final class Unsubscribe implements ObservableCommand {
	public final ActorRef<Observer.ObserverCommand> ref;

	public Unsubscribe(ActorRef<Observer.ObserverCommand> ref) {
	    this.ref = ref;
	}	
    }
    
    public static final class EmitMessage implements ObservableCommand {
	public final ActorRef<Observer.ObserverCommand> sender;
	public final String message;

	public EmitMessage(ActorRef<Observer.ObserverCommand> sender,
			   String message) {
	    this.sender  = sender;
	    this.message = message;
	}
    }

    /* --- State ---------------------------------------- */
    private final List<ActorRef<Observer.ObserverCommand>> subscribers;


    /* --- Constructor ---------------------------------- */
    private Observable(ActorContext<ObservableCommand> context) {
	super(context);
	this.subscribers = new ArrayList<ActorRef<Observer.ObserverCommand>>();
    }

    /* --- Actor initial behavior ----------------------- */
    public static Behavior<ObservableCommand> create() {
	return Behaviors.setup(Observable::new);
    }

    /* --- Message handling ----------------------------- */
    @Override
    public Receive<ObservableCommand> createReceive() {
	return newReceiveBuilder()
	    .onMessage(Subscribe.class, this::onSubscribe)
	    .onMessage(Unsubscribe.class, this::onUnsubscribe)
	    .onMessage(EmitMessage.class, this::onEmitMessage)
	    .build();
    }

    /* --- Handlers ------------------------------------- */
    public Behavior<ObservableCommand> onSubscribe(Subscribe msg) {
	getContext().getLog().info("{}: Actor {} subscribed",
				   getContext().getSelf().path().name(),
				   msg.ref.path().name());
	subscribers.add(msg.ref);
	msg.ref.tell(new Observer.SubscriptionOK());
	return this;
    }

    public Behavior<ObservableCommand> onUnsubscribe(Unsubscribe msg) {
	subscribers.remove(msg.ref);
	getContext().getLog().info("{}: Actor {} unsubscribed",
				   getContext().getSelf().path().name(),
				   msg.ref.path().name());
	msg.ref.tell(new Observer.UnsubscriptionOK());
	return this;
    }

    public Behavior<ObservableCommand> onEmitMessage(EmitMessage msg) {
	getContext().getLog().info("{}: Broadcast of message {} by '{}' initiated",
				   getContext().getSelf().path().name(),
				   msg.message,
				   msg.sender.path().name());
	subscribers
	    // concurrency within the actor! (not recommended, use actors instead)
	    .parallelStream()
	    // do not send the message to the sender
	    .filter((subscriber) -> !subscriber.equals(msg.sender))
	    // send message to others
	    .forEach((subscriber) -> {
		    subscriber.tell(new Observer.BroadcastMessage(msg.sender, msg.message));
		});
	return this;
    }
}
