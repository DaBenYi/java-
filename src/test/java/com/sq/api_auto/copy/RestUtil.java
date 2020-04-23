package com.sq.api_auto.copy;

import java.util.ArrayList;
import java.util.List;

public class RestUtil {
	public static List<Rest> rests = new ArrayList<Rest>();
	static {
		ExcelUitl2.load("src/test/resources/test.xlsx","接口信息",Rest.class);
	}

	/**根据接口编号获取接口地址url
	 * @param apiId
	 * @return
	 */
	public static String getUrlByApiId(String apiId) {
		for (Rest rest : rests) {
			if (rest.getApiId().equals(apiId)) {
				return rest.getUrl();
			}
		}

		return "";
	}
	
	/**获取请求方法method
	 * @param apiId
	 * @return
	 */
	public static String getTypeBy(String apiId) {
		for (Rest rest : rests) {
			if (rest.getApiId().equals(apiId)) {
				return rest.getType();
			}
		}
		return "";
	}
	
	public static void main(String[] args) {
		for (Rest rest : rests) {
			System.out.println(rest);
		}
	}





}
