package exceptions;

import javax.xml.ws.WebFault;

@SuppressWarnings("serial")
@WebFault
public class ArgumentException extends Exception {
	
	protected String info;

	public ArgumentException(String msg, Throwable causa) {		
		super(msg, causa);
		info = msg;
	}
	
	public ArgumentException(String msg) {
		super(msg);
		info = msg;
	}
	
	public String  getFaultInfo() {
		return info;
	}
}
