import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.File;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.io.RandomAccessFile;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

public class Process implements Runnable {

    private Socket socket;
    InputStream in_datos = null;
    OutputStream out_datos = null;

    public Process(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        DateFormat dateformat = new SimpleDateFormat("dd-mm-yyyy hh:mm:ss");
        DataOutputStream dos;
        DataInputStream dis;
        FileOutputStream log;
        try {
            log = new FileOutputStream("./log.txt", true);
            File folder = new File("./Server");
            File[] listOfFiles = folder.listFiles();
            File archivo_s;
            RandomAccessFile archivo;
            int Contador;
            int cantidad;
            byte[] Array;
            OutputStream os;
            String[] ls_aux = new String[listOfFiles.length];
            String to_log;
            to_log = dateformat.format(Calendar.getInstance().getTime()) + "\t" + socket + "\t" + "Conectado\n";
            try {
                log.write(to_log.getBytes());
                dos = new DataOutputStream(socket.getOutputStream());
                dos.writeUTF("Bienvenido al Servidor");
                try {
                    dis = new DataInputStream(socket.getInputStream());
                    String Entrada = dis.readUTF();
                    String[] Entrada_parse = Entrada.split(" ");
                    while (Entrada.length() > 0) {
                        if (Entrada.equals("ls")) {
                            to_log = dateformat.format(Calendar.getInstance().getTime()) + "\t" + socket + "\t" + Entrada
                            + "\n";
                            log.write(to_log.getBytes());
                            folder = new File("./Server");
                            listOfFiles = folder.listFiles();
                            Contador = 0;
                            System.out.println("Peticion de ls: " + socket);
                            for (File file : listOfFiles) {
                                if (file.isFile()) {
                                    ls_aux[Contador] = file.getName();
                                    Contador++;
                                }
                            }
                            dos = new DataOutputStream(socket.getOutputStream());
                            dos.writeInt(Contador);
                            while (Contador > 0) {
                                dos = new DataOutputStream(socket.getOutputStream());
                                dos.writeUTF(ls_aux[Contador - 1]);
                                Contador--;
                            }
                            dos = new DataOutputStream(socket.getOutputStream());
                            dos.writeUTF("Solicitud Completada");
                        } else if (Entrada_parse[0].equals("get")) {
                            to_log = dateformat.format(Calendar.getInstance().getTime()) + "\t" + socket + "\t" + Entrada
                            + "\n";
                            log.write(to_log.getBytes());
                            System.out.println("Peticion de get: " + socket);
                            archivo = new RandomAccessFile("./Server/" + Entrada_parse[1], "r");
                            Contador = (int) archivo.length();
                            dos = new DataOutputStream(socket.getOutputStream());
                            dos.writeInt(Contador);
                            Array = new byte[Contador];
                            archivo.readFully(Array);
                            dos = new DataOutputStream(socket.getOutputStream());
                            dos.write(Array);
                            System.out.println("Archivo Enviado");
                        } else if (Entrada_parse[0].equals("put")) {
                            to_log = dateformat.format(Calendar.getInstance().getTime()) + "\t" + socket + "\t" + Entrada
                            + "\n";
                            log.write(to_log.getBytes());
                            System.out.println("Peticion de put: " + socket);
                            dis = new DataInputStream(socket.getInputStream());
                            cantidad = dis.readInt();
                            archivo_s = new File("./Server/" + Entrada_parse[1]);
                            os = new FileOutputStream(archivo_s);
                            Array = new byte[cantidad];
                            dis = new DataInputStream(socket.getInputStream());
                            dis.readFully(Array);
                            os.write(Array);
                            System.out.println("Archivo Obtenido");
                            os.close();
                            dos = new DataOutputStream(socket.getOutputStream());
                            dos.writeUTF("Solicitud Completada");
                        } else if (Entrada_parse[0].equals("del")) {
                            to_log = dateformat.format(Calendar.getInstance().getTime()) + "\t" + socket + "\t" + Entrada
                            + "\n";
                            log.write(to_log.getBytes());
                            System.out.println("Peticion de del: " + socket);
                            archivo_s = new File("./Server/" + Entrada_parse[1]);
                            if (archivo_s.delete()) {
                                dos = new DataOutputStream(socket.getOutputStream());
                                dos.writeUTF("Solicitud Completada");
                            } else {
                                dos = new DataOutputStream(socket.getOutputStream());
                                dos.writeUTF("Solicitud Fallida");
                            }
                        } else {
                            System.out.println("Peticion invalida: " + socket);
                            dos = new DataOutputStream(socket.getOutputStream());
                            dos.writeUTF("Invalida");
                            break;
                        }
                        dis = new DataInputStream(socket.getInputStream());
                        Entrada = dis.readUTF();
                        Entrada_parse = Entrada.split(" ");
                    }
                } catch (Exception err) {
                    to_log = dateformat.format(Calendar.getInstance().getTime()) + "\t" + socket + "\t" + "Error\n";
                    log.write(to_log.getBytes());
                } finally {
                    try {
                        socket.close();
                    } catch (IOException e) {
                    }
                    to_log = dateformat.format(Calendar.getInstance().getTime()) + "\t" + socket + "\t" + "Cerrado\n";
                    log.write(to_log.getBytes());
                }
            } catch (IOException e) {
            }
            log.close();
        } catch (IOException e) {
        }
    }
}