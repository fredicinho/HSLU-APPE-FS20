package ch.hslu.appe.entities;

public enum AdmonitionLevel {
    NOTHING(0),
    FIRST_LEVEL(1),
    SECOND_LEVEL(2),
    THIRD_LEVEL(3);

    private final int id;

    AdmonitionLevel(final int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }



}
