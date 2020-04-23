package com.sq.api_auto.copy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**Case用例工具类
 * @author 63503
 *
 */
public class CaseUtil {
	
	/**
	 * 保存所有的用例对象(共享数据)
	 */
	public static List<Case> cases = new ArrayList<Case>();
	static {
		//将所有数据解析封装到cases
		ExcelUitl2.load("src/test/resources/test.xlsx","用例",Case.class);
	}
	
	/**根据接口编号拿对应接口的测试数据
	 * @param apiId 指定接口编号
	 * @param cellNames  要获取的数据对应的列名
	 * @return
	 */
	public static Object [][] getCaseDatasByApiId(String apiId,String[] cellNames) {
		Class<Case> clazz = Case.class;
		//保存指定接口编号的case对象的集合
		ArrayList<Case> csList = new ArrayList<Case>();
		//通过循环找出指定接口编号（apiId）有几组数据
		for (Case cs : cases) {
			if (cs.getApiId().equals(apiId)) {
				csList.add(cs);
			}
		}
		//第一个参数为有几组数据，第二个参数为，1组参数有几个数据
		Object[][] datas = new Object[csList.size()][cellNames.length];
		for (int i = 0; i < csList.size(); i++) {
			Case cs = csList.get(i);
			for (int j = 0; j < cellNames.length; j++) {
				//要反射的方法名
				String methodName = "get"+cellNames[j];
				//获取到反射的方法对象
				Method method;
				try {
					method = clazz.getMethod(methodName);
					String value = (String) method.invoke(cs);
					datas[i][j] = value;
				} catch (Exception e) {
					e.printStackTrace();
				} 
				
				
			}
		}
		return datas;
	}
	
	
	public static void main(String[] args) {
		String[] cellNames = {"ApiId","Data","Expected"};
		Object[][] datas = getCaseDatasByApiId("1", cellNames);
		for (Object[] objects : datas) {
			System.out.println("-----");
			for (Object object : objects) {
				System.out.println(object);
			}
		}
	}
}
