import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;


public class MailSender {
	
	private Socket sock = null;
	private InetAddress SMTPServer;
	private InputStream ServerIncoming;
	private OutputStream ClientOut;
	private BufferedReader SIReader;
	
	int WELCOME = 220;
	int OK = 250;
	
	MailSender(InetAddress SMTPServerAddress){
		SMTPServer = SMTPServerAddress;
		connect("Test");
		disconnect();
	}
	
	private boolean connect(String User){
		try {
			sock = new Socket(SMTPServer, 25);
			ServerIncoming = sock.getInputStream();
			ClientOut = sock.getOutputStream();
			SIReader = new BufferedReader(new InputStreamReader(ServerIncoming));
			String messageIn = SIReader.readLine();
			System.out.print(messageIn);
			if(MessageCode(messageIn) == WELCOME){
				ClientOut.write(("HELO " + User + "\n").getBytes());
				messageIn = SIReader.readLine();
				System.out.print(messageIn);
				if(MessageCode(messageIn) == OK){
					return true;
				}
				else{
					return false;
				}
			}	
			else{
				return false;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private void disconnect(){
		try {
			sock.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		sock = null;
		ServerIncoming = null;
		ClientOut = null;
		SIReader = null;
	}
	
	public boolean sendMail(Email mail){
		if (connect(mail.SenderName)){
			try {
				ClientOut.write(("MAIL FROM: <" + mail.SenderAddress + ">\n").getBytes());
				if (MessageCode(SIReader.readLine()) == OK){
					ClientOut.write(("RCPT TO: <" + mail.ReceiverAddress + ">\n").getBytes());
					if(MessageCode(SIReader.readLine()) == OK){
						String MessageToSend = prepareMessage(mail.message);
					}
					else{
						return false;
					}
				}
				else{
					return false;
				}
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}
		else{
			System.out.print("Could not connect to the SMTP Server!");
			disconnect();
			return false;
		}
	}
	
	private int MessageCode(String message){
		return Integer.parseInt(message.substring(0,3));
	}
	
	private String prepareMessage(String message){
		String preparedString = new String();
		///TODO: Make the message conforming to RFC spec
		return preparedString;
	}
}
