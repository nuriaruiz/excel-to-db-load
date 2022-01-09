package com.example.exceltodb.controller;

import com.example.exceltodb.entity.DataToSaveEntity;
import com.example.exceltodb.service.IManageExcelDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.util.List;

@Controller
public class UploadController {

    private final String UPLOAD_DIR = "src/upload/";


    @Autowired
    IManageExcelDataService excelservice;

    @GetMapping("/")
    public String homepage() {
        return "index";
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, RedirectAttributes attributes) throws ParseException {

        if (file.isEmpty()) {
            attributes.addFlashAttribute("message", "Please select a file to upload.");
            return "redirect:/";
        }

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            Path path = Paths.get(UPLOAD_DIR + "File.xlsx");
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<DataToSaveEntity> excelDataAsList = excelservice.getExcelData();
        int noOfRecords = excelservice.saveData(excelDataAsList);

        // return success response
        attributes.addFlashAttribute("message", "You successfully uploaded " + fileName + '!' + " Number of records: " + noOfRecords);

        return "redirect:/";
    }
}
