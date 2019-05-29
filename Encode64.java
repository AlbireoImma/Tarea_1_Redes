import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class Encode64 {
    private String archivo;

    Encode64(String nombre){
        this.archivo = nombre;
    }

    public String codificar(){
        Base64.Encoder encoder = Base64.getMimeEncoder();
        Path original = Paths.get(archivo);
        Path objetivo = Paths.get(archivo + ".cifrado");
        try {
            OutputStream salida = Files.newOutputStream(objetivo);
            Files.copy(original, encoder.wrap(salida));
            return archivo + ".cifrado";
        } catch (Exception e) {
            return "Error";
        }
    }
}