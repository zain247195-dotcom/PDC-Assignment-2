public class ReutersQuoteEvent extends TradeEvent {
    private double bidPrice;
    private double askPrice;

    public ReutersQuoteEvent(String senderId, long timestamp, double basePrice, double bidPrice, double askPrice) {
        super(senderId, timestamp, basePrice);
        this.bidPrice = bidPrice;
        this.askPrice = askPrice;
    }

    @Override
    public void dispatch() {
        double spread = askPrice - bidPrice;
        System.out.printf("Reuters Quote | Sender: %s | Bid: %.2f | Ask: %.2f | Bid-Ask Spread: %.2f%n", senderId, bidPrice, askPrice, spread);
    }
}
