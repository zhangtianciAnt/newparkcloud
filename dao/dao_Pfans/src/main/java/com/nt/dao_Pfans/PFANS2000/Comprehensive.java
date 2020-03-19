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

  @Column(name = "YEARSWAGES")
  private String yearswages;

  @Column(name = "MONTH1WAGES")
  private String month1Wages;

  @Column(name = "MONTH1APPRECIATION")
  private String month1Appreciation;

  @Column(name = "MONTH2WAGES")
  private String month2Wages;

  @Column(name = "MONTH2APPRECIATION")
  private String month2Appreciation;

  @Column(name = "MONTH3WAGES")
  private String month3Wages;

  @Column(name = "MONTH3APPRECIATION")
  private String month3Appreciation;

  @Column(name = "MONTH4WAGES")
  private String month4Wages;

  @Column(name = "MONTH4APPRECIATION")
  private String month4Appreciation;

  @Column(name = "MONTH5WAGES")
  private String month5Wages;

  @Column(name = "MONTH5APPRECIATION")
  private String month5Appreciation;

  @Column(name = "MONTH6WAGES")
  private String month6Wages;

  @Column(name = "MONTH6APPRECIATION")
  private String month6Appreciation;

  @Column(name = "MONTH7WAGES")
  private String month7Wages;

  @Column(name = "MONTH7APPRECIATION")
  private String month7Appreciation;

  @Column(name = "MONTH8WAGES")
  private String month8Wages;

  @Column(name = "MONTH8APPRECIATION")
  private String month8Appreciation;

  @Column(name = "MONTH9WAGES")
  private String month9Wages;

  @Column(name = "MONTH9APPRECIATION")
  private String month9Appreciation;

  @Column(name = "MONTH10WAGES")
  private String month10Wages;

  @Column(name = "MONTH10APPRECIATION")
  private String month10Appreciation;

  @Column(name = "MONTH11WAGES")
  private String month11Wages;

  @Column(name = "MONTH11APPRECIATION")
  private String month11Appreciation;

  @Column(name = "MONTH12WAGES")
  private String month12Wages;

  @Column(name = "MONTH12APPRECIATION")
  private String month12Appreciation;

  @Column(name = "TOTALWAGES")
  private String totalwages;

  @Column(name = "YEARSTOTAL12")
  private String yearstotal12;

  @Column(name = "YEARSTOTAL")
  private String yearstotal;

  @Column(name = "ROWINDEX")
  private String rowindex;

  @Column(name = "JOBNUMBER")
  private String jobnumber;
}
