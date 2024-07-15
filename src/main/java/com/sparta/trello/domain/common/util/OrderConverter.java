package com.sparta.trello.domain.common.util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Convert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Convert
public class OrderConverter implements AttributeConverter<List<Long>, String> {
    // 컬럼으로 바꿔서 넣기
    @Override
    public String convertToDatabaseColumn(List<Long> cardOrder) {
        return cardOrder == null ? "" : String.join(",", cardOrder.stream().map(String::valueOf).collect(Collectors.toList()));
    }

    // 추출
    @Override
    public List<Long> convertToEntityAttribute(String dbData) {
        return dbData == null || dbData.isEmpty() ? new ArrayList<>() : Arrays.stream(dbData.split(",")).map(Long::valueOf).collect(Collectors.toList());
    }

}
