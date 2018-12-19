package com.kms.katalon.core.checkpoint;

/**
 * Checkpoint Cell
 */
public class CheckpointCell {

    private Object value;

    private boolean checked;

    public CheckpointCell(Object value) {
        this(value, false);
    }

    public CheckpointCell(Object value, boolean checked) {
        this.value = value;
        this.checked = checked;
    }

    /**
     * Get value of the cell
     * 
     * @return value of the cell
     */
    public Object getValue() {
        return value;
    }

    /**
     * Set the value to the cell
     * 
     * @param value new value of the cell
     */
    public void setValue(Object value) {
        this.value = value;
    }

    /**
     * Check if this cell is checked
     * 
     * @return true if this cell is checked; otherwise false
     */
    public boolean isChecked() {
        return checked;
    }

    /**
     * Set the checked flag for this cell
     * 
     * @param checked the checked flag
     */
    public void setChecked(boolean checked) {
        this.checked = checked;
    }

}
