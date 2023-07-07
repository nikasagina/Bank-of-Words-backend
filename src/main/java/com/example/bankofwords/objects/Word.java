package com.example.bankofwords.objects;

import java.util.Objects;

public class Word {
    private final String word;
    private final String definition;
    private final long tableId;

    public Word(String word, String definition, long tableId) {
        this.word = word;
        this.definition = definition;
        this.tableId = tableId;
    }

    public String getWord() {
        return word;
    }


    public String getDefinition() {
        return definition;
    }


    public long getTableId() {
        return tableId;
    }

    @Override
    public String toString() {
        return "Word{" +
                "word='" + word + '\'' +
                ", definition='" + definition + '\'' +
                ", tableId='" + tableId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Word word1 = (Word) o;
        return Objects.equals(word, word1.word) && tableId == word1.tableId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(word, definition);
    }
}
