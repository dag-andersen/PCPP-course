package mobilepayment;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;

public class Bank extends AbstractBehavior<Bank.Command> {

    interface Command {
    }

    public static final class TransactionMessage implements Command {
        public final ActorRef<Account.Command> from;
        public final ActorRef<Account.Command> to;
        public final int amount;

        public TransactionMessage(ActorRef<Account.Command> from, ActorRef<Account.Command> to, int message) {
            this.to = to;
            this.from = from;
            this.amount = message;
        }
    }

    private Bank(ActorContext<Command> context) {
        super(context);
    }

    public static Behavior<Command> create() {
        return Behaviors.setup(Bank::new);
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder().onMessage(TransactionMessage.class, this::onTransactionMessage).build();
    }

    public Behavior<Command> onTransactionMessage(TransactionMessage msg) {
        msg.from.tell(new Account.DepositMessage(-msg.amount));
        msg.to.tell(new Account.DepositMessage(msg.amount));
        return this;
    }
}
