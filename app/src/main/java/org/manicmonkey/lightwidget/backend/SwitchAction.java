package org.manicmonkey.lightwidget.backend;

/**
 * @author James Baxter 2015-08-10.
 */
public class SwitchAction {
    private Boolean on;

    public SwitchAction() {
    }

    public SwitchAction(Boolean on) {
        this.on = on;
    }

    public Boolean getOn() {
        return on;
    }

    public void setOn(Boolean on) {
        this.on = on;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SwitchAction that = (SwitchAction) o;

        return !(on != null ? !on.equals(that.on) : that.on != null);

    }

    @Override
    public int hashCode() {
        return on != null ? on.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "SwitchAction{" +
                "on=" + on +
                '}';
    }
}
