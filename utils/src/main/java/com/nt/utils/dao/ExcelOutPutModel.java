package com.nt.utils.dao;

import lombok.Data;

import java.util.List;

@Data
public class ExcelOutPutModel {

    public List<?> data;

    public String fileName;
}
