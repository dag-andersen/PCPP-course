package mobilepayment;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;

import java.util.Random;

public class MobileApp extends AbstractBehavior<MobileApp.MobileAppCommand> {
    
    /* --- Messages ------------------------------------- */
    public interface MobileAppCommand {
    }

    public static final class TransactionMessage implements MobileAppCommand {
        public final ActorRef<Account.AccountCommand> from;
        public final ActorRef<Account.AccountCommand> to;
        public final int message;

        public TransactionMessage(ActorRef<Account.AccountCommand> from, ActorRef<Account.AccountCommand> to,
                int message) {
            this.to = to;
            this.from = from;
            this.message = message;
        }
    }

    /* --- State ---------------------------------------- */
    private final ActorRef<Bank.BankCommand> banks;

    /* --- Constructor ---------------------------------- */
    private MobileApp(ActorContext<MobileAppCommand> context, ActorRef<Bank.BankCommand> banks) {
        super(context);
        this.banks = banks;
    }

    /* --- Actor initial behavior ----------------------- */
    public static Behavior<MobileAppCommand> create(ActorRef<Bank.BankCommand> observable_actor) {
        return Behaviors.setup((context) -> new MobileApp(context, observable_actor));
    }

    /* --- Message handling ----------------------------- */
    @Override
    public Receive<MobileAppCommand> createReceive() {
        return newReceiveBuilder()
            .onMessage(TransactionMessage.class, this::onTransaction)
            .build();
    }

    /* --- Handlers ------------------------------------- */
    public Behavior<MobileAppCommand> onTransaction(TransactionMessage msg) {
        getContext().getLog().info("{}: Subscription confirmed by observable", getContext().getSelf().path().name());
        
        banks.tell(new Bank.TransactionMessage(msg.from, msg.to, msg.message));
        return this;
    }
}
