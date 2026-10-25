public class AsyncWebEngineApp {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Main JVM thread: " + Thread.currentThread().getName());

        TelemetryThread telemetry = new TelemetryThread();
        telemetry.setName("Telemetry-Worker");
        Thread logWorker = new Thread(new LogProcessorTask(), "Log-Worker");
    }
}