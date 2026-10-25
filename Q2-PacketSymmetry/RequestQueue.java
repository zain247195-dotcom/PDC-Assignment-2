public class RequestQueue {
    private char[] data;
    private int front;
    private int rear;
    private int size;

    public RequestQueue(int capacity) {
        data = new char[capacity];
        front = 0;
        rear = -1;
        size = 0;
    }

    public void enqueue(char c) {
        if (size == data.length) {
            throw new IllegalStateException("Queue is full");
        }
        rear = (rear + 1) % data.length;
        data[rear] = c;
        size++;
    }

    public char dequeue() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }
        char value = data[front];
        front = (front + 1) % data.length;
        size--;
        return value;
    }

    public boolean isEmpty() {
        return size == 0;
    }
}
