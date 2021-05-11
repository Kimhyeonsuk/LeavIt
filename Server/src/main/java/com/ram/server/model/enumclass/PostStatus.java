package com.ram.server.model.enumclass;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PostStatus {
    SELLING(0,"판매","판매중"),
    TRADING(1,"거래중","거래가 신청되어 현재 거래중"),
    DONE(2,"거래완료","거래가 완료된 상태");
    private Integer id;
    private String title;
    private String description;
}
