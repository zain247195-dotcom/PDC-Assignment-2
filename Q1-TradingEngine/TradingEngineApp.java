public class TradingEngineApp {
    public static void main(String[] args) {
        TradeEvent event1 = new FIXOrderEvent("FIX-Gateway", System.currentTimeMillis(), 1000.00, "ORD-1001");
        TradeEvent event2 = new ReutersQuoteEvent("Reuters-Gateway", System.currentTimeMillis(), 250.00, 249.80, 250.25);
        TradeEvent event3 = new TradeEvent("Internal-System", System.currentTimeMillis(), 500.00);

        event1.dispatch();
        event2.dispatch();
        event3.dispatch();
    }
}
