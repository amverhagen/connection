package com.andrew.verhagen.connection.center;

import java.io.IOException;

class CenterOutputWorker extends Thread {

    private OutputSender outputSender;

    public CenterOutputWorker(OutputSender outputSender) {
        this.outputSender = outputSender;
    }

    @Override
    public void run() {
        try {
            while (true) {
                outputSender.sendOutput();
                try {
                    Thread.sleep(outputSender.getOutputDelayInMilliSeconds());
                } catch (InterruptedException e) {
                    throw new IOException();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }
}
