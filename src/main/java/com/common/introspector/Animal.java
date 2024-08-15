package com.common.introspector;

import java.util.List;

/**
 * description: com.common.introspector
 * company: 北京海量数据有限公司
 * create by: zhaosong 2024/3/8
 * version: 1.0
 */
public class Animal {

    private int id;

    private Integer id2;

    private String name;

    private Address address;

    private List<Address> addressList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getId2() {
        return id2;
    }

    public void setId2(Integer id2) {
        this.id2 = id2;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Address> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<Address> addressList) {
        this.addressList = addressList;
    }
}
