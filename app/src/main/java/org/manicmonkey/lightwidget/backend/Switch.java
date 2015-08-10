package org.manicmonkey.lightwidget.backend;

/**
 * Created by James on 10/08/2015.
 */
public class Switch {

    private String name;
    private String group;
    private int switchNumber;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public int getSwitchNumber() {
        return switchNumber;
    }

    public void setSwitchNumber(int switchNumber) {
        this.switchNumber = switchNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Switch aSwitch = (Switch) o;

        if (switchNumber != aSwitch.switchNumber) return false;
        if (name != null ? !name.equals(aSwitch.name) : aSwitch.name != null) return false;
        return !(group != null ? !group.equals(aSwitch.group) : aSwitch.group != null);

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (group != null ? group.hashCode() : 0);
        result = 31 * result + switchNumber;
        return result;
    }

    @Override
    public String toString() {
        return "Switch{" +
                "name='" + name + '\'' +
                ", group='" + group + '\'' +
                ", switchNumber=" + switchNumber +
                '}';
    }
}
