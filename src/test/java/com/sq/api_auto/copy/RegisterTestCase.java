package com.sq.api_auto.copy;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONObject;


/**@version   配置testNg  配置xml-apis文件   配置testng.xml文件
 * */
public class RegisterTestCase extends BaseProcessor {	

	@DataProvider
	public Object[] [] datas(){
		String[] cellNames = {"CaseId","ApiId","Data","Expected"};
		Object[][] datas = CaseUtil.getCaseDatasByApiId("1", cellNames);
		return datas;
	}

}
