package com.camunda.demo.business.service;

import com.camunda.demo.business.DTO.FileDto;
import com.camunda.demo.dataInterface.entity.Attachement;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface AttachementService {

    List<Attachement> getAll();

    List<Attachement> listFile(FileDto fileDto);

    Attachement uploadFile(MultipartFile file) throws IOException;

    void downloadFile(Long id, HttpServletResponse response);

    void previewFile(Long id, HttpServletResponse response);
}