import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;


public class MailSender {
	
	private Socket sock;
	
	MailSender(InetAddress SMTPServer){
		try {
			setSocket(new Socket(SMTPServer, 25));
			sock = getSocket();		
			InputStream ServerIncoming = getSocket().getInputStream();
			OutputStream ServerOut = getSocket().getOutputStream();
			BufferedReader SIReader = new BufferedReader( new InputStreamReader(ServerIncoming));
			//TODO
			// - EHLO / HELO
			// Send mail
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void sendMail(){
		
	}
	private Socket getSocket() {
		return sock;
	}
	private void setSocket(Socket sock) {
		this.sock = sock;
	}
}
