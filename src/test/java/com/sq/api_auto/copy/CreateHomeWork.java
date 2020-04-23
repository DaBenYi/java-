package com.sq.api_auto.copy;

import org.testng.annotations.DataProvider;

public class CreateHomeWork extends BaseProcessor {
	@DataProvider
	public Object[] [] datas(){
		String[] cellNames = {"CaseId","ApiId","Data","Expected"};
		Object[][] datas = CaseUtil.getCaseDatasByApiId("2", cellNames);
		return datas;
	}
}
