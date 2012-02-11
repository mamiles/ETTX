
package com.cisco.ettx.admin.common;

public class AttrResult {
	private String iterator = null;
	private Object output = null;
	private int pending = 0;

	public AttrResult(String ltoken) {
		iterator = ltoken;
	}

	public AttrResult() {
	}

	public void setOutput(Object loutput) {
		output = loutput;
	}

	public void setPending(int lcount) {
		pending = lcount;
	}

	public int getPending() {
		return pending;
	}

	public Object getOutput() {
		return output;
	}

	public String getIterator() {
		return iterator;
	}

	public void setIterator(String ltoken) {
		iterator = ltoken;
	}
}
