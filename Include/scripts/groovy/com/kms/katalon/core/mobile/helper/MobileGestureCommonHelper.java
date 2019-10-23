package com.kms.katalon.core.mobile.helper;

import java.text.MessageFormat;

import com.kms.katalon.core.exception.StepFailedException;
import com.kms.katalon.core.logging.KeywordLogger;
import com.kms.katalon.core.mobile.constants.StringConstants;
import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MultiTouchAction;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.touch.offset.PointOption;

public class MobileGestureCommonHelper {
    
    private static final KeywordLogger logger = KeywordLogger.getInstance(MobileGestureCommonHelper.class);
    
    private static final int IOS_PINCH_OFFSET = 5;

    private static final int ANDROID_PINCH_OFFSET = 150;

    private static final int PINCH_PART_LENGTH = 10;

    public static void pinchToZoomIn(Number startX, Number startY, Number offset) {
        MobileCommonHelper.checkXAndY(startX, startY);
        checkOffset(offset);
        pinchToZoomIn(startX.intValue(), startY.intValue(), offset.intValue());
        logger.logPassed(
                MessageFormat.format(StringConstants.KW_LOG_PASSED_ZOOM_AT_X_Y_WITH_OFFSET_Z, startX, startY, offset));
    }

    private static void pinchToZoomIn(int startX, int startY, int offset) {
        offset = makeSureOffsetValueIsValid(startY, offset);
        int offsetParts = offset / PINCH_PART_LENGTH;
        int offsetRemainingPart = offset % PINCH_PART_LENGTH;
        AppiumDriver<?> driver = MobileDriverFactory.getDriver();
        TouchAction<?> leftTouchAction = new TouchAction<>(driver).press(PointOption.point(startX, startY));
        TouchAction<?> rightTouchAction = new TouchAction<>(driver).press(PointOption.point(startX, startY));
        for (int i = 0; i < offsetParts; i++) {
            leftTouchAction = leftTouchAction.moveTo(PointOption.point(0, -PINCH_PART_LENGTH));
            rightTouchAction = rightTouchAction.moveTo(PointOption.point(0, PINCH_PART_LENGTH));
        }
        if (offsetRemainingPart != 0) {
            leftTouchAction = leftTouchAction.moveTo(PointOption.point(0, -offsetRemainingPart));
            rightTouchAction = rightTouchAction.moveTo(PointOption.point(0, offsetRemainingPart));
        }
        leftTouchAction = leftTouchAction.release();
        rightTouchAction = rightTouchAction.release();

        MultiTouchAction multiTouchAction = new MultiTouchAction(driver).add(leftTouchAction).add(rightTouchAction);
        multiTouchAction.perform();
    }

    public static void pinchToZoomOut(Number endX, Number endY, Number offset) {
        MobileCommonHelper.checkXAndY(endX, endY);
        checkOffset(offset);
        pinchToZoomOut(endX.intValue(), endY.intValue(), offset.intValue());
        logger.logPassed(
                MessageFormat.format(StringConstants.KW_LOG_PASSED_PINCH_AT_X_Y_WITH_OFFSET_Z, endX, endY, offset));
    }

    private static void pinchToZoomOut(int endX, int endY, int offset) {
        AppiumDriver<?> driver = MobileDriverFactory.getDriver();
        offset = makeSureOffsetValueIsValid(endY, offset);
        int offsetParts = offset / PINCH_PART_LENGTH;
        int offsetRemainingPart = offset % PINCH_PART_LENGTH;
        int additionalOffset = (driver instanceof AndroidDriver<?>) ? ANDROID_PINCH_OFFSET : IOS_PINCH_OFFSET;
        TouchAction<?> leftTouchAction = new TouchAction<>(driver).press(PointOption.point(endX, endY - offset - additionalOffset));
        TouchAction<?> rightTouchAction = new TouchAction<>(driver).press(PointOption.point(endX, endY + offset + additionalOffset));
        for (int i = 0; i < offsetParts; i++) {
            leftTouchAction = leftTouchAction.moveTo(PointOption.point(0, PINCH_PART_LENGTH));
            rightTouchAction = rightTouchAction.moveTo(PointOption.point(0, -PINCH_PART_LENGTH));
        }
        if (offsetRemainingPart != 0) {
            leftTouchAction = leftTouchAction.moveTo(PointOption.point(0, offsetRemainingPart));
            rightTouchAction = rightTouchAction.moveTo(PointOption.point(0, -offsetRemainingPart));
        }
        leftTouchAction = leftTouchAction.release();
        rightTouchAction = rightTouchAction.release();
        MultiTouchAction multiTouchAction = new MultiTouchAction(driver).add(leftTouchAction).add(rightTouchAction);
        multiTouchAction.perform();
    }

    private static void checkOffset(Number offset) {
        logger.logDebug(StringConstants.COMM_LOG_INFO_CHECKING_OFFSET);
        if (offset == null) {
            throw new StepFailedException(MessageFormat.format(StringConstants.KW_MSG_FAILED_PARAM_X_CANNOT_BE_NULL,
                    "offset"));
        }
    }

    private static int makeSureOffsetValueIsValid(int startY, int offset) {
        int deviceHeight = MobileDriverFactory.getDriver().manage().window().getSize().height;
        if (startY - offset < 0) {
            offset = startY;
        } else if (startY + offset > deviceHeight) {
            offset = deviceHeight - startY;
        }
        return offset;
    }
}
