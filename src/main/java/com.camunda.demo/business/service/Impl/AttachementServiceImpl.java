package com.camunda.demo.business.service.Impl;

import com.camunda.demo.base.repository.Pager;
import com.camunda.demo.base.utils.FileUtils;
import com.camunda.demo.business.form.FileForm;
import com.camunda.demo.business.service.AttachementService;
import com.camunda.demo.dataInterface.constant.IEntityStatus;
import com.camunda.demo.dataInterface.dao.AttachementDao;
import com.camunda.demo.dataInterface.entity.Attachement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AttachementServiceImpl implements AttachementService {
    @Autowired
    private AttachementDao attachementDao;

    @Override
    public List<Attachement> getAll() {
        return attachementDao.findAll();
    }

    @Override
    public List<Attachement> listFile(FileForm fileForm) {
        return attachementDao.findByExample(fileForm.buildEntity(),
                Pager.of(fileForm.getPage(), fileForm.getPageSize())).getContent();
    }

    @Override
    public Attachement uploadFile(MultipartFile file) throws IOException {
        String path = FileUtils.upload(file);
        LocalDateTime now = LocalDateTime.now();
        Attachement attachement = new Attachement();
        attachement.setName(FileUtils.getUploadFileName(file));
        attachement.setExt(FileUtils.getUploadFileExt(file));
        attachement.setStatus(IEntityStatus.ACTIVE);
        attachement.setSize(file.getSize());
        attachement.setPath(path);
        attachement.setUploadTime(now);
        attachement.setUpdateTime(now);
        return attachementDao.save(attachement);
    }

    @Override
    public void downloadFile(Long id, HttpServletResponse response) {
        Attachement attachement = attachementDao.findByKey(id);
        FileUtils.download(attachement.getPath(), response);
    }

    @Override
    public void previewFile(Long id, HttpServletResponse response) {
        Attachement attachement = attachementDao.findByKey(id);
        FileUtils.preview(attachement.getPath(), response);
    }
}
