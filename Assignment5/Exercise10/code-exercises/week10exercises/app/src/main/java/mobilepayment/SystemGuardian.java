package mobilepayment;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;

public class SystemGuardian extends AbstractBehavior<SystemGuardian.KickOff> {
    public static final class KickOff {
    }

    private SystemGuardian(ActorContext<KickOff> context) {
        super(context);
    }

    public static Behavior<KickOff> create() {
        return Behaviors.setup(SystemGuardian::new);
    }

    @Override
    public Receive<KickOff> createReceive() {
        return newReceiveBuilder().onMessage(KickOff.class, this::onKickOff).build();
    }

    public Behavior<KickOff> onKickOff(KickOff msg) {

        final ActorRef<Bank.Command> b1 = getContext().spawn(Bank.create(), "b1");
        final ActorRef<Bank.Command> b2 = getContext().spawn(Bank.create(), "b2");

        final ActorRef<MobileApp.MobileAppCommand> mobileApp = getContext().spawn(MobileApp.create(b1), "mobile_app_1");
        final ActorRef<MobileApp.MobileAppCommand> mobileApp2 = getContext().spawn(MobileApp.create(b2),
                "mobile_app_2");

        final ActorRef<Account.Command> a1 = getContext().spawn(Account.create(), "account_" + 1);
        final ActorRef<Account.Command> a2 = getContext().spawn(Account.create(), "account_" + 2);

        mobileApp.tell(new MobileApp.MakePaymentMessage(b1, a1, a2, 100));
        mobileApp2.tell(new MobileApp.MakePaymentMessage(b2, a2, a1, 50));

        return this;
    }
}
