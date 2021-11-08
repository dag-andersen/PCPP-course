package mobilepayment;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;

public class Bank extends AbstractBehavior<Bank.BankCommand> {

    /* --- Messages ------------------------------------- */
    public interface BankCommand {
    }

    public static final class TransactionMessage implements BankCommand {
        public final ActorRef<Account.AccountCommand> from;
        public final ActorRef<Account.AccountCommand> to;
        public final int amount;

        public TransactionMessage(ActorRef<Account.AccountCommand> from, ActorRef<Account.AccountCommand> to,
                int message) {
            this.to = to;
            this.from = from;
            this.amount = message;
        }
    }

    /* --- State ---------------------------------------- */

    /* --- Constructor ---------------------------------- */
    private Bank(ActorContext<BankCommand> context) {
        super(context);
    }

    /* --- Actor initial behavior ----------------------- */
    public static Behavior<BankCommand> create() {
        return Behaviors.setup(Bank::new);
    }

    /* --- Message handling ----------------------------- */
    @Override
    public Receive<BankCommand> createReceive() {
        return newReceiveBuilder().onMessage(TransactionMessage.class, this::onTransactionMessage).build();
    }

    /* --- Handlers ------------------------------------- */

    public Behavior<BankCommand> onTransactionMessage(TransactionMessage msg) {
        getContext().getLog().info("{}: Broadcast of message {} to {} from {}", getContext().getSelf().path().name(),
                msg.amount, msg.to.path().name(), msg.from.path().name());
        msg.from.tell(new Account.DepositMessage(-msg.amount));
        msg.to.tell(new Account.DepositMessage(msg.amount));
        return this;
    }
}
