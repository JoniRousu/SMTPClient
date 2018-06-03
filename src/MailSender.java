import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;


public class MailSender {
	
	private Socket sock;
	private InetAddress SMTPServer;
	private InputStream ServerIncoming;
	private OutputStream ClientOut;
	private BufferedReader SIReader;
	
	int WELCOME = 220;
	int OK = 250;
	int DATA = 354;
	
	MailSender(InetAddress SMTPServerAddress){
		SMTPServer = SMTPServerAddress;
	}
	
	private boolean connect(String User){
		try {
			sock = new Socket(SMTPServer, 25);
			ServerIncoming = sock.getInputStream();
			ClientOut = sock.getOutputStream();
			SIReader = new BufferedReader(new InputStreamReader(ServerIncoming));
			String messageIn = SIReader.readLine();
			if(MessageCode(messageIn) == WELCOME){
				ClientOut.write(("HELO " + User + "\n").getBytes());
				messageIn = SIReader.readLine();
				if(MessageCode(messageIn) == OK){
					return true;
				}
				else{
					System.out.print(messageIn);
					return false;
				}
			}	
			else{
				System.out.print(messageIn);
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
	
	public String sendMail(Email mail){
		String MessageID = new String();
		if (connect(mail.SenderName)){
			try {
				ClientOut.write(("MAIL FROM: <" + mail.getSenderAddress() + ">\n").getBytes());
				String message = SIReader.readLine();
				if (MessageCode(message) == OK){
					ClientOut.write(("RCPT TO: <" + mail.ReceiverAddress + ">\n").getBytes());
					message = SIReader.readLine();
					if(MessageCode(message) == OK){
						ClientOut.write(("DATA\n").getBytes());
						message = SIReader.readLine();
						if (MessageCode(message) == DATA){
							ClientOut.write((mail.getHeader() + "\n").getBytes());
							ClientOut.write(mail.getMessage().getBytes());
							ClientOut.write("\n.\n".getBytes());
						}
						else{
							System.out.print("Problem with data reception!\n" + message + "\n");
							return "Error!";
						}
						message = SIReader.readLine();
						if (MessageCode(message) == OK){
							MessageID = message.substring(10, message.length());
						}
						else{
							System.out.print("Problem with message!\n" + message + "\n");
							return "Error!";
						}
					}
					else{
						System.out.print("Problem with receiver address!\n" + message + "\n");
						return "Error!";
					}
				}
				else{
					System.out.print("Problem with sender address!\n" + message + "\n");
					return "Error!";
				}
			} catch (IOException e) {
				e.printStackTrace();
				return "Error!";
			}
			disconnect();
			return MessageID;
		}
		else{
			System.out.print("Could not connect to the SMTP Server!\n");
			disconnect();
			return "Error!";
		}
	}
	
	private int MessageCode(String message){
		return Integer.parseInt(message.substring(0,3));
	}
}
