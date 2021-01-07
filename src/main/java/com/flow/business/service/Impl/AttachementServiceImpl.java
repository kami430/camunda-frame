package com.flow.business.service.Impl;

import com.flow.base.repository.Pager;
import com.flow.base.utils.FileUtils;
import com.flow.business.form.FileForm;
import com.flow.business.service.AttachementService;
import com.flow.dataInterface.constant.IEntityStatus;
import com.flow.dataInterface.dao.AttachementDao;
import com.flow.dataInterface.entity.Attachement;
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
                Pager.of(fileForm.getPage(), fileForm.getPageSize()).pageable()).getContent();
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
