package com.spring.integration.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class Demo implements Serializable {

    public String id;
    public String name;
    public String age;

}
