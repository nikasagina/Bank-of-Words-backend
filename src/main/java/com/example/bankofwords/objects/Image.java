package com.example.bankofwords.objects;

public class Image {
    private final String imageName;
    private final Long wordId;

    public Image(long wordId, String imageName) {
        this.imageName = imageName;
        this.wordId = wordId;
    }

    public String getImageName() {
        return imageName;
    }

    public Long getWordId() {
        return wordId;
    }
}
