package com.sq.api_auto.copy;

import java.util.Map;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONObject;

/**接口测试统一处理类
 * @author 63503
 *
 */
public class BaseProcessor {
	@Test(dataProvider="datas")
	public void test(String caseId,String ApiId,String parameter,String Expected) {	
		//获取url
		String url = RestUtil.getUrlByApiId(ApiId);
		//获取type
		String request_type = RestUtil.getTypeBy(ApiId);
		//pom配置fastjson,解析parms的json格式的字符串
		Map<String, String> params = (Map<String, String>) JSONObject.parse(parameter);//强制转换
		System.out.println("params="+params);
		System.out.println("size"+params);
		//调用doService方法	完成接口调用，拿到响应报文
		String testResult = HttpUtil.doService(url, request_type, params);
		System.out.println("resul=【"+testResult+"】");
		//截取"retcode"返回值，用作断言
		testResult = testResult.substring(12, 13);
		//断言
		String result = AssertUtil.assertEquals(testResult,Expected);
		//保存回写数据对象
		WriteBackData writeBackData = new WriteBackData("用例", caseId, "TestResult", testResult,"Result",result);	
		ExcelUitl2.writeBackDatas.add(writeBackData);
		
		
		//直接写回写回返回结果，或者下面batchWriteBackDtas()方法，一次性写回
		//ExcelUitl2.writeBackData("src/test/resources/test.xlsx","用例",caseId,"TestResult",testResult);
	}
	//整个suit套件执行完毕以后，数据回写
	@AfterSuite
	public void batchWriteBackDtas() {
		ExcelUitl2.batchWriteBackDtas("src/test/resources/test.xlsx");
	}
}
