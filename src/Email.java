
public class Email {

	public String SenderName;
	public String SenderAddress;
	public String ReceiverName;
	public String ReceiverAddress;
	public String message;
	public MailHeader header = new MailHeader();
	Email(){
		SenderAddress = "";
		SenderName = "";
		ReceiverAddress = "";
		ReceiverName = "";
		header.Subject = "";
	}
}
