/**
 * Copyright 2012 Eric P. Vialle
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

	private String classUnderTestName;

	private String methodUnderTestName;

	private transient Throwable throwable;

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

	/**
	 * @return the throwable
	 */
	public Throwable getThrowable() {
		return throwable;
	}

	/**
	 * @param throwable the throwable to set
	 */
	public void setThrowable(Throwable throwable) {
		this.throwable = throwable;
	}


	/**
	 * @return the methodUnderTestName
	 */
	public String getMethodUnderTestName() {
		return methodUnderTestName;
	}

	/**
	 * @param methodUnderTestName the methodUnderTestName to set
	 */
	public void setMethodUnderTestName(String methodUnderTestName) {
		this.methodUnderTestName = methodUnderTestName;
	}

	/**
	 * @return the classUnderTestName
	 */
	public String getClassUnderTestName() {
		return classUnderTestName;
	}

	/**
	 * @param classUnderTestName the classUnderTestName to set
	 */
	public void setClassUnderTestName(String classUnderTestName) {
		this.classUnderTestName = classUnderTestName;
	}



}
