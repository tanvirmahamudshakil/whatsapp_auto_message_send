package com.example.myapplication

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.pm.PackageManager
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import kotlin.concurrent.thread


class MyAccessibilityService : AccessibilityService() {

     var TAG: String = "AccessibilityService"

    override fun onAccessibilityEvent(p0: AccessibilityEvent?) {
        Log.e(TAG, "onAccessibilityEvent: ${p0!!.packageName.toString()}")
        var packageName = p0!!.packageName.toString();
        var classNAme = p0!!.className;
        Log.e(TAG, "Action: ${p0.text[0]}")
        val rootNodeInfo: AccessibilityNodeInfoCompat = AccessibilityNodeInfoCompat.wrap(getRootInActiveWindow())

        var messageNodeList : List<AccessibilityNodeInfoCompat> = rootNodeInfo.findAccessibilityNodeInfosByViewId("com.whatsapp:id/entry")
        if(messageNodeList == null || messageNodeList.isEmpty()){
            return;
        }

        var messagefield : AccessibilityNodeInfoCompat =messageNodeList[0];
        if(messagefield == null || messagefield.text.length == 0){
            return;
        }

        var sendMessageNodeList : List<AccessibilityNodeInfoCompat> = rootNodeInfo.findAccessibilityNodeInfosByViewId("com.whatsapp:id/send")
        if(sendMessageNodeList == null || sendMessageNodeList.isEmpty()){
            return;
        }

        var sendMessage : AccessibilityNodeInfoCompat = sendMessageNodeList[0]
        if(!sendMessage.isVisibleToUser){
            return;
        }

        sendMessage.performAction(AccessibilityNodeInfo.ACTION_CLICK)


    }
    private fun getButtonText(nodeInfo: AccessibilityNodeInfo): String {
        return if (nodeInfo.text != null) {
            nodeInfo.text.toString()
        } else ""
    }

    override fun onInterrupt() {
        Log.e(TAG, "onInterrupt: some thing wrong")
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        var info = AccessibilityServiceInfo()
        info.apply {
            eventTypes = AccessibilityEvent.TYPE_VIEW_CLICKED or AccessibilityEvent.TYPE_VIEW_FOCUSED
            feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN
            notificationTimeout = 100
        }
        Log.e(TAG, "onServiceConnected: ")
        this.serviceInfo = info
    }
}