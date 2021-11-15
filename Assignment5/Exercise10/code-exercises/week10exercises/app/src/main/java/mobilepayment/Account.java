package mobilepayment;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;

public class Account extends AbstractBehavior<Account.Command> {

    interface Command {
    }

    public static final class DepositMessage implements Command {
        public final int amount;

        public DepositMessage(int amount) {
            this.amount = amount;
        }
    }

    public static Behavior<Command> create() {
        return Behaviors.setup(Account::new);
    }

    private int balance = 1000;

    private Account(ActorContext<Command> context) {
        super(context);
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder().onMessage(DepositMessage.class, this::onDepositMessage).build();
    }

    public Behavior<Command> onDepositMessage(DepositMessage msg) {
        String amount = String.format("%9d", msg.amount);
        String oldBalance = String.format("%9d", balance);
        balance += msg.amount;
        String newBalance = String.format("%14d", balance);
        System.out.println(getContext().getSelf().path().name() + oldBalance + amount + newBalance);
        return this;
    }
}
