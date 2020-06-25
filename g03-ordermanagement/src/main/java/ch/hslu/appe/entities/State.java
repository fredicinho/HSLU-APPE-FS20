package ch.hslu.appe.entities;

/**
 * State of Orders.
 *
 */
public enum State {
    ORDERED(0),
    DELIVERED(1),
    WAITING_FOR_PRODUCT(2),
    CANCELLED(3),
    STATE_NOT_AVAILABLE(4);

    private int id;

    State(final int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static State getStateForValue(final String id) {
        for (State state : State.values()) {
            if (Integer.toString(state.id).equals(id)) {
                return state;
            }
        }
        return STATE_NOT_AVAILABLE;
    }


}
