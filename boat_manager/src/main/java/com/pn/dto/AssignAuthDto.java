package com.pn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
//接收给角色分配权限请求的json数据的Dto类
public class AssignAuthDto {

    //接收角色Id
    private Integer roleId;
    //接收给角色分配的菜单权限的id
    private List<Integer> authIds;

}
