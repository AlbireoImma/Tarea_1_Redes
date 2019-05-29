import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStream;
import java.util.Base64;
import java.util.Base64.Decoder;

public class Decode64 {
    private String archivo;

    Decode64(String nombre){
        this.archivo = nombre;
    }

    public String decodificar(){
        byte[] datos;
        Decoder decoder = Base64.getDecoder();
        String linea;
        BufferedReader lector;
        String nombre_arch = archivo.split("[.]cifrado")[0]+".final";
        try{
            OutputStream salida = new FileOutputStream(nombre_arch);
            lector = new BufferedReader(new FileReader(archivo));
            linea = lector.readLine();
            while (linea != null) {
                datos = decoder.decode(linea);
                salida.write(datos, 0, datos.length);
                linea = lector.readLine();
            }
            lector.close();
            salida.close();
            return nombre_arch;
        } catch(Exception e){
            System.out.println("Error en Decode64");
            return "Error";
        }
    }
}