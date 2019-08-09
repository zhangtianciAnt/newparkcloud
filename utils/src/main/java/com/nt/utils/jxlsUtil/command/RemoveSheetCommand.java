package com.nt.utils.jxlsUtil.command;

import org.jxls.area.Area;
import org.jxls.command.AbstractCommand;
import org.jxls.command.Command;
import org.jxls.common.CellRef;
import org.jxls.common.Context;
import org.jxls.common.Size;

/**
 * <p>保持单元格样式</p>
 * jx:keep(lastCell="结束单元格")<br>
 * 如果生成的单元格样式被改变了，可以试用该指定keep恢复
 * @Author lnk
 * @Date 2018/1/23
 */
public class RemoveSheetCommand extends AbstractCommand {

    private Area area;
    private String sheetName;

    public RemoveSheetCommand(String sheetName) {
        this.sheetName = sheetName;
    }

    @Override
    public Command addArea(Area area) {
        return super.addArea(area);
    }

    @Override
    public String getName() {
        return "removeSheet";
    }

    @Override
    public Size applyAt(CellRef cellRef, Context context) {
        if( area == null ){
            throw new IllegalArgumentException("No area is defined for keep command");
        }
        System.out.println(sheetName);
        getTransformer().deleteSheet(sheetName);
        return area.getSize();
    }
}
