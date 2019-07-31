import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.List;

public class Decode64 {
    private String archivo;

    Decode64(String nombre){
        this.archivo = nombre;
    }

    public String decodificar(){
        byte[] datos;
        Decoder decoder = Base64.getMimeDecoder();
        //String linea;
        BufferedReader lector;
        String nombre_arch = this.archivo.split("[.]cifrado")[0]+".final";
        try{
            OutputStream salida = new FileOutputStream(nombre_arch);
            //lector = new BufferedReader(new FileReader(this.archivo));
            //linea = lector.readLine();
            List<String> lines = Files.readAllLines(Paths.get(this.archivo), StandardCharsets.ISO_8859_1);
            for(String line : lines) {
            	datos = decoder.decode(line.getBytes());
            	salida.write(datos, 0, datos.length);            	
            }
            /*
            while (linea != null) {
                linea = lector.readLine();
            }
            */
            //lector.close();
            salida.close();
            return nombre_arch;
        } catch(Exception e){
            System.out.println("Error en Decode64");
            return "Error";
        }
    }
}