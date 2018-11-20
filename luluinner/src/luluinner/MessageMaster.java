package luluinner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import luluinner.model.Client;
import luluinner.model.TcpMessage;
import luluinner.upper.UpperAgentHandler;

public class MessageMaster {

    private static final MessageMaster INSTANCE = new MessageMaster();
    private List<MessageWorker> workers = new ArrayList<MessageWorker>();
    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private Lock readLock = readWriteLock.readLock();
    private Lock writeLock = readWriteLock.writeLock();

    private MessageMaster() {
    }

    public static MessageMaster getInstance() {
        return INSTANCE;
    }

    public void addMessage(TcpMessage message)
    {
        Client client = message.getClient();
        int hashCode = client.hashCode();
        readLock.lock();
        try {
            int length = workers.size();
            try {
                if (length > 0) {
                    MessageWorker messageWorker = workers.get(hashCode % length);
                    messageWorker.addMessage(message);
                } else {
                    System.out.println("addMessage failed, no workers.");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } finally {
            readLock.unlock();
        }
    }
    
    public void addMessageWorker(UpperAgentHandler handler) {
        MessageWorker messageWorker = new MessageWorker(handler);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(messageWorker);
        executor.shutdown();

        writeLock.lock();
        try {
            workers.add(messageWorker);
        } finally {
            writeLock.unlock();
        }
    }

    class MessageWorker implements Runnable {
        private BlockingQueue<TcpMessage> queue = new LinkedBlockingQueue<TcpMessage>(20000);
        private UpperAgentHandler handler = null;
        
        public MessageWorker(UpperAgentHandler handler) {
            this.handler = handler;
        }

        public void addMessage(TcpMessage message) throws InterruptedException {
            queue.put(message);
        }

        @Override
        public void run() {
            while(true)
            {
                try {
                    TcpMessage msg = queue.take();
                    System.out.println("MessageWorker:" + msg);
                    handler.send(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        
    }
}
