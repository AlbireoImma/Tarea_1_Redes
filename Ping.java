import java.net.InetAddress;

public class Ping {
	private String direccion;
	public Ping(String dir){
		this.direccion = dir;
	}
	public boolean Respuesta(){
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
	public static void main(String[] args){
		Ping test = new Ping("10.6.40.195"); // dist55.inf.santiago.usm.cl
		test.Respuesta();
	}
}