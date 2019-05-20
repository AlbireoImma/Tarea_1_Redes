// Dependencias necesarias para el funcionamiento de la clase
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.io.RandomAccessFile;
import java.io.File;

public class Client {
    public static void main(String[] args) throws IOException {
        String Entrada; // Variable utilizada para la entrada de comandos al servidor
        String[] Entrada_parse; // Variable utiliazda para almacenar la entrada en forma parseada
        int cantidad; // variable utilizada para almacenar cifras relevantes
        byte[] Array; // Array de bytes utilizado para el movimiento de archivos
        File archivo; // Variable del tipo File utilizada para verificar la existencia de archivos
        // Intentamos conectarnos al servidor en el puerto 59090
        try (Socket socket = new Socket("10.6.40.195", 59090)) {
            DataInputStream dis = new DataInputStream(socket.getInputStream()); // Creamos un stream de entrada de datos desde el servidor
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream()); // Creamos un stream de salida de datos hacia el servidor
            OutputStream os; // Variable usada para escribir a un archivo
            RandomAccessFile archivo_s; // Variable para enviar los archivos como array de bytes
            String msg = dis.readUTF(); // Esperamos respuesta del servidor
            System.out.println(msg); // Imprimimos la respuesta del servidor
            System.out.println("Ctrl+C para salir");
            Scanner scanner = new Scanner(System.in); // Obtenemos la entrada del cliente
            Scanner in = new Scanner(socket.getInputStream()); // Obtenemos la entrada desde el socket
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true); // Obtenemos la salida desde el socket
            while (scanner.hasNextLine()) { // Verificamos si el usuario escribio algo por consola
                Entrada = scanner.nextLine(); // Obtenemos la entrada
                Entrada_parse = Entrada.split(" "); // Parseamos la entrada
                if (Entrada_parse.length == 1) { // Verificamos el tipo de entrada
                    if (Entrada.equals("ls")) { // Verbo ls
                        System.out.println("Obteniendo Directorio...");
                        dos.writeUTF(Entrada); // Enviamos la entrada al servidor
                        dis = new DataInputStream(socket.getInputStream()); // Creamos un stream de entrada y lo esperamos
                        cantidad = dis.readInt(); // Leemos la respuesta del servidor (cantidad de archivos)
                        while (cantidad > 0) { // recibimos los nombres de archivos del servidor
                            dis = new DataInputStream(socket.getInputStream()); // Creamos un stream de entrada y lo esperamos
                            System.out.println(dis.readUTF()); // Imprimimos los nombres de los archivos
                            cantidad--; // Restamos a la cantidad faltante de archivos por uno
                        }
                        System.out.println("Directorio Obtenido..."); // Notificamos la obtencion del directorio
                        // System.out.println(dis.readUTF());
                    }
                } else {
                    if (Entrada_parse.length > 1) {
                        if (Entrada_parse[0].equals("get")) { // verbo get
                            System.out.println("Obteniendo Archivo...");
                            dos.writeUTF(Entrada); // Enviamos la entrada al servidor
                            dis = new DataInputStream(socket.getInputStream()); // Creamos un stream de entrada y lo esperamos
                            cantidad = dis.readInt(); // Leemos la respuesta del servidor
                            if (cantidad > 0) { // El archivo existe
                                archivo = new File("./Client/" + Entrada_parse[1]); // Creamos el archivo
                                os = new FileOutputStream(archivo); // Creamos el stream para escribir el archivo
                                Array = new byte[cantidad]; // Creamos el array para almacenar el archivo
                                dis = new DataInputStream(socket.getInputStream()); // Creamos un stream de entrada y lo esperamos
                                dis.readFully(Array); // Guardamos el archivo en el array
                                os.write(Array); // Escribimos el archivo
                                System.out.println("Archivo Obtenido");
                                os.close(); // Cerramos el archivo
                            } else { // El archivo no existe
                                System.out.println("Archivo Inexistente");
                            }

                        } else if (Entrada_parse[0].equals("put")) { // Verbo put
                            System.out.println("Subiendo Archivo...");
                            dos.writeUTF(Entrada); // Enviamos la entrada al servidor
                            archivo = new File("./Client/" + Entrada_parse[1]); // Abrimos el archivo a enviar
                            if (archivo.exists()) { // Verificamos la existencia del archivo
                                archivo_s = new RandomAccessFile("./Client/" + Entrada_parse[1], "r"); // Abrimos el archivo en modo lectura
                                cantidad = (int) archivo_s.length(); // Obtenemos el largo del archivo a enviar (en bytes)
                                dos = new DataOutputStream(socket.getOutputStream()); // Creamos un stream de salida
                                dos.writeInt(cantidad); // Enviamos la cantidad de bytes al servidor
                                Array = new byte[cantidad]; // Creamos el array que contendra el archivo
                                archivo_s.readFully(Array); // Leemos el archivo y lo alojamos en el array
                                dos = new DataOutputStream(socket.getOutputStream()); // Creamos un stream de salida
                                dos.write(Array); // Enviamos el array al servidor
                                dis = new DataInputStream(socket.getInputStream()); // Creamos un stream de entrada y lo esperamos
                                System.out.println(dis.readUTF()); // Leemos la entrada y la imprimimos
                                archivo_s.close(); // Cerramos el archivo
                            } else { // El archivo no existe
                                System.out.println("Archivo No Existe");
                                dos = new DataOutputStream(socket.getOutputStream()); // Creamos un stream de salida
                                dos.writeInt(0); // Enviamos un 0 a forma de no existencia al servidor
                            }
                        } else if (Entrada_parse[0].equals("del")) { // Verbo del
                            System.out.println("Eliminando Archivo...");
                            dos.writeUTF(Entrada); // Enviamos la entrada al servidor
                            dis = new DataInputStream(socket.getInputStream());
                            System.out.println(dis.readUTF());
                        } else { // Entrada invalida
                            System.out.println("Entrada Invalida Saliendo...");
                            dos.writeUTF("Invalida");
                            break;
                        }
                        // dos.writeUTF("Invalida");
                        // break;
                    }
                }
                // System.out.println(in.nextLine());
            }
        }
    }
}
