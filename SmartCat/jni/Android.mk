LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := SmartCat
LOCAL_SRC_FILES := SmartCat.cpp

include $(BUILD_SHARED_LIBRARY)
