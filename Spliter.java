import java.io.RandomAccessFile;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import java.lang.Math;
import java.io.InputStream;
import java.io.OutputStream;

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
		this.restante = tamanio - (partes*64000);
		this.buffer = 1 * 1024;
		} catch(Exception e){}
	};

	public ArrayList<String> Separar(){
		ArrayList<String> archivos = new ArrayList<String>();
		String nombra_archivo;
		System.out.println("Tamaño: " + tamanio);
		System.out.println("Partes: " + partes);
		System.out.println("Restante: " + restante);
		try{
			int subarchivo = 1;
			while(subarchivo <= partes){
				nombra_archivo = nombre + "." + subarchivo + ".split";
				archivos.add(nombra_archivo);
				BufferedOutputStream salida = new BufferedOutputStream(new FileOutputStream(nombra_archivo));
				if (largo > buffer) {
					long lecturas = largo/buffer;
					long restantes = largo - (buffer * lecturas);
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
			System.out.println("Archivos generados");
			for (String archivo : archivos) {
				System.out.println(archivo);
			}
			return archivos;
		} catch(Exception e){
			System.out.println("Error en la separación de los archivos");
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

	public static void Unir(ArrayList<String> archivos){
		try{
			//String expresion = (String)archivos.get(0) + "test";
			//expresion = expresion.split(".")[0] + "." + expresion.split(".")[1];
			System.out.println("Uniendo...");
			InputStream entrada = null;
			OutputStream salida = null;
			System.out.println("Cantidad a unir: " + archivos.size());
			File archivo = new File("expresion.jpg");
			byte[] dato = new byte[1024];
			int linea;
			salida = new FileOutputStream(archivo);
			for (String expresion : archivos) {
				System.out.println("Leyendo: " + expresion);
				File subarchivo = new File(expresion);
				entrada = new FileInputStream(subarchivo);
				while ((linea = entrada.read(dato)) > 0) {
					salida.write(dato, 0,linea);
				}
			}
			entrada.close();
			salida.close();
		} catch(Exception e){
			System.out.println("Excepción generada al unir");
		}
	}

	public static void main(String[] args) {
		Spliter archivo = new Spliter("test.jpg");
		ArrayList<String> lista = archivo.Separar();
		System.out.println("Pulsar para unir...");
		try{System.in.read();}catch(Exception e){}
		Unir(lista);
		System.out.println("Archivos: " + lista.size());

	}
}