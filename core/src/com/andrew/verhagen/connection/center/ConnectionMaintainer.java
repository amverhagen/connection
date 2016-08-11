package com.andrew.verhagen.connection.center;

import java.net.InetSocketAddress;
import java.util.ArrayList;

public abstract class ConnectionMaintainer {

    protected int maxConnections;
    protected int timeOutTimeInMilliseconds;

    protected ArrayList<ConnectionAddress> activeConnectionAddresses;
    protected ArrayList<ConnectionAddress> expiredConnectionAddresses;

    public ConnectionMaintainer(int maxConnections, int timeOutTimeInMilliseconds) {
        this.maxConnections = maxConnections;
        this.timeOutTimeInMilliseconds = timeOutTimeInMilliseconds;
    }

    public synchronized boolean addConnection(InetSocketAddress incomingAddress) {
        if (this.holdingConnection(incomingAddress) || this.maxConnections <= activeConnectionAddresses.size())
            return false;
        else
            return this.activeConnectionAddresses.add(new ConnectionAddress(incomingAddress, timeOutTimeInMilliseconds));
    }

    public synchronized boolean holdingConnection(InetSocketAddress incomingAddress) {
        for (ConnectionAddress activeAddress : activeConnectionAddresses) {
            if (activeAddress.hasSameAddressAndPort(incomingAddress))
                return true;
        }
        return false;
    }

    public synchronized void receivedValidInputFromAddress(InetSocketAddress inputAddress) {
        refreshConnectionAddresses();
        updateLastInputTimeOfAddress(inputAddress);
    }

    private synchronized void updateLastInputTimeOfAddress(InetSocketAddress inputAddress) {
        long inputTime = System.nanoTime();
        for (ConnectionAddress activeAddress : activeConnectionAddresses) {
            if (activeAddress.hasSameAddressAndPort(inputAddress))
                activeAddress.timeOfLastValidInput = inputTime;
        }
    }

    private synchronized void refreshConnectionAddresses() {
        markExpiredAddresses();
        removeExpiredAddresses();
    }

    private synchronized void markExpiredAddresses() {
        long currentTime = System.nanoTime();
        for (ConnectionAddress connectionAddress : activeConnectionAddresses) {
            if ((currentTime - connectionAddress.timeOfLastValidInput) >= connectionAddress.timeOutTimeInNanoSeconds) {
                System.err.println("Address had a timeout time of " + connectionAddress.timeOutTimeInNanoSeconds);
                System.err.println("Address last input was " + (currentTime - connectionAddress.timeOfLastValidInput) + " nano seconds ago.");
                expiredConnectionAddresses.add(connectionAddress);
            }
        }
    }

    private synchronized void removeExpiredAddresses() {
        for (ConnectionAddress expiredAddress : expiredConnectionAddresses) {
            activeConnectionAddresses.remove(expiredAddress);
        }
    }
}
