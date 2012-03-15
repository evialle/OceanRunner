package it.freshminutes.oceanrunner.exceptions;

/**
 * Exception to be used by OceanModule.
 * 
 * @author Eric Vialle
 * 
 */
public class OceanModuleException extends Exception {

	private static final long serialVersionUID = 465178879784554689L;

	/**
	 * Constructor.
	 * 
	 * @param t
	 *            root throwable
	 */
	public OceanModuleException(Throwable t) {
		super(t);
	}

	/**
	 * Constructor.
	 * 
	 * @param s
	 *            root explanation of the exception.
	 */
	public OceanModuleException(String s) {
		super(s);
	}

}
