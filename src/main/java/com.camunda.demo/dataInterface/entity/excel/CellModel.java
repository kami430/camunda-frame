package com.camunda.demo.dataInterface.entity.excel;

import lombok.Data;

@Data
public class CellModel {

    private Long id;

    private Object value;

    private Boolean isHeader;

    private Boolean isMerge;

    private Integer firstRowIndex;

    private Integer lastRowIndex;

    private Integer firstColumnIndex;

    private Integer lastColumnIndex;
}
