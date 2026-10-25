public class TelemetryThread extends Thread {
    @Override
    public void run() {
        for (int i = 1; i <= 5; i++) {
            System.out.println(getName() + " | System CPU and memory health update " + i);
            try {
                Thread.sleep(800);
            } catch (InterruptedException e) {
                interrupt();
                System.out.println("Telemetry worker interrupted");
                return;
            }
        }
    }
}
