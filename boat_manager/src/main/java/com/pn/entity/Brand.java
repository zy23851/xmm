package com.pn.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

//品牌
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Brand implements Serializable {

    private Integer brandId;
    private String brandName;
    private String brandLeter;
    private String brandDesc;

}
