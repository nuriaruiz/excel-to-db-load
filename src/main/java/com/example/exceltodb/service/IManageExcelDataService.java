package com.example.exceltodb.service;

import com.example.exceltodb.entity.DataToSaveEntity;

import java.text.ParseException;
import java.util.List;

public interface IManageExcelDataService {

    List<DataToSaveEntity> getExcelData() throws ParseException;

    int saveData(List<DataToSaveEntity> precios);

}
