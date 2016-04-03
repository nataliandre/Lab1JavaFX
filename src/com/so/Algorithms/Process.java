package com.so.Algorithms;

public class Process {
    private int id, burstTime, currentLength, arrivalTime, startedAt, waitTime;

    public Process(int id, int burstTime, int arrivalTime){
        this.id = id;
        this.burstTime = burstTime;
        this.currentLength = burstTime;
        this.arrivalTime = arrivalTime;
        this.startedAt = 0;
        this.waitTime = 0;
    }

    public String toString(){
        return String.format("ID: %-4d Długość: %-4d Moment zgłaszania: %-4d", id, burstTime, arrivalTime);
    }

    public int getId() {
        return id;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public int getCurrentLength() {
        return currentLength;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }

    public void setCurrentLength(int currentLength) {
        this.currentLength = currentLength;
    }

    public void setStartedAt(int startedAt) {
        this.startedAt = startedAt;
    }
}