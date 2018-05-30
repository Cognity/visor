package de.uniulm.omi.cloudiator.visor.sensors.restUtil;

import java.io.Serializable;

/**
 * Created by Dimitris Charilas on 28/06/17.
 */
public class PerformanceResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private int requestsNumber;
	private int responsesNumber;
	private double avgResponseTime;
	private int dbCallsNumber;

	public int getRequestsNumber() {
		return requestsNumber;
	}

	public void setRequestsNumber(int requestsNumber) {
		this.requestsNumber = requestsNumber;
	}

	public int getResponsesNumber() {
		return responsesNumber;
	}

	public void setResponsesNumber(int responsesNumber) {
		this.responsesNumber = responsesNumber;
	}

	public int getDbCallsNumber() {
		return dbCallsNumber;
	}

	public void setDbCallsNumber(int dbCallsNumber) {
		this.dbCallsNumber = dbCallsNumber;
	}

	public double getAvgResponseTime() {
		return avgResponseTime;
	}

	public void setAvgResponseTime(double avgResponseTime) {
		this.avgResponseTime = avgResponseTime;
	}

	@Override
	public String toString() {
		return "PerformanceResponse [requestsNumber: " + this.requestsNumber +
				", responsesNumber: " + this.responsesNumber +
				", avgResponseTime: " + this.avgResponseTime +
				", dbCallsNumber: " + this.dbCallsNumber +"]";
	}
}
