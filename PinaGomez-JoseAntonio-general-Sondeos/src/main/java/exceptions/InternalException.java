package exceptions;

@SuppressWarnings("serial")
public class InternalException extends Exception {
	
	protected String info;

	public InternalException(String msg, Throwable causa) {		
		super(msg, causa);
		info = msg;
	}
	
	public InternalException(String msg) {
		super(msg);
		info = msg;
	}
	
	public String getFaultInfo() {
		return info;
	}
}
