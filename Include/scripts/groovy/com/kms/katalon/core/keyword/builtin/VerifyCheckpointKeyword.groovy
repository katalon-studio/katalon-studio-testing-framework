package com.kms.katalon.core.keyword.builtin

import groovy.transform.CompileStatic

import java.text.MessageFormat


import org.apache.commons.lang.ObjectUtils
import org.apache.commons.lang.StringUtils
import org.apache.commons.lang.math.NumberUtils

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.annotation.internal.Action
import com.kms.katalon.core.checkpoint.Checkpoint;
import com.kms.katalon.core.checkpoint.CheckpointCell
import com.kms.katalon.core.constants.StringConstants
import com.kms.katalon.core.exception.StepErrorException
import com.kms.katalon.core.exception.StepFailedException
import com.kms.katalon.core.helper.KeywordHelper
import com.kms.katalon.core.keyword.internal.AbstractKeyword
import com.kms.katalon.core.keyword.internal.KeywordExecutor
import com.kms.katalon.core.keyword.internal.KeywordMain
import com.kms.katalon.core.keyword.internal.SupportLevel
import com.kms.katalon.core.logging.ErrorCollector
import com.kms.katalon.core.logging.KeywordLogger
import com.kms.katalon.core.logging.model.TestStatus
import com.kms.katalon.core.main.TestCaseMain
import com.kms.katalon.core.main.TestResult
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testcase.TestCaseBinding

import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.testdata.TestData

@Action(value = "verifyCheckpoint")
public class VerifyCheckpointKeyword extends AbstractKeyword {

    @CompileStatic
    @Override
    public SupportLevel getSupportLevel(Object ...params) {
        return SupportLevel.BUITIN
    }

    @CompileStatic
    @Override
    public Object execute(Object ...params) {
        Checkpoint checkpoint = (Checkpoint) params[0]
        boolean logChangedValues = (boolean) params[1]
        FailureHandling flowControl = (FailureHandling)(params.length > 2 && params[2] instanceof FailureHandling ? params[2] : RunConfiguration.getDefaultFailureHandling())
        return verifyCheckpoint(checkpoint,logChangedValues,flowControl)
    }

    @CompileStatic
    public boolean verifyCheckpoint(Checkpoint checkpoint, boolean logChangedValues, FailureHandling flowControl) throws StepFailedException {
        KeywordMain.runKeyword({
            logger.logDebug(StringConstants.KW_MSG_VERIFY_CHECKPOINT)
            if (checkpoint == null) {
                throw new IllegalArgumentException(StringConstants.KW_MSG_CHECKPOINT_IS_NULL)
            }
            logger.logDebug(MessageFormat.format(StringConstants.KW_MSG_CHECKPOINT_ID_X, checkpoint.getId()))
            if (checkpoint.getTakenDate() == null) {
                throw new StepFailedException(StringConstants.KW_MSG_NO_SNAPSHOT)
            }
            if (checkpoint.getCheckpointData() == null) {
                logger.logWarning(StringConstants.KW_MSG_CHECKPOINT_DATA_IS_NULL)
            }
            if (checkpoint.getSourceData() == null) {
                logger.logWarning(StringConstants.KW_MSG_CHECKPOINT_SRC_DATA_IS_NULL)
            }
            if (checkpoint.getCheckpointData() == null && checkpoint.getSourceData() == null) {
                logger.logPassed(StringConstants.KW_MSG_CHECKPOINT_DATA_MATCHES_WITH_NULL);
                return true
            }
            if ((checkpoint.getCheckpointData() != null && checkpoint.getSourceData() == null) || (checkpoint.getCheckpointData() == null && checkpoint.getSourceData() != null)) {
                throw new StepFailedException(StringConstants.KW_MSG_CHECKPOINT_DATA_DOES_NOT_MATCH)
            }

            if (checkpoint.getCheckpointRowNumbers() == checkpoint.getSourceRowNumbers()) {
                logger.logDebug(StringConstants.KW_MSG_CHECKPOINT_ROW_NUMBER_MATCHES)
            } else {
                logger.logWarning(StringConstants.KW_MSG_CHECKPOINT_ROW_NUMBER_DOES_NOT_MATCH)
            }

            if (checkpoint.getCheckpointColumnNumbers() == checkpoint.getSourceColumnNumbers()) {
                logger.logDebug(StringConstants.KW_MSG_CHECKPOINT_COL_NUMBER_MATCHES)
            } else {
                logger.logWarning(StringConstants.KW_MSG_CHECKPOINT_COL_NUMBER_DOES_NOT_MATCH)
            }

            List<List<Object>> sourceData = checkpoint.getSourceData()
            List<List<CheckpointCell>> checkpointData = checkpoint.getCheckpointData()
            try {
                logger.logDebug(StringConstants.KW_MSG_VERIFY_CHECKED_VALUES)
                boolean isDataNotChanged = true
                for (int rowIndex = 0; rowIndex < checkpoint.getCheckpointRowNumbers(); rowIndex++) {
                    List<CheckpointCell> row = checkpointData.get(rowIndex)
                    for (int colIndex = 0; colIndex < checkpoint.getCheckpointColumnNumbers(); colIndex++) {
                        CheckpointCell cell = row.get(colIndex)
                        if (!cell.isChecked()) {
                            continue
                        }

                        Object checkedValue = cell.getValue()
                        Object currentValue = sourceData.get(rowIndex).get(colIndex)
                        if (!ObjectUtils.equals(checkedValue, currentValue)) {
                            if (logChangedValues) {
                                logger.logWarning(MessageFormat.format(StringConstants.KW_MSG_CHECKPOINT_NOT_MATCH_AT_ROW_X_COL_Y_CHECKED_VAL_NEW_VAL, rowIndex + TestData.BASE_INDEX, colIndex + TestData.BASE_INDEX, checkedValue, currentValue))
                            }
                            isDataNotChanged = false
                        }
                    }
                }

                if (isDataNotChanged) {
                    logger.logPassed(StringConstants.KW_MSG_CHECKPOINT_DATA_MATCHES)
                    return true
                }

                throw new StepFailedException(StringConstants.KW_MSG_CHECKPOINT_DATA_DOES_NOT_MATCH)
            } catch (Exception e) {
                // Index out of bound
                throw new StepFailedException(StringConstants.KW_MSG_CHECKPOINT_DATA_DOES_NOT_MATCH)
            }
        }, flowControl, MessageFormat.format(StringConstants.KW_MSG_UNABLE_TO_VERIFY_CHECKPOINT_X, checkpoint != null ? checkpoint.getId() : StringUtils.EMPTY));
    }
}
