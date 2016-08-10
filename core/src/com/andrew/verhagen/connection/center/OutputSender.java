package com.andrew.verhagen.connection.center;

public interface OutputSender {

    int getOutputDelayInMilliSeconds();

    void sendOutput();
}
