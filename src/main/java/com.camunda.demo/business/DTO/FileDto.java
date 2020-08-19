package com.camunda.demo.business.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileDto {
    String name;

    String ext;

    Integer page;

    Integer pageSize;
}
