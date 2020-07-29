package com.nt.service_AOCHUAN.AOCHUAN3000.Impl.xls;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SheetInfo {
	/**
	 * sheet命名
	 */
	private String sheetName;

	/**
	 * 列别名
	 */
	private List<String> colNames = new ArrayList<String>();

	/**
	 * 数据别名
	 */
	private String dataAlias;

	private int start = 0;//替代行数

	private int oldEnd = 0;//结束行数

	private int sheetNum = -1;//sheet数,默认-1,无sheet页

	private AtomicInteger cursor = new AtomicInteger();//游标位置


	public String getSheetName() {
		return sheetName;
	}


	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}


	public List<String> getColNames() {
		return colNames;
	}


	public void setColNames(List<String> colNames) {
		this.colNames = colNames;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
		cursor.set(start);
	}

	public int getOldEnd() {
		return oldEnd;
	}


	public void setOldEnd(int oldEnd) {
		this.oldEnd = oldEnd;
	}
	public String getDataAlias() {
		return dataAlias;
	}

	public void setDataAlias(String dataAlias) {
		this.dataAlias = dataAlias;
	}

	public int getSheetNum() {
		return sheetNum;
	}

	public void setSheetNum(int sheetNum) {
		this.sheetNum = sheetNum;
	}

	/**
	 * 得到游标，并将游标下移一位
	 * @author yyh 2020年4月14日 下午2:29:37
	 * @return
	 * @since 2.1
	 */
	public int getCursorAndMove() {
		return cursor.getAndIncrement();
	}
}
