package com.sq.api_auto.copy;

/**封装回写数据对象*/
public class WriteBackData {
	private String sheetName;
	private String caseId;
	private String cellName;
	private String testResult;
	private String resultName;
	private String result;
	
	public WriteBackData(String sheetName, String caseId, String cellName, String testResult, String resultName,String result) {
		super();
		this.sheetName = sheetName;
		this.caseId = caseId;
		this.cellName = cellName;
		this.testResult = testResult;
		this.result = result;
		this.resultName = resultName;
	}

	public String getResultName() {
		return resultName;
	}

	public void setResultName(String resultName) {
		this.resultName = resultName;
	}
	
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public WriteBackData() {
		super();
	}
	
	public String getSheetName() {
		return sheetName;
	}
	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}
	public String getCaseId() {
		return caseId;
	}
	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}
	public String getCellName() {
		return cellName;
	}
	public void setCellName(String cellName) {
		this.cellName = cellName;
	}
	public String getTestResult() {
		return testResult;
	}
	public void setTestResult(String testResult) {
		this.testResult = testResult;
	}
	public String toString() {
		return "caseId="+caseId+"sheetName="+sheetName+"cellName="+cellName+"testResult="+testResult+"result="+result+"testResult="+testResult;
		
	}
}
