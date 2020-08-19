package com.camunda.demo.dataInterface.entity.excel;

import lombok.Data;

import java.util.List;

@Data
public class SheetModel {

    private Long id;

    private String name;

    private List<CellModel> cellModels;

}
