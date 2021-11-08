package mobilepayment;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;

import java.util.stream.IntStream;

public class SystemGuardian extends AbstractBehavior<SystemGuardian.KickOff> {
    /* --- Messages ------------------------------------- */
    public static final class KickOff {
    }

    /* --- State ---------------------------------------- */
    // empty

    /* --- Constructor ---------------------------------- */
    private SystemGuardian(ActorContext<KickOff> context) {
        super(context);
    }

    /* --- Actor initial behavior ----------------------- */
    public static Behavior<KickOff> create() {
        return Behaviors.setup(SystemGuardian::new);
    }

    /* --- Message handling ----------------------------- */
    @Override
    public Receive<KickOff> createReceive() {
        return newReceiveBuilder().onMessage(KickOff.class, this::onKickOff).build();
    }

    /* --- Handlers ------------------------------------- */
    public Behavior<KickOff> onKickOff(KickOff msg) {
        // spawn the observable server
        final ActorRef<Bank.BankCommand> observableServer = getContext().spawn(Bank.create(),
                "observable_server");

        // spawn the N observers, and tell them to start
        final int N = 4;
        IntStream.range(1, N + 1).forEach((id) -> {
            final ActorRef<MobileApp.MobileAppCommand> observer = getContext().spawn(MobileApp.create(observableServer),
                    "observer_" + id);
        });
        return this;
    }
}
