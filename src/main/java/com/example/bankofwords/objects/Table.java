package com.example.bankofwords.objects;

public class Table {
    private final long tableId;
    private final long creatorId;
    private final String name;

    public Table(long tableId, long creatorId, String name) {
        this.tableId = tableId;
        this.creatorId = creatorId;
        this.name = name;
    }

    public long getTableId() {
        return tableId;
    }


    public long getCreatorId() {
        return creatorId;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Table{" +
                "tableId=" + tableId +
                ", creatorId=" + creatorId +
                ", name='" + name + '\'' +
                '}';
    }
}
