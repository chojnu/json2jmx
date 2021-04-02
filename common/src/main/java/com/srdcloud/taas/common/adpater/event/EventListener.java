package com.srdcloud.taas.common.adpater.event;

import com.srdcloud.taas.common.event.TestCaseCompileNotify;

public interface EventListener {
    void onHandleEvent(TestCaseCompileNotify testCaseCompileNotify);
}
