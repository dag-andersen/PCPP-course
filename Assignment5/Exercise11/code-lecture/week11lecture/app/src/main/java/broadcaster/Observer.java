package broadcaster;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;

import java.util.Random;

public class Observer extends AbstractBehavior<Observer.ObserverCommand> {
    /* --- Messages ------------------------------------- */
    public interface ObserverCommand { }

    public static final class BroadcastMessage implements ObserverCommand {
	public final ActorRef<ObserverCommand> ref;
	public final String message;
	
	public BroadcastMessage(ActorRef<ObserverCommand> ref, String message) {
	    this.ref = ref;
	    this.message = message;
	}
    }

    public static final class SubscriptionOK implements ObserverCommand { }

    public static final class UnsubscriptionOK implements ObserverCommand { }

    public static final class ObserverStarter implements ObserverCommand { }

    

    
    /* --- State ---------------------------------------- */
    private final ActorRef<Observable.ObservableCommand> observable_actor;

    
    /* --- Constructor ---------------------------------- */
    private Observer(ActorContext<ObserverCommand> context,
		       ActorRef<Observable.ObservableCommand> observable_actor) {
	super(context);
	this.observable_actor = observable_actor;
    }

    /* --- Actor initial behavior ----------------------- */
    public static Behavior<ObserverCommand>
	create(ActorRef<Observable.ObservableCommand> observable_actor) {
	return Behaviors.setup((context) -> new Observer(context,observable_actor));
    }

    
    /* --- Message handling ----------------------------- */
    @Override
    public Receive<ObserverCommand> createReceive() {
	return newReceiveBuilder()
	    .onMessage(BroadcastMessage.class, this::onBroadcastMessage)
	    .onMessage(SubscriptionOK.class, this::onSubscriptionOK)
	    .onMessage(UnsubscriptionOK.class, this::onUnsubscriptionOK)
	    .onMessage(ObserverStarter.class, this::onObserverStarter)
	    .build();
    }

    /* --- Handlers ------------------------------------- */
    public Behavior<ObserverCommand> onBroadcastMessage(BroadcastMessage msg) {
	getContext().getLog().info("{}: Message '{}' by {} received",
				   getContext().getSelf().path().name(),
				   msg.message,
				   msg.ref.path().name());
	return this;
    }

    public Behavior<ObserverCommand> onSubscriptionOK(SubscriptionOK msg) {
	getContext().getLog().info("{}: Subscription confirmed by observable",
				   getContext().getSelf().path().name());

	// Randomly decide whether the actor sends a message to broadcast
	if (new Random().nextBoolean()) {
	    String message = "Salutations from " + getContext().getSelf().path().name();
	    getContext().getLog().info("{}: send message '{}'",
				       getContext().getSelf().path().name(),
				       message);
	    observable_actor.tell(new Observable.EmitMessage(getContext().getSelf(), message));

	    // unsubscribe after sending the message
	    observable_actor.tell(new Observable.Unsubscribe(getContext().getSelf()));
	}	
	return this;
    }

    public Behavior<ObserverCommand> onUnsubscriptionOK(UnsubscriptionOK msg) {
	getContext().getLog().info("{}: Unsubcription confirmed by observable",
				   getContext().getSelf().path().name());
	return this;
    }

    public Behavior<ObserverCommand> onObserverStarter(ObserverStarter msg) {
	// subscribe
	observable_actor.tell(new Observable.Subscribe(getContext().getSelf()));	
	return this;
    }
}
