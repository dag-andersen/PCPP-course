package mobilepayment;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;

public class MobileApp extends AbstractBehavior<MobileApp.MobileAppCommand> {

    public interface MobileAppCommand {
    }

    public static final class MakePaymentMessage implements MobileAppCommand {
        public final ActorRef<Bank.Command> bank;
        public final ActorRef<Account.Command> from;
        public final ActorRef<Account.Command> to;
        public final int amount;

        public MakePaymentMessage(ActorRef<Bank.Command> bank, ActorRef<Account.Command> from,
                ActorRef<Account.Command> to, int message) {
            this.bank = bank;
            this.to = to;
            this.from = from;
            this.amount = message;
        }
    }

    private MobileApp(ActorContext<MobileAppCommand> context) {
        super(context);
    }

    public static Behavior<MobileAppCommand> create(ActorRef<Bank.Command> observable_actor) {
        return Behaviors.setup(MobileApp::new);
    }

    @Override
    public Receive<MobileAppCommand> createReceive() {
        return newReceiveBuilder().onMessage(MakePaymentMessage.class, this::onTransaction).build();
    }

    public Behavior<MobileAppCommand> onTransaction(MakePaymentMessage msg) {
        // Exercise 3
        // msg.bank.tell(new Bank.TransactionMessage(msg.from, msg.to, msg.amount));

        // Exercise 4
        for (int i = 0; i < 100; i++) {
            // get random amount between -100 and 100
            int amount = (int) (Math.random() * 100);
            amount = Math.random() < 0.5 ? amount : amount * -1;
            msg.bank.tell(new Bank.TransactionMessage(msg.from, msg.to, amount));
        }
        return this;
    }
}
