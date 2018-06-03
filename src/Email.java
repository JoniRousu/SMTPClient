
public class Email {

	public String SenderName;
	public String SenderAddress;
	public String ReceiverName;
	public String ReceiverAddress;
	public String message;
	public String Subject;
	public String header;
	
	Email(){
		SenderAddress = "";
		SenderName = "";
		ReceiverAddress = "";
		ReceiverName = "";
		Subject = "";
		message = "";
	}
	public String getHeader(){
		String Header = "";
		Header += "From: " + SenderName + " <" + SenderAddress + ">\n";
		Header += "To: " + ReceiverName + " <" + ReceiverAddress + ">\n";
		Header += "Subject: " + Subject + "\n";
		
		return Header;
	}
}
