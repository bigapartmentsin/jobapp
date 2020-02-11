package com.abln.chat.ui.Network;

import com.pubnub.api.models.consumer.history.PNHistoryItemResult;

import java.util.List;

public interface IMNetworkResponse {

    void processFinish(String output);

    void onProcessFinish(List<PNHistoryItemResult> results);

}