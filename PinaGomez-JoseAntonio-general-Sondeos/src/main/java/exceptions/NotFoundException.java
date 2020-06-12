package exceptions;

@SuppressWarnings("serial")
public class NotFoundException extends Exception {
	
	protected String info;
	
	public NotFoundException(String msg, Throwable causa) {		
		super(msg, causa);
		info = msg;
	}
	
	public NotFoundException(String msg) {
		super(msg);	
		info = msg;
	}
	
	public String getFaultInfo() {
		return info;
	}
}
