package com.example.bankofwords.objects;

import java.util.Objects;

public class Word {
    private final long id;
    private final String word;
    private final String definition;
    private final long tableId;

    public Word(long id, String word, String definition, long tableId) {
        this.id = id;
        this.word = word;
        this.definition = definition;
        this.tableId = tableId;
    }

    public long getId() {
        return id;
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
                "id='" + id + '\'' +
                ", word='" + word + '\'' +
                ", definition='" + definition + '\'' +
                ", tableId='" + tableId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Word word1 = (Word) o;
        return Objects.equals(id, word1.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(word, definition, id);
    }
}
