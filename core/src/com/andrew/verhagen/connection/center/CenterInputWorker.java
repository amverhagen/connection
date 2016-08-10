package com.andrew.verhagen.connection.center;

class CenterInputWorker extends Thread {

    private final UDPInputHandler inputHandler;

    public CenterInputWorker(UDPInputHandler inputHandler) {
        this.inputHandler = inputHandler;
    }

    @Override
    public void run() {
        try {
            while (true) {
                synchronized (inputHandler) {
                    inputHandler.getInputSocket().receive(inputHandler.getDatagramPacket());
                    inputHandler.handleInput();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("Closing socket");
        }
    }
}
