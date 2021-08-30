package exercises01;

public class TestPrinterExperiments {

    Printer p = new Printer();

    public TestPrinterExperiments() {

        Thread t1 = new Thread(() -> {
            while (true) {
                p.print();
            }
        });
        Thread t2 = new Thread(() -> {
            while (true) {
                p.print();
            }
        });
        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException exn) {
            System.out.println("Some thread was interrupted");
        }
    }

    public static void main(String[] args) {
        new TestPrinterExperiments();
    }

}
