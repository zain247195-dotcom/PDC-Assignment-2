public class PacketSymmetryAuditor {
    public static void main(String[] args) {
        String payload = args.length > 0 ? args[0] : "ABCCBA";
        TransactionStack stack = new TransactionStack(payload.length());
        RequestQueue queue = new RequestQueue(payload.length());

        for (int i = 0; i < payload.length(); i++) {
            char c = payload.charAt(i);
            stack.push(c);
            queue.enqueue(c);
        }

        boolean symmetric = true;
        while (!stack.isEmpty() && !queue.isEmpty()) {
            if (stack.pop() != queue.dequeue()) {
                symmetric = false;
                break;
            }
        }

        System.out.println("Packet payload: " + payload);
        System.out.println("Bi-directional symmetry: " + (symmetric ? "VERIFIED (palindrome)" : "NOT VERIFIED (not a palindrome)"));
    }
}
