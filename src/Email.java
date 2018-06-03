import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
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
	private String attachmentType;
	private String attachmentName;
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
		attachmentType = "";
		attachmentName = "";
	}
	
	
	public String getSenderAddress() {
		return SenderAddress;
	}


	public void setSenderAddress(String senderAddress) {
		SenderAddress = senderAddress;
		boundary = Base64.getMimeEncoder().encodeToString(getSenderAddress().getBytes());
	}


	public void setMessage(String Message){
		message = Base64.getMimeEncoder(72, "\r\n".getBytes()).encodeToString(Message.getBytes());
		if (!attachment.isEmpty()){
			message += "\n\n--" + boundary + "\n";
			message += "Content Type: " + attachmentType + ";\n name =\"" + attachmentName + "\"\n";
			message += "Content-Transfer-Encoding: base64\n";
			message += "Content-Disposition: attachment;\n filename=\"" + attachmentName + "\"\n\n";
			message += attachment + "\n";
		}
		message += "--" + boundary + "--";
	}
	
	public String getMessage(){
		return message;
	}
	
	
	public String getAttachment() {
		return attachment;
	}


	public void setAttachment(String pathToAttachment) throws IOException, URISyntaxException {
		File Attachment = new File(pathToAttachment);
		attachmentType = Files.probeContentType(Paths.get(new URI("file:///" + pathToAttachment)));
		attachmentName = Attachment.getName();
		if (attachment.isEmpty()){
			attachment = Base64.getMimeEncoder(72, "\r\n".getBytes()).encodeToString(Files.readAllBytes(Paths.get(new URI("file:///" + pathToAttachment))));
		}
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
		Header += "Content-Type: multipart/mixed;\n boundary=\"" + boundary + "\"\n";
		Header += "\nThis is a multi-part message in MIME format.\n";
		Header += "--" + boundary + "\n";
		Header += "Content-Type: text/plain; charset=utf-8\n";
		Header += "Content-Transfer-Encoding: base64\n";
		
		return Header;
	}
}
