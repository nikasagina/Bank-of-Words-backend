package com.example.bankofwords.objects;

import java.util.Objects;

public class Table {
    private final long tableId;
    private final long creatorId;
    private final String name;

    public Table(long tableId, long creatorId, String name) {
        this.tableId = tableId;
        this.creatorId = creatorId;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Table table = (Table) o;
        return Objects.equals(name, table.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tableId, creatorId, name);
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
