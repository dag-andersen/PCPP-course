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
        //getContext().getLog().info("{}: Broadcast of DepositMessage {}", getContext().getSelf().path().name(), msg.amount);
        balance += msg.amount;
        return this;
    }
}
