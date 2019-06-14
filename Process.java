/*
Importes necesarios para el funcionamiento de la clase
*/
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Process implements Runnable {
    // Nuestras principales variables de comunicacion es el socket del cliente
    private Socket socket;
    // Constructor de la clase
      public Process(Socket socket) {
        this.socket = socket;
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
                                    System.out.println(file.getName());
                                    Contador++;
                                }
                            }
                            System.out.println("Terminamos de revisar archivos"+ Contador);
                            dos = new DataOutputStream(socket.getOutputStream()); // Creamos un stream de salida al cliente
                            dos.writeInt(Contador); // Enviamos la cantidad de archivos al cliente
                            System.out.println("enviamos contador: "+Contador);
                            // Enviamos los nombres de los archivos al servidor uno a la vez
                            while (Contador > 0) {
                                dos = new DataOutputStream(socket.getOutputStream());
                                dos.writeUTF(ls_aux[Contador - 1]);
                                Contador--;
                            }
                        } else if (Entrada_parse[0].equals("get")) { // Si el verbo es un get
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
                            to_log = dateformat.format(Calendar.getInstance().getTime()) + "\t" + socket + "\t" + Entrada + "\n"; // Armamos el string para el log
                            log.write(to_log.getBytes()); // Escribimos el string en el archivo de log
                            System.out.println("Peticion de put: " + socket); // Avisamos de la peticion
                            dis = new DataInputStream(socket.getInputStream()); // Creamos un stream de entrada al cliente
                            System.out.println("dis creado");
                            cantidad = dis.readInt(); // Esperamos un entero por parte del cliente (la cantidad de bytes del archivo a recibir)
                            System.out.println("tamanio obtenido");
                            if (cantidad > 0) { // Si el entero es mayor que uno el archivo existe
                                archivo_s = new File("./Server/" + Entrada_parse[1]); // Creamos el archivo
                                System.out.println("archivo_s creado");
                                os = new FileOutputStream(archivo_s); // Creamos el stream para escribir en el archivo
                                Array = new byte[cantidad]; // Creamos un array de bytes que contendra el archivo
                                dis = new DataInputStream(socket.getInputStream()); // Esperamos por el archivo
                                dis.readFully(Array); // Escribimos el archivo en el array
                                System.out.println("Archivo escrito");
                                os.write(Array); // Pasamos el array al archivo en la parte del servidor
                                System.out.println("Archivo Obtenido"); // Notificamos que hemos recibido el archivo
                                os.close(); // Cerramos el archivo (el stream a el)
                                System.out.println("Archivo cerrado");
                                dos = new DataOutputStream(socket.getOutputStream()); // Creamos un stream de salida al cliente
                                dos.writeUTF("Solicitud Completada"); // Enviamos un string al cliente
                            } else { // El archivo no existe
                                System.out.println("Archivo Inexistente"); // Notificamos la no existencia del archivo
                            }
                        } else if (Entrada_parse[0].equals("del")) { // Si el verbo es del
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
                    System.out.println("Excepcion de algun tipo");
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