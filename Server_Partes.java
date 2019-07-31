import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.util.Hashtable;
@SuppressWarnings("unchecked")

public class Server_Partes {
    public static void escribir_IP(Hashtable<String, Boolean> IPS){
        try{
        FileOutputStream escritura = new FileOutputStream("IPS.ser");
        ObjectOutputStream ob_ser = new ObjectOutputStream(escritura);
        ob_ser.writeObject(IPS);
        ob_ser.close();
        escritura.close();
        } catch (Exception e){
            // TODO
        }
    }
    public static Object leer_IP(){
        try{
            FileInputStream salida = new FileInputStream("IPS.ser");
            ObjectInputStream objeto = new ObjectInputStream(salida);
            Object info = objeto.readObject();
            objeto.close();
            salida.close();
            return info;
        } catch (Exception e){
            // TODO
            return null;
        }
    }
    public static void escribir_ARCHIVOS(Hashtable<String, String[]> ARCHIVOS){
        try{
            FileOutputStream escritura = new FileOutputStream("ARCHIVOS.ser");
            ObjectOutputStream ob_ser = new ObjectOutputStream(escritura);
            ob_ser.writeObject(ARCHIVOS);
            ob_ser.close();
            escritura.close();
        } catch (Exception e){
            // TODO
        }
    }
    public static Hashtable<String,String[]> leer_ARCHIVOS(){
        try {
            FileInputStream salida = new FileInputStream("ARCHIVOS.ser");
            ObjectInputStream objeto = new ObjectInputStream(salida);
            Object info = objeto.readObject();
            objeto.close();
            salida.close();
            return (Hashtable<String,String[]>)info;
        } catch (Exception e) {
            // TODO
            Hashtable<String,String[]> info = new Hashtable<String,String[]>();
            return info;

        }
    }
    public static void main(String[] args) throws IOException {
    	// Intentamos abrir el puerto del servidor
        try {
            Hashtable<String, String[]> ARCHIVOS = new Hashtable<String, String[]>();
            Hashtable<String, Boolean> IPS = new Hashtable<String, Boolean>();
            //IPS = (Hashtable<String,Boolean>)leer_IP();
            ARCHIVOS = leer_ARCHIVOS();
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