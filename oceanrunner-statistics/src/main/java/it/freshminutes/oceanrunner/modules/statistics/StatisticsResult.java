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
package it.freshminutes.oceanrunner.modules.statistics;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author Eric Vialle
 * 
 */
@Entity
@Table(name = "oceanrunner_statisticsresult")
public class StatisticsResult implements Serializable {

	/**
	 * JUnit status.
	 * 
	 * @author Eric Vialle
	 */
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

	/** Last run date with that status. */
	@Column(name = "runDate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date runDate;


	@Column(name = "status")
	private StatusTestResult status;

	@Column(name = "comments")
	private String comments;

	@Column(name = "classundertestName")
	private String classUnderTestName;

	@Column(name = "methodundertestName")
	private String methodUnderTestName;

	@Column(name = "environement")
	private String environement;

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

	/**
	 * @return the environement
	 */
	public String getEnvironement() {
		return environement;
	}

	/**
	 * @param environement the environement to set
	 */
	public void setEnvironement(String environement) {
		this.environement = environement;
	}

}
