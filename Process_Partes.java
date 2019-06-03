
/*
Importes necesarios para el funcionamiento de la clase
*/
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.InetAddress;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Random;
@SuppressWarnings("unchecked") // Necesario por cast en runtime [De objeto a Hashtable]


public class Process_Partes implements Runnable {
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
    public static String send_file(String nombre,String IP){
        try{
            Socket socket = new Socket(IP, 59091);
            DataInputStream dis = new DataInputStream(socket.getInputStream()); // Creamos un stream de entrada de datos desde el servidor
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream()); // Creamos un stream de salida de datos hacia el servidor
            String msg = dis.readUTF(); // Esperamos respuesta del servidor
            System.out.println("Subiendo Archivo...");
            String Entrada = "put " + nombre.split("/")[2];
            dos.writeUTF(Entrada); // Enviamos la entrada al servidor
            File archivo = new File(nombre); // Abrimos el archivo a enviar
            if (archivo.exists()) { // Verificamos la existencia del archivo
                RandomAccessFile archivo_s = new RandomAccessFile(nombre, "r"); // Abrimos el archivo en modo lectura
                int cantidad = (int) archivo_s.length(); // Obtenemos el largo del archivo a enviar (en bytes)
                dos = new DataOutputStream(socket.getOutputStream()); // Creamos un stream de salida
                dos.writeInt(cantidad); // Enviamos la cantidad de bytes al servidor
                byte[] Array = new byte[cantidad]; // Creamos el array que contendra el archivo
                archivo_s.readFully(Array); // Leemos el archivo y lo alojamos en el array
                dos = new DataOutputStream(socket.getOutputStream()); // Creamos un stream de salida
                dos.write(Array); // Enviamos el array al servidor
                dis = new DataInputStream(socket.getInputStream()); // Creamos un stream de entrada y lo esperamos
                System.out.println(dis.readUTF()); // Leemos la entrada y la imprimimos
                archivo_s.close(); // Cerramos el archivo
                // socket.close();
            } else { // El archivo no existe
                System.out.println("Archivo No Existe");
                dos = new DataOutputStream(socket.getOutputStream()); // Creamos un stream de salida
                dos.writeInt(0); // Enviamos un 0 a forma de no existencia al servidor
                socket.close();
            }
            return "Archivo";
        } catch(Exception e){
            return "Error";
        }
    }
    public static int del_file(String archivo){
        return 0;
    }
    public static void ls(){
        
    }
    public static boolean Ping(String direccion){
		try{
			InetAddress res = InetAddress.getByName(direccion);
			boolean respuesta = res.isReachable(20000);
			System.out.println("Alcanzable? -> " + respuesta);
			return respuesta;
		} catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}
    // Nuestras principales variables de comunicacion es el socket del cliente
    private Socket socket;
    private Hashtable<String, String[]> ARCHIVOS;
    private Hashtable<String, Boolean> IPS;
    // Constructor de la clase
      public Process_Partes(Socket socket, Hashtable<String, String[]> ARCHIVOS, Hashtable<String, Boolean> IPS){
        this.socket = socket;
        this.ARCHIVOS = ARCHIVOS;
        this.IPS = IPS;
    }
    // Metodo para el funcionamiento de la hebra en el servidor
    @Override
      public void run() {
        // Variables usadas para el log como la transferencia de datos entre servidor y cliente
        DateFormat dateformat = new SimpleDateFormat("EEE d-MM-YYYY hh:mm:ss"); // Plantilla del timestamp en el log
        DataOutputStream dos; // Stream de salida de datos con el cliente
        DataInputStream dis; // Stream de entrada de datos con el cliente
        FileOutputStream log; // Stream para el archivo de log
        try {
            log = new FileOutputStream("./log.txt", true); // Anexamos los eventos al log
            File folder = new File("./Server"); // Obtenemos nuestro folder del servidor en caso de usarse
            File[] listOfFiles = folder.listFiles(); // Obtenemos la lista de archivos en el servidor
            File archivo_s; // Variable para verificar si los archivos existen en el servidor
            RandomAccessFile archivo; // Variable para enviar los archivos como array de bytes
            int Contador; // Variable usada para transportar enteros
            int cantidad; // Variable usada para transportar enteros
            byte[] Array; // Variable usada para transportar array de bytes
            OutputStream os; // Variable usada para escribir a un archivo
            String[] ls_aux = new String[listOfFiles.length]; // Variable que contiene los nombres de archivo del servidor
            String to_log; // String usado para escribir en el log
            to_log = dateformat.format(Calendar.getInstance().getTime()) + "\t" + socket + "\t" + "Conectado\n"; // Instanciacion de string para el log
            try { // Bloque Try-catch
                log.write(to_log.getBytes()); // Escribimos en el log
                dos = new DataOutputStream(socket.getOutputStream()); // Creamos un stream de salida al cliente
                dos.writeUTF("Bienvenido al Servidor"); // Le damos la bienvenida al servidor
                try { // Bloque Try-catch
                    dis = new DataInputStream(socket.getInputStream()); // Creamos un stream de entrada al cliente
                    String Entrada = dis.readUTF(); // Esperamos un string por parte del cliente, el comando a realizar
                    String[] Entrada_parse = Entrada.split(" "); // Parseamos la entrada con un espacio de la forma "verbo nombre_archivo" o "verbo"
                    while (Entrada.length() > 0) { // Mientras la entrada del cliente no sea nula
                        if (Entrada.equals("ls")) { // Si el verbo es un ls 
                            //TODO
                            to_log = dateformat.format(Calendar.getInstance().getTime()) + "\t" + socket + "\t" + Entrada + "\n"; // Armamos el string para el log
                            log.write(to_log.getBytes()); // Escribimos el string en el archivo de log
                            folder = new File("./Server"); // Abrimos el directorio del servidor
                            listOfFiles = folder.listFiles(); // Listamos los archivos
                            Contador = 0; // Fijamos nuestro contador
                            System.out.println("Peticion de ls: " + socket); // Avisamos de la peticion
                            // En este for obtenemos la cantidad de archivos y un arreglo con sus nombres
                            for (File file : listOfFiles) {
                                if (file.isFile()) {
                                    ls_aux[Contador] = file.getName();
                                    Contador++;
                                }
                            }
                            dos = new DataOutputStream(socket.getOutputStream()); // Creamos un stream de salida al cliente
                            dos.writeInt(Contador); // Enviamos la cantidad de archivos al cliente
                            // Enviamos los nombres de los archivos al servidor uno a la vez
                            while (Contador > 0) {
                                dos = new DataOutputStream(socket.getOutputStream());
                                dos.writeUTF(ls_aux[Contador - 1]);
                                Contador--;
                            }
                        } else if (Entrada_parse[0].equals("get")) { // Si el verbo es un get 
                            //TODO
                            to_log = dateformat.format(Calendar.getInstance().getTime()) + "\t" + socket + "\t" + Entrada + "\n"; // Armamos el string para el log
                            log.write(to_log.getBytes()); // Escribimos el string en el archivo de log
                            System.out.println("Peticion de get: " + socket); // Avisamos de la peticion
                            archivo_s = new File("./Server/" + Entrada_parse[1]); // Obtenemos una variable File del archivo solicitado
                            if (archivo_s.exists()){ // Comprobamos si el archivo existe
                                archivo = new RandomAccessFile("./Server/" + Entrada_parse[1], "r"); // Abrimos el archivo en modo lectura
                                Contador = (int) archivo.length(); // Obtenemos el largo del archivo (la cantidad de bytes)
                                dos = new DataOutputStream(socket.getOutputStream()); // Creamos un stream de salida al cliente
                                dos.writeInt(Contador); // Enviamos el largo del archivo al cliente
                                Array = new byte[Contador]; // Creamos un array para almacenar el archivo
                                archivo.readFully(Array); // Escribimos el archivo al array
                                dos = new DataOutputStream(socket.getOutputStream()); // Creamos un stream de salida al cliente
                                dos.write(Array); // Enviamos el array con el archivo en el
                                archivo.close(); // Cerramos el archivo
                                System.out.println("Archivo Enviado"); // Notificamos del envio
                            } else { // El archivo no existe
                                System.out.println("Archivo No Existe"); // Notificamos de la no existencia
                                dos = new DataOutputStream(socket.getOutputStream()); // Creamos un stream de salida al cliente
                                dos.writeInt(0); // Enviamos un 0 al cliente a modo que el archivo no existe
                            }
                        } else if (Entrada_parse[0].equals("put")) { // Si el verbo es un put 
                            //CHECK
                            to_log = dateformat.format(Calendar.getInstance().getTime()) + "\t" + socket + "\t" + Entrada + "\n"; // Armamos el string para el log
                            log.write(to_log.getBytes()); // Escribimos el string en el archivo de log
                            System.out.println("Peticion de put: " + socket); // Avisamos de la peticion
                            dis = new DataInputStream(socket.getInputStream()); // Creamos un stream de entrada al cliente
                            cantidad = dis.readInt(); // Esperamos un entero por parte del cliente (la cantidad de bytes del archivo a recibir)
                            if (cantidad > 0) { // Si el entero es mayor que uno el archivo existe
                                archivo_s = new File("./Server/" + Entrada_parse[1]); // Creamos el archivo
                                os = new FileOutputStream(archivo_s); // Creamos el stream para escribir en el archivo
                                Array = new byte[cantidad]; // Creamos un array de bytes que contendra el archivo
                                dis = new DataInputStream(socket.getInputStream()); // Esperamos por el archivo
                                dis.readFully(Array); // Escribimos el archivo en el array
                                os.write(Array); // Pasamos el array al archivo en la parte del servidor
                                System.out.println("Archivo Obtenido"); // Notificamos que hemos recibido el archivo
                                os.close(); // Cerramos el archivo (el stream a el)
                                Encode64 coder = new Encode64("./Server/" + Entrada_parse[1]);
                                String codificado = coder.codificar();
                                Spliter separador = new Spliter(codificado);
                                ArrayList<String> lista = separador.Separar();
                                ArrayList<String> direcciones = new ArrayList<String>();
                                String tamanio = Integer.toString(lista.size());
                                direcciones.add(tamanio);
                                String[] seleccion = IPS.keySet().toArray(new String[IPS.size()]);
                                String seleccionado;
                                for (String nombre : lista) {
                                    // Falta revisar si las IP estan disponibles
                                    seleccionado = seleccion[new Random().nextInt(seleccion.length)];
                                    System.out.println("Enviando: " + nombre + ", hacia: " + seleccionado);
                                    send_file(nombre, seleccionado);
                                    direcciones.add(seleccionado);
                                }
                                ARCHIVOS.put(Entrada_parse[1], direcciones.toArray(new String[0]));
                                escribir_ARCHIVOS(ARCHIVOS);
                                ARCHIVOS = leer_ARCHIVOS();
                                dos = new DataOutputStream(socket.getOutputStream()); // Creamos un stream de salida al cliente
                                dos.writeUTF("Solicitud Completada"); // Enviamos un string al cliente
                            } else { // El archivo no existe
                                System.out.println("Archivo Inexistente"); // Notificamos la no existencia del archivo
                            }
                        } else if (Entrada_parse[0].equals("del")) { // Si el verbo es del
                            // TODO
                            to_log = dateformat.format(Calendar.getInstance().getTime()) + "\t" + socket + "\t" + Entrada + "\n"; // Armamos el string para el log
                            log.write(to_log.getBytes()); // Escribimos el string en el archivo de log
                            System.out.println("Peticion de del: " + socket); // Avisamos de la peticion
                            archivo_s = new File("./Server/" + Entrada_parse[1]); // Creamos el acceso al archivo
                            if (archivo_s.delete()) { // Verificamos si el archivo existe
                                dos = new DataOutputStream(socket.getOutputStream()); // Creamos un stream de salida al cliente
                                dos.writeUTF("Solicitud Completada"); // Enviamos un string al cliente
                            } else { // El archivo no existe
                                dos = new DataOutputStream(socket.getOutputStream()); // Creamos un stream de salida al cliente
                                dos.writeUTF("Solicitud Fallida"); // Enviamos un string al cliente
                            }
                        } else { // la pericion es invalida
                            System.out.println("Peticion invalida: " + socket); // Avisamos de la peticion
                            dos = new DataOutputStream(socket.getOutputStream()); // Creamos un stream de salida al cliente
                            dos.writeUTF("Invalida"); // Enviamos un string al cliente
                        }
                        dis = new DataInputStream(socket.getInputStream());
                        Entrada = dis.readUTF(); // Creamos un stream de entrada al cliente (Esperamos por otro comando)
                        Entrada_parse = Entrada.split(" "); // Parseamos la entrada
                    }
                } catch (Exception err) {
                    //to_log = dateformat.format(Calendar.getInstance().getTime()) + "\t" + socket + "\t" + "Error\n";
                    //log.write(to_log.getBytes());
                } finally {
                    try {socket.close();} catch (IOException e) {}
                    to_log = dateformat.format(Calendar.getInstance().getTime()) + "\t" + socket + "\t" + "Cerrado\n";
                    log.write(to_log.getBytes());
                }
            } catch (IOException e) {}
            log.close();
        } catch (IOException e) {}
    }

}