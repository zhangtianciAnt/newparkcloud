package com.nt.dao_Pfans.PFANS2000;

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
@Table(name = "comprehensive")
public class Comprehensive extends BaseModel {

  @Id
  @Column(name = "COMPREHENSIVE_ID")
  private String comprehensiveId;

  @Column(name = "GIVING_ID")
  private String givingId;

  @Column(name = "USER_ID")
  private String userId;

  private String yearswages;
  private String month1Wages;
  private String month1Appreciation;
  private String month2Wages;
  private String month2Appreciation;
  private String month3Wages;
  private String month3Appreciation;
  private String month4Wages;
  private String month4Appreciation;
  private String month5Wages;
  private String month5Appreciation;
  private String month6Wages;
  private String month6Appreciation;
  private String month7Wages;
  private String month7Appreciation;
  private String month8Wages;
  private String month8Appreciation;
  private String month9Wages;
  private String month9Appreciation;
  private String month10Wages;
  private String month10Appreciation;
  private String month11Wages;
  private String month11Appreciation;
  private String month12Wages;
  private String month12Appreciation;
  private String totalwages;
  private String yearstotal12;
  private String yearstotal;
  private String rowindex;
  private String jobnumber;
}
