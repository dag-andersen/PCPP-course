// For week 7
// raup@itu.dk * 10/10/2021
package exercises07;

import java.util.concurrent.atomic.AtomicReference;

// Lock free Histogram implementation - not Reentrant
class ReadWriteCASLock implements SimpleRWTryLockInterface {

    // AtomicReference
    private final AtomicReference<Holders> holder = new AtomicReference<Holders>();

    public static void main(String[] args) {
        // test the methods
    }

    public boolean readerTryLock() {
        var current = Thread.currentThread();
        Holders oldHolder = null;
        Holders newHolder = null;

        do {
            oldHolder = holder.get();
            if (oldHolder == null) {
                newHolder = new ReaderList(current, null);
            } else if (oldHolder instanceof Writer) {
                return false;
            } else if (oldHolder instanceof ReaderList) {
                ReaderList readerList = (ReaderList) oldHolder;
                if (readerList.contains(current)) return false; //throw new RuntimeException("ReaderList contains current thread");
                newHolder = new ReaderList(current, (ReaderList) oldHolder);
            }
        } while (!holder.compareAndSet(oldHolder, newHolder));

        return true;
    }

    public void readerUnlock() {
        Thread current = Thread.currentThread();
        Holders oldHolder;
        ReaderList newHolder;

        do {
            oldHolder = holder.get();
            if (oldHolder == null || oldHolder instanceof Writer) {
                throw new IllegalStateException("Holder is not ReaderList");
            } else { 
                ReaderList reader = (ReaderList) oldHolder;
                if (reader.contains(current)) {
                    newHolder = reader.remove(current);
                } else {
                    throw new IllegalStateException("Readerlist does not contain thread");
                } 
            }
        } while (!holder.compareAndSet(oldHolder, newHolder));
    }

    public boolean writerTryLock() {
        var concurrent = Thread.currentThread();
        var writer = new Writer(concurrent);
        return holder.compareAndSet(null, writer);
    }

    public void writerUnlock() {
        var current = Thread.currentThread();
        var oldHolder = holder.get();
        if (oldHolder == null || oldHolder instanceof ReaderList) {
            throw new IllegalStateException("No writer to unlock");
        }
        if (oldHolder instanceof Writer) {
            var writer = (Writer) oldHolder;
            if (writer.thread == current) {
                holder.compareAndSet(writer, null);
            } else {
                throw new IllegalStateException("Writer not lock holder");
            }
        }
    }

    private static abstract class Holders {
    }

    private static class ReaderList extends Holders {
        private final Thread thread;
        private final ReaderList next;

        public ReaderList(Thread t, ReaderList nxt) {
            thread = t;
            next = nxt;
        }

        public boolean contains(Thread t) {
            return thread == t || (next != null && next.contains(t));
         }

        public ReaderList remove(Thread t) {
            if (thread == t) {
                return next;
            } else if (next != null) {
                var newNext = next.remove(t);
                if (newNext == next) {
                    return this;
                } else {
                    return new ReaderList(thread, newNext);
                }    
            } else {
                return this;
            }
        }
    }

    private static class Writer extends Holders {
        public final Thread thread;

        public Writer(Thread t) {
            this.thread = t;
        }
    }
}
