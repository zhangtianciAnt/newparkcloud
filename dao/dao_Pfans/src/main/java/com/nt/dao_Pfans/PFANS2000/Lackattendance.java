package com.nt.dao_Pfans.PFANS2000;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The Lackattendance
 *
 * @author skaixx
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "lackattendance")
public class Lackattendance extends BaseModel {

  @Id
  @Column(name = "LACKATTENDANCE_ID")
  private String lackattendance_id; //主键

  @Column(name = "GIVING_ID")
  private String giving_id; //所属给与计算

  @Column(name = "USER_ID")
  private String user_id; //名字

  @Column(name = "LASTDILIGENCETRY")
  private String lastdiligencetry; //上月欠勤（H）

  @Column(name = "LASTSHORTDEFICIENCYTRY")
  private String lastshortdeficiencytry; //上月短病欠(H)

  @Column(name = "LASTCHRONICDEFICIENCYTRY")
  private String lastchronicdeficiencytry; //上月长病欠(H)

  @Column(name = "LASTDILIGENCEFORMAL")
  private String lastdiligenceformal; //本月欠勤（H）

  @Column(name = "LASTSHORTDEFICIENCYFORMAL")
  private String lastshortdeficiencyformal; //本月短病欠(H)

  @Column(name = "LASTCHRONICDEFICIENCYFORMAL")
  private String lastchronicdeficiencyformal; //本月长病欠(H)

  @Column(name = "LASTDILIGENCE")
  private String lastdiligence; //

  @Column(name = "LASTSHORTDEFICIENCY")
  private String lastshortdeficiency; //

  @Column(name = "LASTCHRONICDEFICIENCY")
  private String lastchronicdeficiency; //

  @Column(name = "LASTTOTAL")
  private String lasttotal; //本月合計（元）

  @Column(name = "GIVE")
  private String give; //給料

  @Column(name = "REMARKS")
  private String remarks; //備考

  @Column(name = "ROWINDEX")
  private Integer rowindex; //顺序

  @Column(name = "JOBNUMBER")
  private String jobnumber; //工号

  @Column(name = "THISDILIGENCETRY")
  private String thisdiligencetry; //上月欠勤（H）

  @Column(name = "THISSHORTDEFICIENCYTRY")
  private String thisshortdeficiencytry; //上月短病欠(H)

  @Column(name = "THISCHRONICDEFICIENCYTRY")
  private String thischronicdeficiencytry; //上月长病欠(H)

  @Column(name = "THISDILIGENCEFORMAL")
  private String thisdiligenceformal; //本月欠勤（H）

  @Column(name = "THISSHORTDEFICIENCYFORMAL")
  private String thisshortdeficiencyformal; //本月短病欠(H)

  @Column(name = "THISCHRONICDEFICIENCYFORMAL")
  private String thischronicdeficiencyformal; //本月长病欠(H)

  @Column(name = "THISDILIGENCE")
  private String thisdiligence; //

  @Column(name = "THISSHORTDEFICIENCY")
  private String thisshortdeficiency; //

  @Column(name = "THISCHRONICDEFICIENCY")
  private String thischronicdeficiency; //

  @Column(name = "THISTOTAL")
  private String thistotal; //本月合計（元）

}
