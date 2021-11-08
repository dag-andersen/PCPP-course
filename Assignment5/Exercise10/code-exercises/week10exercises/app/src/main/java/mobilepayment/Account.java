package mobilepayment;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;


public class Account extends AbstractBehavior<Account.AccountCommand> {

    /* --- Messages ------------------------------------- */
    public interface AccountCommand {
    }

    public static final class DepositMessage implements AccountCommand {
        public final int amount;

        public DepositMessage(int amount) {
            this.amount = amount;
        }
    }

    /* --- State ---------------------------------------- */
    private final int balance;

    /* --- Constructor ---------------------------------- */
    private Account(ActorContext<AccountCommand> context) {
        super(context);
        balance = 2000;
    }

    /* --- Actor initial behavior ----------------------- */
    public static Behavior<AccountCommand> create() {
        return Behaviors.setup(Account::new);
    }

    /* --- Message handling ----------------------------- */
    @Override
    public Receive<AccountCommand> createReceive() {
        return newReceiveBuilder().onMessage(DepositMessage.class, this::onDepositMessage).build();
    }

    /* --- Handlers ------------------------------------- */

    public Behavior<AccountCommand> onDepositMessage(DepositMessage msg) {
        getContext().getLog().info("{}: Broadcast of message {}",
                getContext().getSelf().path().name(), msg.amount);
        return this;
    }
}
