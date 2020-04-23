package com.sq.api_auto.copy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;


public class ExcelUitl2 {
	//装caseid以及它对应的行索引
	public static Map<String, Integer> caseIdRownumMapping = new HashMap<String, Integer>();
	//装cellname以及它对应的列索引
	public static Map<String, Integer> cellNameCellMapping = new HashMap<String, Integer>();
	//装回写excel数据
	public static List<WriteBackData> writeBackDatas = new ArrayList<WriteBackData>();
	static {
		loadRownumAndCellnumMapping("src/test/resources/test.xlsx","用例");
	}
	
	/**解析指定excel表单的数据，封装为对象
	 * @param <T>
	 * @param excelPath excel文件的相对路径
	 * @param sheetName excel表单名
	 */
	public static <T> void load(String excelPath, String sheetName,Class<T> clazz) {	
		try {
			//创建workBook对象
			Workbook workbook = WorkbookFactory.create(new File(excelPath));
			//创建sheet对象
			Sheet sheet =  workbook.getSheet(sheetName);
			//获取首行标题
			Row titleRow = sheet.getRow(0);
			//获取最后一列的列号
			int lastCellNum = titleRow.getLastCellNum();
			//创建数组，保存数据
			String[] fields = new String[lastCellNum];
			//循环处理每一列,取出每一列里面的字段名，保存到数组fields
		for (int i = 0; i < lastCellNum; i++) {
			//根据列索引获取每一列，后面一个参数策略，防止空值
			Cell cell = titleRow.getCell(i,MissingCellPolicy.CREATE_NULL_AS_BLANK);
			//设置列的类型为字符串
			cell.setCellType(CellType.STRING);
			//获取列的值
			String title = cell.getStringCellValue();
			//截取到括号出现的地方,注意“（”excel的中英文，
			title = title.substring(0, title.indexOf("("));
			//将反射名保存到数组
			fields[i] = title;		
		}
		//得到最后一行行号
		int lastRowIndex = sheet.getLastRowNum();
		//循环处理每一个数据行
		for (int i = 1; i <= lastRowIndex; i++) {
			//创建每一行对象
			Object obj = clazz.newInstance();
			//拿到一个数据行
			Row dataRow = sheet.getRow(i);
			//如果数据行拿到空的，跳过
			if(dataRow==null||isEmptyRow(dataRow)) {
				continue;
			}
			//拿到数据行上的每一列，将数据封装到对象cs对象
			for (int j = 0; j < lastCellNum; j++) {
				Cell cell = dataRow.getCell(j,MissingCellPolicy.CREATE_NULL_AS_BLANK);
				//类型为字符串
				cell.setCellType(CellType.STRING);
				String value = cell.getStringCellValue();
				//用来反射方法名，拼接“set”
				String methodName = "set"+fields[j];
				//拿到method方法对象
				Method method = clazz.getMethod(methodName, String.class);
				//完成反射调用，将value封装到cs
				method.invoke(obj, value);
			}
			//判断对象obj类型
			if (obj instanceof Case) {
				Case cs = (Case) obj;
				CaseUtil.cases.add(cs);
			}else if (obj instanceof Rest) {
				Rest rest = (Rest) obj;
				RestUtil.rests.add(rest);
			}
		}
		} catch (Exception e) {
			e.printStackTrace();
		} 	
	}
	
	/**判断行数据是否为空
	 * @param dataRow
	 * @return
	 */
	private static boolean isEmptyRow(Row dataRow) {
		int lastCellNum = dataRow.getLastCellNum();
		for (int i = 0; i < lastCellNum; i++) {
			Cell cell = dataRow.getCell(i,MissingCellPolicy.CREATE_NULL_AS_BLANK);
			cell.setCellType(CellType.STRING);
			String value = cell.getStringCellValue();
			//只要有1列的值为空，或者去掉两边空格，为空
			if (value!=null||value.trim().length()==0) {
				return false;
			}
		}
		return true;
	}
	

	/**回写接口响应报文
	 * @param excelPath excel文件路径
	 * @param sheetName 
	 * @param caseId
	 * @param cellName
	 * @param testResult
	 */
	public static void writeBackData(String excelPath,String sheetName,String caseId, String cellName, String testResult) {	
		InputStream inputStream = null;
		OutputStream stream = null;
		try {
			//创建流
			inputStream = new FileInputStream(new File(excelPath));
			Workbook workbook = WorkbookFactory.create(inputStream);
			//创建表单对象
			Sheet sheet =  workbook.getSheet(sheetName);
			int rownum = caseIdRownumMapping.get(caseId);
			Row row = sheet.getRow(rownum);
			int cellnum = cellNameCellMapping.get(cellName);
			Cell cell = row.getCell(cellnum,MissingCellPolicy.CREATE_NULL_AS_BLANK);
			cell.setCellType(CellType.STRING);
			cell.setCellValue(testResult);
			stream = new FileOutputStream(new File(excelPath));
			workbook.write(stream);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stream!=null) {
					stream.close();
				}
				if (inputStream!=null) {
					inputStream.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
	
		}


		
	}

	/**获取caseid以及它对应的行索引
	 * 获取cellname以及它对应的列索引
	 * @param excelPath
	 * @param sheetName
	 */
	private static void loadRownumAndCellnumMapping(String excelPath, String sheetName) {
		InputStream inputStream = null;
		try {
			//创建流
			inputStream = new FileInputStream(new File(excelPath));
			//创建workbench对象
			Workbook workbook = WorkbookFactory.create(inputStream);
			//创建表单对象
			Sheet sheet =  workbook.getSheet(sheetName);
			//获取第一行（标题行）
			Row titleRow = sheet.getRow(0);
			if (titleRow!=null&&!isEmptyRow(titleRow)) {
				int lastCellnum = titleRow.getLastCellNum();
				//循环处理标题行的每一列
				for (int i = 0; i < lastCellnum; i++) {
					Cell cell = titleRow.getCell(i,MissingCellPolicy.CREATE_NULL_AS_BLANK);
					cell.setCellType(CellType.STRING);
					String title = cell.getStringCellValue();
					title = title.substring(0, title.indexOf("("));
					//获取列索引
					int cellnum = cell.getAddress().getColumn();
					cellNameCellMapping.put(title, cellnum);
				}
			}
			//从第二行开始，获取所有的数据行
			int lastRownum = sheet.getLastRowNum();
			//循环拿到每个数据行
			for (int i = 1; i <=lastRownum; i++) {
				Row dataRow = sheet.getRow(i);
				//行上面的第一列数据caseid
				Cell firstCellOfRow = dataRow.getCell(0,MissingCellPolicy.CREATE_NULL_AS_BLANK);
				firstCellOfRow.setCellType(CellType.STRING);
				String caseId = firstCellOfRow.getStringCellValue();
				//获取行索引
				int rownum = dataRow.getRowNum();
				caseIdRownumMapping.put(caseId, rownum);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if (inputStream!=null) {
				try {
					inputStream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		
	}
	
	/**批量回写数据的方法
	 * @param string
	 */
	public static void batchWriteBackDtas(String excelPath) {
		InputStream inputStream = null;
		OutputStream stream = null;
		try {
			inputStream =new FileInputStream(new File(excelPath));
			Workbook workbook =WorkbookFactory.create(inputStream);
			for (WriteBackData writeBackData : writeBackDatas) {
				//获取sheetname
				String sheetname = writeBackData.getSheetName();
				Sheet sheet = workbook.getSheet(sheetname);
				String caseId =  writeBackData.getCaseId();
				int rownum = caseIdRownumMapping.get(caseId);
				//获取用例行号
				Row row = sheet.getRow(rownum);		
				//获取"TestResult"索引
				String cellName = writeBackData.getCellName();	
				//获取"TestResult"列号
				int cellnum = cellNameCellMapping.get(cellName);
				Cell cell = row.getCell(cellnum);
				cell.setCellType(CellType.STRING);
				//获取测试结果
				String testResult = writeBackData.getTestResult();
				//写回用例
				cell.setCellValue(testResult);
			
				//下面代码块写回result结果"通过"或“失败”
				String resultName = writeBackData.getResultName();
				int resultNum = cellNameCellMapping.get(resultName);
				Cell cell1 = row.getCell(resultNum);
				cell1.setCellType(CellType.STRING);
				String result = writeBackData.getResult();	
				cell1.setCellValue(result);
			}
			stream = new FileOutputStream(new File(excelPath));
			workbook.write(stream);
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (inputStream!=null) {
					inputStream.close();
				}
				if (stream!=null) {
					stream.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
	
		}
		
	}
	

	/**测试datas()
	 * */
	public static void main(String[] args) {
		batchWriteBackDtas("src/test/resources/test.xlsx");

		}


		
	}






