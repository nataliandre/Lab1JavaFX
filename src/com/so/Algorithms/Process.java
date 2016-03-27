package com.so.Algorithms;

public class Process {
    private int id, length, currentLength, callAt, startedAt, waitingTime;

    public Process(int id, int length, int callAt){
        this.id = id;
        this.length = length;
        this.currentLength = length;
        this.callAt = callAt;
        this.startedAt = 0;
        this.waitingTime = 0;
    }

    public String toString(){
        return String.format("ID: %-4d Długość: %-4d Moment zgłaszania: %-4d", id, length, callAt);
    }

    public int getId() {
        return id;
    }

    public int getLength() {
        return length;
    }

    public int getCurrentLength() {
        return currentLength;
    }

    public int getCallAt() {
        return callAt;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public void setCurrentLength(int currentLength) {
        this.currentLength = currentLength;
    }

    public void setStartedAt(int startedAt) {
        this.startedAt = startedAt;
    }
}