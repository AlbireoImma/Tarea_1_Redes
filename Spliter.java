import java.io.RandomAccessFile;
import java.io.FileOutputStream;
import java.io.BufferedOutputStream;
import java.util.ArrayList;
import java.lang.Math;

public class Spliter {
	private RandomAccessFile file;
	private long tamanio;
	private long partes;
	private long largo;
	private long restante;
	private int buffer;
	private String nombre;

	public Spliter(String nombre){
		try{
		this.nombre = nombre;
		this.file = new RandomAccessFile(nombre,"r");
		this.tamanio = file.length();
		this.partes = (long)Math.ceil((float)file.length()/64000);
		this.largo = 64000;
		this.restante = tamanio % partes;
		this.buffer = 10 * 1024;
		} catch(Exception e){}
	};

	public ArrayList<String> Separar(){
		ArrayList<String> archivos = new ArrayList<>();
		String nombra_archivo;
		try{
			int subarchivo = 1;
			while(subarchivo <= partes){
				nombra_archivo = nombre + "." + subarchivo + ".split";
				archivos.add(nombra_archivo);
				BufferedOutputStream salida = new BufferedOutputStream(new FileOutputStream(nombra_archivo));
				if (largo > buffer) {
					long lecturas = largo/buffer;
					long restantes = largo % buffer;
					for (int i=0; i < lecturas ; i++) {
						Escribir(file, salida, buffer);
					}
					if (restantes > 0) {
						Escribir(file, salida, restantes);
					}
				} else {
					Escribir(file, salida, largo);
				}
				salida.close();
				subarchivo++;
			}
			if (restante > 0) {
				nombra_archivo = nombre + "." + subarchivo + ".split";
				archivos.add(nombra_archivo);
				BufferedOutputStream salida = new BufferedOutputStream(new FileOutputStream(nombra_archivo));
				Escribir(file, salida, restante);
				salida.close();
			}
			file.close();
			return archivos;
		} catch(Exception e){
			return archivos;
		}
	}

	public static void Escribir(RandomAccessFile file, BufferedOutputStream salida, long cantidad){
		try{
			byte[] buffer_escritura = new byte[(int) cantidad];
			int datos = file.read(buffer_escritura);
			if (datos != -1) {
				salida.write(buffer_escritura);
			}
		} catch(Exception e){}
	}

	public static void Unir(ArrayList<> archivos){		
		// TODO
	}

	public static void main(String[] args) {
		Spliter archivo = new Spliter("test.jpg");
		ArrayList<String> lista = archivo.Separar();
		System.out.println("Archivos: " + lista.size());
	}
}