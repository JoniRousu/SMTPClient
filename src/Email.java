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
	private String[] attachment;
	private String[] attachmentType;
	private String[] attachmentName;
	private String boundary;
	private int NrOfAttachments;
	
	Email(){
		setSenderAddress("");
		SenderName = "";
		ReceiverAddress = "";
		ReceiverName = "";
		Subject = "";
		message = "";
		boundary = "";
		header = "";
		attachment = new String[10];
		attachmentType = new String[10];
		attachmentName = new String[10];
		for (int i = 0; i < 10; i++){
			attachment[i] = "";
			attachmentType[i] = "";
			attachmentName[i] = "";
		}
		NrOfAttachments = 0;
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
		if(!attachment[0].isEmpty()){
			for (int i = 0; i< NrOfAttachments; i++){
				message += "\n\n--" + boundary + "\n";
				message += "Content-Type: " + attachmentType[i] + ";\n name =\"" + attachmentName[i] + "\"\n";
				message += "Content-Transfer-Encoding: base64\n";
				message += "Content-Disposition: attachment;\n filename=\"" + attachmentName[i] + "\"\n\n";
				message += attachment[i];
			}
		}
		message += "\n--" + boundary + "--";
	}
	
	public String getMessage(){
		return message;
	}
	
	public void setAttachment(String pathToAttachment) throws IOException, URISyntaxException {
		File Attachment = new File(pathToAttachment);
		if (!Attachment.exists()){
			System.out.println("The file you want to attach was not found!");
			System.exit(1);
		}
		attachmentType[NrOfAttachments] = Files.probeContentType(Paths.get(new URI("file:///" + pathToAttachment)));
		if (attachmentType[NrOfAttachments] == null){
			attachmentType[NrOfAttachments] = "application/octet-stream";
		}
		attachmentName[NrOfAttachments] = Attachment.getName();
		attachment[NrOfAttachments] = Base64.getMimeEncoder(72, "\r\n".getBytes()).encodeToString(Files.readAllBytes(Paths.get(new URI("file:///" + pathToAttachment))));
		NrOfAttachments++;
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
