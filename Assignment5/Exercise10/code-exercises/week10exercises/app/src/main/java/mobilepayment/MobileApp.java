package mobilepayment;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;

public class MobileApp extends AbstractBehavior<MobileApp.MobileAppCommand> {
    
    /* --- Messages ------------------------------------- */
    public interface MobileAppCommand {
    }

    public static final class MakePaymentMessage implements MobileAppCommand {
        public final ActorRef<Account.AccountCommand> from;
        public final ActorRef<Account.AccountCommand> to;
        public final int amount;

        public MakePaymentMessage(ActorRef<Account.AccountCommand> from, ActorRef<Account.AccountCommand> to,
                int message) {
            this.to = to;
            this.from = from;
            this.amount = message;
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
            .onMessage(MakePaymentMessage.class, this::onTransaction)
            .build();
    }

    /* --- Handlers ------------------------------------- */
    public Behavior<MobileAppCommand> onTransaction(MakePaymentMessage msg) {
        getContext().getLog().info("{}: Subscription confirmed by observable", getContext().getSelf().path().name());
        
        for (int i = 0; i < 100; i++) {
            // get random amount between 0 and 100
            int amount = (int) (Math.random() * 10);
            // random boolean
            if (Math.random() < 0.5) {
                banks.tell(new Bank.TransactionMessage(msg.from, msg.to, amount));
            } else {
                banks.tell(new Bank.TransactionMessage(msg.to, msg.from, amount));
            }
        }

        return this;
    }
}
