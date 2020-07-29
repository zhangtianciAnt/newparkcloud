package com.nt.service_AOCHUAN.AOCHUAN3000.Impl.xls;

import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * 基于XLSTransformer上的开发,可用于百万级数据的导出
 * 注:只支持excel2007及其以上的excel
 * copy from https://www.codenong.com/cs105843314/
 * modify by zy
 */
public class MyXLSTransformer extends XLSTransformer {
	private static final int DEFAULTMEMORYSIZE = 100;
	private List<SheetInfo> sheetInfos = null;

	/**
	 * 导出数据逻辑处理
	 */
	public Workbook transformXLS(InputStream is, Map beanParams , int memoryNum) throws ParsePropertyException {
		Workbook workbook = null;
		try {
			//根据输入的excel模板,创建对于得workbook
			workbook = WorkbookFactory.create(is);
			if(workbook instanceof XSSFWorkbook) {
				//excel2007可导出百万级数据
				workbook = transManyformWorkbook(workbook, beanParams ,memoryNum , null);
			} else {
				//此分支适用于excel2003,预计只能达到4W的数据量
				transformWorkbook(workbook, beanParams);
			}
		} catch (IOException e) {
			log.error("deal excel fail!" , e);
		}
		return workbook;
	}

	/**
	 * 导出数据逻辑处理
	 */
	public Workbook transformXLS(InputStream is, Map beanParams) throws ParsePropertyException {
		return this.transformXLS(is, beanParams , DEFAULTMEMORYSIZE);
	}

	/**
	 * 多次导出一个excel
	 * @author yyh 2020年3月16日 下午6:56:47
	 * @param is
	 * @param beanParams
	 * @return
	 * @throws ParsePropertyException
	 * @throws InvalidFormatException
	 * @since 2.1
	 */
	public Workbook transformXLSByManyFQ(InputStream is, Map beanParams) throws ParsePropertyException, InvalidFormatException {
		return transformXLSByManyFQ(is , beanParams , null);
	}

	/**
	 * 多次导出一个excel
	 * @author yyh 2020年3月16日 下午6:56:47
	 * @param is
	 * @param beanParams
	 * @param sheetInfos
	 * @return
	 * @throws ParsePropertyException
	 * @throws InvalidFormatException
	 * @since 2.1
	 */
	public Workbook transformXLSByManyFQ(InputStream is, Map beanParams , List<SheetInfo> sheetInfos) throws ParsePropertyException, InvalidFormatException {
		Workbook workbook = null;
		try {
			workbook = WorkbookFactory.create(is);
			if(workbook instanceof XSSFWorkbook) {
				workbook = transManyformWorkbook(workbook, beanParams ,DEFAULTMEMORYSIZE , sheetInfos);
				return workbook;
			}
		} catch (IOException e) {
			log.error("deal excel fail!" , e);
		}
		return workbook;
	}

	/**
	 * 用于多次导出的下一次操作
	 * @param beanParams
	 * @param workbook
	 */
	public void next(Map beanParams , Workbook workbook) {
		dealWorkbook(workbook  , beanParams);
	}

	/**
	 * 多数据量处理代码
	 * @author yyh 2020年3月16日 下午4:23:48
	 * @param workbook excel信息
	 * @param beanParams 数据量
	 * @param memoryNum 读取到内存的数量
	 * @return
	 * @since 2.1
	 */
	private Workbook transManyformWorkbook(Workbook workbook, Map beanParams , int memoryNum , List<SheetInfo> sheetInfos) {
		if(sheetInfos == null || sheetInfos.size() == 0) {
			this.sheetInfos = dealWorkbookSheet(workbook , beanParams);
		} else {
			this.sheetInfos = sheetInfos;
		}
		workbook = new SXSSFWorkbook((XSSFWorkbook)workbook , memoryNum);
		dealWorkbook(workbook  , beanParams);
		return workbook;
	}

	/**
	 * 处理excel
	 * @author yyh 2020年3月16日 下午4:23:27
	 * @param workbook excel文件
	 * @param beanParams 数据信息
	 * @since 2.1
	 */
	private void dealWorkbook(Workbook workbook,  Map beanParams) {
		for(int i = 0;i < sheetInfos.size();i++) {
			SheetInfo sheetInfo = sheetInfos.get(i);
			if(sheetInfo == null || sheetInfo.getSheetNum() < 0) {
				log.debug("sheet info error,so export fail!");
				return;
			}
			Sheet sheet = workbook.getSheetAt(sheetInfo.getSheetNum());
			List<Object> datas = (List<Object>)beanParams.get(sheetInfos.get(i).getDataAlias());
			if(CollectionUtils.isEmpty(datas)) {
				log.debug("sheet data is empty, not export!");
				// motify by zy
				// fix empty data for sheet.
				continue;
			}
			for(Object o : datas) {
				Map data = null;
				// add by zy
				// fix VO object
				if ( o instanceof Map ) {
					data = (Map) o;
				} else {
					data = BeanMap.create(o);
				}
				Row row = sheet.createRow(sheetInfo.getCursorAndMove());
				for(int j = 0;j < sheetInfo.getColNames().size();j++) {
					Cell cell = row.createCell(j);
					cell.setCellValue(data.get(sheetInfo.getColNames().get(j)) == null ? "" : data.get(sheetInfo.getColNames().get(j)) + "");
				}
			}
		}
	}

	/**
	 * 处理excel的sheet
	 * @author yyh 2020年3月16日 下午4:22:29
	 * @param workbook excel文件
	 * @param beanParams 参数
	 * @return
	 * @since 2.1
	 */
	private List<SheetInfo> dealWorkbookSheet(Workbook workbook, Map beanParams) {
		List<SheetInfo> sheetInfos = new ArrayList<SheetInfo>();
		for(int i = 0 ; i < workbook.getNumberOfSheets();i++) {
			Sheet sheet = workbook.getSheetAt(i);
			SheetInfo sheetInfo = new SheetInfo();
			sheetInfo.setSheetName(sheet.getSheetName());
			dealOldRowData(sheetInfo , sheet , beanParams);
			if(sheetInfo.getStart() == sheetInfo.getOldEnd()) {
				continue;
			}
			sheetInfo.setSheetNum(i);
			for(int j = sheetInfo.getStart() ;j <= sheetInfo.getOldEnd();j++) {
				Row row = sheet.getRow(j);
				sheet.removeRow(row);
			}
			sheetInfos.add(sheetInfo);
		}
		return sheetInfos;
	}


	/**
	 * 处理row数据和sheet信息
	 * @author yyh 2020年3月16日 下午6:24:08
	 * @param sheetInfo 记录sheet信息
	 * @param sheet excel的sheet
	 * @param beanParams 数据
	 * @since 2.1
	 */
	private void dealOldRowData(SheetInfo sheetInfo, Sheet sheet , Map beanParams) {
		String replaceStr = "";
		for(int i = 1 ; i <= sheet.getLastRowNum();i++) {
			Row row = sheet.getRow(i);
			//判断第一例是否符合为开始列
			Cell cell = row.getCell(0);
			String str = "";

			if(cell != null && cell.getStringCellValue() != null) {
				str = cell.getStringCellValue();
				if(str.matches("<jx:forEach items=(.*)")) {
					sheetInfo.setStart(i);
					str = str.replace("\"", "");
                    str = str.replace("<jx:forEach items=${", "");
                    str = str.replace("}", "");
                    str = str.replace("var=", "");
                    str = str.replace(">", "");
                    StringTokenizer pas = new StringTokenizer(str," ");
                    int z = 0;
                    while (pas.hasMoreTokens()) {
                        String s = pas.nextToken();
                        if(z == 0) {
                            sheetInfo.setDataAlias(s);
                        }
                        z++;
                        if(z != 0) {
                            replaceStr = s;
                        }
                    }
                }
            }
            if(replaceStr == null || replaceStr.trim().equals("")) {
                continue;
            }
            if(str.matches("</jx:forEach>")) {
                sheetInfo.setOldEnd(i);
            }
            // fix by zy
			// full of replaceStr
            if(str.indexOf("${" + replaceStr + ".") >= 0) {
                List<String> colNames = new ArrayList<String>();
                for(int j = 0; j < row.getLastCellNum();j++) {
                    String cellValue = row.getCell(j) != null ? row.getCell(j).getStringCellValue() : null ;
                    if(cellValue != null && !cellValue.trim().equals("")) {
                        colNames.add(cellValue.replace("${" + replaceStr + ".", "").replace("}","").trim());
                    }

                }
                sheetInfo.setColNames(colNames);
            }
        }
    }
}
