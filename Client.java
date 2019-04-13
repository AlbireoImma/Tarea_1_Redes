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
        String Entrada;
        String[] Entrada_parse;
        int cantidad;
        byte[] Array;
        File archivo;

        try (Socket socket = new Socket("localhost", 59090)) {
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            OutputStream os;
            RandomAccessFile archivo_s;
            String msg = dis.readUTF();
            System.out.println(msg);
            System.out.println("Ctrl+C para salir");
            Scanner scanner = new Scanner(System.in);
            Scanner in = new Scanner(socket.getInputStream());
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            while (scanner.hasNextLine()) {
                Entrada = scanner.nextLine();
                Entrada_parse = Entrada.split(" ");
                if (Entrada_parse.length == 1) {
                    if (Entrada.equals("ls")) {
                        System.out.println("Obteniendo Directorio...");
                        dos.writeUTF(Entrada);
                        dis = new DataInputStream(socket.getInputStream());
                        cantidad = dis.readInt();
                        while (cantidad > 0) {
                            dis = new DataInputStream(socket.getInputStream());
                            System.out.println(dis.readUTF());
                            cantidad--;
                        }
                        System.out.println(dis.readUTF());
                    }
                } else {
                    if (Entrada_parse.length > 1) {
                        if (Entrada_parse[0].equals("get")) {
                            System.out.println("Obteniendo Archivo...");
                            dos.writeUTF(Entrada);
                            dis = new DataInputStream(socket.getInputStream());
                            cantidad = dis.readInt();
                            archivo = new File("./Client/" + Entrada_parse[1]);
                            os = new FileOutputStream(archivo);
                            Array = new byte[cantidad];
                            dis = new DataInputStream(socket.getInputStream());
                            dis.readFully(Array);
                            os.write(Array);
                            System.out.println("Archivo Obtenido");
                            os.close();

                        } else if (Entrada_parse[0].equals("put")) {
                            System.out.println("Subiendo Archivo...");
                            dos.writeUTF(Entrada);
                            archivo_s = new RandomAccessFile("./Client/" + Entrada_parse[1], "r");
                            cantidad = (int) archivo_s.length();
                            dos = new DataOutputStream(socket.getOutputStream());
                            dos.writeInt(cantidad);
                            Array = new byte[cantidad];
                            archivo_s.readFully(Array);
                            dos = new DataOutputStream(socket.getOutputStream());
                            dos.write(Array);
                            dis = new DataInputStream(socket.getInputStream());
                            System.out.println(dis.readUTF());
                        } else if (Entrada_parse[0].equals("del")) {
                            System.out.println("Eliminando Archivo...");
                            dos.writeUTF(Entrada);
                            dis = new DataInputStream(socket.getInputStream());
                            System.out.println(dis.readUTF());
                        } else {
                            System.out.println("Entrada Invalida Saliendo...");
                            dos.writeUTF("Invalida");
                            break;
                        }
                    } else {
                        System.out.println("Entrada Invalida Saliendo...");
                        dos.writeUTF("Invalida");
                        break;
                    }
                }
                // System.out.println(in.nextLine());
            }
        }
    }
}