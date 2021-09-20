package exercises04;

public class Person {

    private static long idCounter = 0;

    final long id;
    String name;
    int zip;
    String address;

    public Person() {
        synchronized (Person.class) {
            this.id = ++Person.idCounter; // or should this be idCounter++?
        }
    }

    public Person(long id) {
        this.id = id;
        synchronized (Person.class) {
            Person.idCounter = id + 1;
        }
    }

    public long getId() { // This is final, so we do not have to synchronize.
        return this.id;
    }

    public synchronized String getName() {
        return this.name;
    }

    public synchronized String getAddress() {
        return this.address;
    }

    public synchronized int getZip() {
        return this.zip;
    }

    public synchronized void setAddress(String address, int zip) {
        this.zip = zip;
        this.address = address;
    }

    public static void main(String[] args) {
        // Starts threads that create a new persons
        for (int i = 0; i < 10000; i++) {
            new Thread(() -> {
                var aaa = new Person();
                System.out.print(" " + aaa.getId());
            }).start();
        }

        // Wait for the threads to finish
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
