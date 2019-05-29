import java.util.concurrent.LinkedBlockingQueue;

public class ThreadPool {
    // Variables de la piscina de hebras
    private final int nThreads; // cantidad de hebras
    private final PoolWorker[] threads; // Trabajador de la piscina
    private final LinkedBlockingQueue<Runnable> queue; // Cola de ejecucion
    // Constructor

    public ThreadPool(int nThreads) {
        this.nThreads = nThreads; // Definimos la cantidad de hebras
        this.queue = new LinkedBlockingQueue<Runnable>(50); // Creamos nuestra cola
        this.threads = new PoolWorker[nThreads]; // Creamos nuestros trabajadores
        // iniciamos las hebras
        for (int i = 0; i < nThreads; i++) {
            threads[i] = new PoolWorker();
            threads[i].start();
        }
    }

    public int getnThreads() {
        return nThreads;
    }

    // Metodo para ejecutar las tareas (la clase Process para ser mas especifico)
    public void execute(Runnable task) {
        synchronized (queue) {
            queue.add(task); // anadimos la tarea a la cola
            queue.notify(); // notificamos sobre la tarea nueva en la cola
        }
    }

    private class PoolWorker extends Thread {
        public void run() {
            Runnable task;
            while (true) {
                synchronized (queue) {
                    while (queue.isEmpty()) {
                        try {
                            queue.wait();
                        } catch (InterruptedException e) {
                            System.out.println("Un error ocurrio mientras la cola esperaba: " + e.getMessage());
                        }
                    }
                    task = queue.poll();
                }
                try {
                    task.run();
                } catch (RuntimeException e) {
                    System.out.println("La piscina de hebras se vio interrumpida debido a un error: " + e.getMessage());
                }
            }
        }
    }
}