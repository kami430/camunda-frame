package com.flow.business.controller;

import com.flow.business.form.FileForm;
import com.flow.business.service.AttachementService;
import com.flow.dataInterface.entity.Attachement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private AttachementService attachementService;

    @PostMapping("/uploadFile")
    public Attachement upload(@RequestParam("file") MultipartFile file) throws IOException {
        return attachementService.uploadFile(file);
    }

    @GetMapping("/downloadFile")
    public void downloadFile(@RequestParam("id") Long id, HttpServletResponse response) {
        attachementService.downloadFile(id, response);
    }

    @GetMapping("/previewFile")
    public void previewFile(@RequestParam("id") Long id, HttpServletResponse response) {
        attachementService.previewFile(id, response);
    }

    @GetMapping("/allFile")
    public List<Attachement> allFile() {
        return attachementService.getAll();
    }

    @GetMapping("/listFile")
    public List<Attachement> listFile(FileForm fileForm){
        return  attachementService.listFile(fileForm);
    }
}
