import java.io.IOException;
import java.net.ServerSocket;
import java.util.Hashtable;
public class Server_Partes {
    public static void main(String[] args) throws IOException {
    	// Intentamos abrir el puerto del servidor
        try {
            Hashtable<String, String[]> ARCHIVOS = new Hashtable<String, String[]>();
            Hashtable<String, Boolean> IPS = new Hashtable<String, Boolean>();
            ServerSocket listener = new ServerSocket(59090);
            System.out.println("El servidor esta en marcha...");
            System.out.println("Ctrl + C para salir");
            // Creamos nuestra piscina con 50 hebras
            ThreadPool piscina = new ThreadPool(50);
            while (true) {
            	// Escuchamos a quien contacte el servidor
                piscina.execute(new Process_Partes(listener.accept(),ARCHIVOS,IPS));
            }
        } catch (Exception e){
            // TODO
        }
    }
}