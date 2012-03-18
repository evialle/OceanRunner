/**
 * 
 */
package it.freshminutes.oceanrunner.modules.builtin.statistics;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Eric Vialle
 * 
 */
public class StatisticsResult implements Serializable {

	public static enum StatusTestResult {

		SUCCESS(0), IGNORE(1), FAILED(2), ASSUMPTION_FAILED(3);

		int value_;
		
		/** Constructor. */
		StatusTestResult(int value) {
			this.value_ = value;
		}

	}

	/** Like any good javabean... */
	private static final long serialVersionUID = -4454510292257647912L;

	private Date runDate;

	private StatusTestResult status;

	private String comments;

	/**
	 * @return the runDate
	 */
	public Date getRunDate() {
		return runDate;
	}

	/**
	 * @param runDate the runDate to set
	 */
	public void setRunDate(Date runDate) {
		this.runDate = runDate;
	}

	/**
	 * @return the status
	 */
	public StatusTestResult getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(StatusTestResult status) {
		this.status = status;
	}

	/**
	 * @return the comments
	 */
	public String getComments() {
		return comments;
	}

	/**
	 * @param comments the comments to set
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}

}
