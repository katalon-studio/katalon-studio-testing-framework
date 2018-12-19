package com.kms.katalon.core.checkpoint;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

import com.google.gson.reflect.TypeToken;

public class Checkpoint {

    public static final Type CHECKPOINT_DATA_TYPE = new TypeToken<List<List<CheckpointCell>>>() {}.getType();

    private String id;

    private List<List<CheckpointCell>> checkpointData;

    private List<List<Object>> sourceData;

    private Date takenDate;

    private String description;

    public Checkpoint(String checkpointId) {
        id = checkpointId;
    }

    /**
     * Get the id
     * 
     * @return the id
     */
    public String getId() {
        return id;
    }

    /* public */void setId(String id) {
        this.id = id;
    }

    /**
     * Get the checkpoint data
     * 
     * @return the checkpoint data
     */
    public List<List<CheckpointCell>> getCheckpointData() {
        return checkpointData;
    }

    /* public */void setCheckpointData(List<List<CheckpointCell>> checkpointData) {
        this.checkpointData = checkpointData;
    }

    /**
     * Get the source data
     * 
     * @return the source data
     */
    public List<List<Object>> getSourceData() {
        return sourceData;
    }

    /* public */void setSourceData(List<List<Object>> sourceData) {
        this.sourceData = sourceData;
    }

    /**
     * Get the taken date
     * 
     * @return the taken date
     */
    public Date getTakenDate() {
        return takenDate;
    }

    /* public */void setTakenDate(Date takenDate) {
        this.takenDate = takenDate;
    }

    /**
     * Get the description
     * 
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /* public */void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get the total row numbers of the checkpoint
     * 
     * @return the total row numbers of the checkpoint
     */
    public int getCheckpointRowNumbers() {
        return getCheckpointData().size();
    }

    /**
     * Get the total column numbers of the checkpoint
     * 
     * @return the total column numbers of the checkpoint
     */
    public int getCheckpointColumnNumbers() {
        if (getCheckpointRowNumbers() == 0) {
            return 0;
        }
        return getCheckpointData().get(0).size();
    }

    /**
     * Get the total row numbers of the source
     * 
     * @return the the total row numbers of the source
     */
    public int getSourceRowNumbers() {
        return getSourceData().size();
    }

    /**
     * Get the total column numbers of the source
     * 
     * @return the total column numbers of the source
     */
    public int getSourceColumnNumbers() {
        if (getSourceRowNumbers() == 0) {
            return 0;
        }
        return getSourceData().get(0).size();
    }

}
