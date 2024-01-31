package fr.newstaz.istore.model;

public class Store {
    private int id;

    private final String name;

    public Store(String name) {
        this.name = name;
    }

    public Store(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
