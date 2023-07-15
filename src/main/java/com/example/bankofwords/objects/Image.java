package com.example.bankofwords.objects;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
public class Image {
    private final Long wordId;
    private final String imageName;
}