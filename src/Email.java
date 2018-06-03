import java.util.Base64;


public class Email {

	public String SenderName;
	private String SenderAddress;
	public String ReceiverName;
	public String ReceiverAddress;
	private String message;
	public String Subject;
	public String header;
	private String attachment;
	private String boundary;
	
	Email(){
		setSenderAddress("");
		SenderName = "";
		ReceiverAddress = "";
		ReceiverName = "";
		Subject = "";
		message = "";
		boundary = "";
		header = "";
		attachment = "";
	}
	
	
	public String getSenderAddress() {
		return SenderAddress;
	}


	public void setSenderAddress(String senderAddress) {
		SenderAddress = senderAddress;
		boundary = Base64.getMimeEncoder().encodeToString(getSenderAddress().getBytes());
	}


	public void setMessage(String Message){
		message = Base64.getMimeEncoder().encodeToString(Message.getBytes());
		if (attachment.isEmpty()){
			message += "\n--" + boundary + "--";
		}
	}
	
	public String getMessage(){
		return message;
	}
	
	
	public String getAttachment() {
		return attachment;
	}


	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	public String getHeader(){
		String Header = "";
		Header += "From: " + SenderName + " <" + getSenderAddress() + ">\n";
		Header += "To: " + ReceiverName + " <" + ReceiverAddress + ">\n";
		if (!Subject.isEmpty()){
			Header += "Subject: " + Subject + "\n";
		}
		//MIME
		Header += "Mime-Version: 1.0\n";
		Header += "Content-Type: multipart/mixed; boundary=\"" + boundary + "\"\n";
		Header += "Content-Disposition: inline\n";
		Header += "--" + boundary + "\n";
		Header += "Content-Type: text/plain; charset=utf-8\n";
		Header += "Content-Disposition: inline\n";
		Header += "Content-Transfer-Encoding: base64\n";
		
		return Header;
	}
}
