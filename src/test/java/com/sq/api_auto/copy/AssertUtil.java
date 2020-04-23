package com.sq.api_auto.copy;

import org.testng.Assert;

public class AssertUtil {

	/**自定义类库：断言比较实际测试结果跟期望值是否一样
	 * @param expected
	 * @param testResult
	 */
	public static String assertEquals(String testResult,String expected) {
		String result = "通过";
		try {
			Assert.assertEquals(testResult, expected);
		} catch (Throwable e) {
			//如果报错，返回实际测试结果
			result = "失败";
		}	
		return result;
	}

}
