package it.freshminutes.oceanrunner.exceptions;

/**
 * 
 * @author Eric Vialle
 * 
 */
public class NoOceanModuleException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 705471103092704419L;

	public NoOceanModuleException() {
		super();
	}

	public NoOceanModuleException(String str) {
		super(str + "is not an OceanModule");
	}


}
