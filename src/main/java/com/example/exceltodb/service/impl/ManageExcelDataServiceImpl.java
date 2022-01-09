package com.example.exceltodb.service.impl;


import com.example.exceltodb.entity.DataToSaveEntity;
import com.example.exceltodb.repository.DataRepository;
import com.example.exceltodb.service.IManageExcelDataService;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
public class ManageExcelDataServiceImpl implements IManageExcelDataService {

    @Value("${app.upload.file:${user.home}}")
    public String EXCEL_FILE_PATH;

    @Autowired
    DataRepository repo;

    Workbook workbook;

    public List<DataToSaveEntity> getExcelData(){

        List<String> list = new ArrayList<String>();

        // Create a DataFormatter to format and get each cell's value as String
        DataFormatter dataFormatter = new DataFormatter();

        // Create the Workbook
        try {
            workbook = WorkbookFactory.create(new File(EXCEL_FILE_PATH));
        } catch (EncryptedDocumentException | IOException e) {
            e.printStackTrace();
        }
        // Getting the Sheet at index zero
        Sheet sheet = workbook.getSheetAt(0);

        // Getting number of columns in the Sheet
        int columnsNumber = sheet.getRow(0).getLastCellNum();

        // Loop all rows and cells of the sheet
        for (Row row : sheet) {
            for (Cell cell : row) {
                String cellValue = dataFormatter.formatCellValue(cell);
                list.add(cellValue);
            }
        }

        // filling excel data and creating list as List<Invoice>
        List<DataToSaveEntity> dataList = createList(list, columnsNumber);

        // Closing the workbook
        try {
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dataList;
    }

    private List<DataToSaveEntity> createList(List<String> excelData, int columnNumber){

        ArrayList<DataToSaveEntity> dataList = new ArrayList<DataToSaveEntity>();

        int i = columnNumber;
        do {
            DataToSaveEntity data = new DataToSaveEntity();

            //read each column starting by 0
            data.setField1(excelData.get(i));
            data.setField2(excelData.get(i+2));
            data.setField3(excelData.get(i+3));
            data.setField4(excelData.get(i+4));

            dataList.add(data);
            i = i + (columnNumber);

        } while (i < excelData.size());
        return dataList;
    }

    @Override
    public int saveData(List<DataToSaveEntity> datacolums) {
        datacolums = repo.saveAll(datacolums);
        return datacolums.size();
    }

}
