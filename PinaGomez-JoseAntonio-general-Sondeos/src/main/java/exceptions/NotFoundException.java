package exceptions;

import javax.xml.ws.WebFault;

@SuppressWarnings("serial")
@WebFault
public class NotFoundException extends Exception {
	
	public String info; 
	
	public NotFoundException(String msg, Throwable causa) {		
		super(msg, causa);
		this.info = msg;
	}
	
	public NotFoundException(String msg) {
		super(msg);	
		this.info = msg;
	}
	
	public String getFaultInfo() {
		return info;
	}
}
