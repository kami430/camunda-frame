package com.camunda.demo.business.form;

import com.camunda.demo.base.repository.BaseForm;
import com.camunda.demo.dataInterface.entity.Attachement;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileForm implements BaseForm<Attachement> {
    String name;

    String ext;

    Integer page;

    Integer pageSize;

    @Override
    public Attachement buildEntity() {
        Attachement attachement = new Attachement();
        copyProperties(this, attachement);
        return attachement;
    }
}
