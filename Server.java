import java.io.IOException;
import java.net.ServerSocket;

public class Server {
    public static void main(String[] args) throws IOException {
    	// Intentamos abrir el puerto del servidor
        try {
            ServerSocket listener = new ServerSocket(59091);
            System.out.println("El servidor esta en marcha...");
            System.out.println("Ctrl + C para salir");
            // Creamos nuestra piscina con 50 hebras
            ThreadPool piscina = new ThreadPool(50);
            while (true) {
            	// Escuchamos a quien contacte el servidor
                piscina.execute(new Process(listener.accept()));
            }
        } catch (Exception e){
            // TODO
        }
    }
}