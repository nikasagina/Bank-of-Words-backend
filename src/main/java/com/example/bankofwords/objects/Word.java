package com.example.bankofwords.objects;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Word {
    private long id;
    private String word;
    private String definition;
    private long tableId;
}
