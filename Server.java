import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
    	// Intentamos abrir el puerto del servidor
        try {
            Socket socket = new Socket("10.6.40.141", 59090); // Nos registramos en el server_partes
            DataInputStream dis = new DataInputStream(socket.getInputStream()); // Creamos un stream de entrada de datos desde el servidor
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream()); // Creamos un stream de salida de datos hacia el servidor
            String msg = dis.readUTF();
            InetAddress inetAddress = InetAddress.getLocalHost();
            String Entrada = "register " + inetAddress.getHostAddress();
            System.out.println(Entrada);
            dos.writeUTF(Entrada); // Enviamos la entrada al servidor
            dis = new DataInputStream(socket.getInputStream());
            msg = dis.readUTF();
            System.out.println(msg);
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