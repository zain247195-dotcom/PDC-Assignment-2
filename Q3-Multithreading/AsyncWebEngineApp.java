public class AsyncWebEngineApp {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Main JVM thread: " + Thread.currentThread().getName());

        TelemetryThread telemetry = new TelemetryThread();
        telemetry.setName("Telemetry-Worker");
        Thread logWorker = new Thread(new LogProcessorTask(), "Log-Worker");
        telemetry.start();
logWorker.start();

System.out.println("Telemetry state after start: " + telemetry.getState());
System.out.println("Log worker state after start: " + logWorker.getState());
telemetry.join();
logWorker.join();

System.out.println("Both workers completed.");
    }
}