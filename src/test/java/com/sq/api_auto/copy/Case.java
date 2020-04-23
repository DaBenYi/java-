package com.sq.api_auto.copy;

/**保存Case的信息
 * @author 63503
 *
 */
public class Case {
	private String caseId;
	private String apiId;
	private String data;
	private String title;
	private String module;
	private String expected;
	private String testResult;
	private String result;
	
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getExpected() {
		return expected;
	}

	public void setExpected(String expected) {
		this.expected = expected;
	}

	public String getTestResult() {
		return testResult;
	}

	public void setTestResult(String testResult) {
		this.testResult = testResult;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getCaseId() {
		return caseId;
	}

	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}

	public String getApiId() {
		return apiId;
	}

	public void setApiId(String apiId) {
		this.apiId = apiId;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String toString() {
		return "caseId="+caseId+",apiId="+apiId+",data="+data+",title="+title+",module="+module+","
				+ "expected="+expected+",testResult"+testResult+",result"+result;
	}
	
}
