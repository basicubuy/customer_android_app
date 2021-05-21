package com.ubuyng.app.ubuyapi.Interface;

public interface AdapBidsInterface {
    void onDeclineClick(String bidId, String bidderID);
    void onAcceptClick(String  bidId, String bidderID);

}
