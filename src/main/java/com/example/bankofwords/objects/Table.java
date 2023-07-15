package com.example.bankofwords.objects;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class Table {
    private final long tableId;
    private final long creatorId;
    private final String name;
}