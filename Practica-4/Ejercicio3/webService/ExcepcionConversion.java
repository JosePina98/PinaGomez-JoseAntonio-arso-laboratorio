package webService;

import javax.xml.ws.WebFault;

@WebFault
public class ExcepcionConversion extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String info;
	
	public ExcepcionConversion(String info) {
		super(info);
		this.info = info;
	}

	public String getFaultInfo() {
		return info;
	}
}
