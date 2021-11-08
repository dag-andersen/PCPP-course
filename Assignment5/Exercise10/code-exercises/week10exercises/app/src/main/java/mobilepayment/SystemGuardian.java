package mobilepayment;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;

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
        final ActorRef<Bank.BankCommand> bank = getContext().spawn(Bank.create(), "bank");
        final ActorRef<MobileApp.MobileAppCommand> mobileApp = getContext().spawn(MobileApp.create(bank), "mobile-app");

        // spawn the N observers, and tell them to start
        final ActorRef<Account.AccountCommand> account1 = getContext().spawn(Account.create(), "account-" + 1);
        final ActorRef<Account.AccountCommand> account2 = getContext().spawn(Account.create(), "account-" + 2);

        mobileApp.tell(new MobileApp.MakePaymentMessage(account1, account2, 100));

        return this;
    }
}
