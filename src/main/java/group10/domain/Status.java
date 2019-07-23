package group10.domain;

/**
 * The status of a story object.
 */
public enum Status {
    OPEN, CLOSED;


    @Override
    public String toString() {
        return this.name().charAt(0) + this.name().substring(1).toLowerCase();
    }
}
