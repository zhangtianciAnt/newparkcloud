package com.nt.dao_Pfans.PFANS1000;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
/**
 * pl成本转接表
 */
public class Pltab extends BaseModel {

	private static final long serialVersionUID = 1L;

    private String pj;//项目

    private String outst;//外部受託

    private String tax;//税金

    private String inst;//内部受託

    private String intotal;//収入合計

    private String emhours;//社員工数

    private String outhours;//外注工数

    private String peocost;//人件費

    private String outcost;//外注費

    private String inwetuo;//内部委託

    private String researchcost;//研究材料費

    private String departmentcom;//部門共通按分（A是共通费用）

    private String yuanqincost;//原動費

    private String travalcost;//旅費

    private String concost;//消耗费

    private String callcost;//通信費

    private String brandcost;//ブランド料

    private String rent;//家賃

    private String other;//その他

    private String departmenttotal;//部門共通費用合計

    private String allocation;//配賦費用

    private String costtotal;//支出合計

    private String process;//仕掛品処理

    private String marginal;//限界利益

    private String Operating;//営業利益


}
