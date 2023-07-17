package com.example.bankofwords.objects;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class Table {
    private final long tableId;
    private final long creatorId;
    @EqualsAndHashCode.Include
    private final String name;
}