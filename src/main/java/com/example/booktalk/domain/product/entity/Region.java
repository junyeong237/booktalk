package com.example.booktalk.domain.product.entity;


import lombok.Getter;

@Getter
public enum Region {

    SEOUL("서울"),
    BUSAN("부산"),
    INCHEON("인천"),
    DAEGU("대구"),
    GWANGJU("광주"),
    DAEJEON("대전"),
    ULSAN("울산"),
    SUWON("수원"),
    CHEONGJU("청주"),
    JEJU("제주");


    private final String name;

    Region(String name) {
        this.name = name;
    }

}
