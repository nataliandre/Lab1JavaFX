package com.so.Algorithms;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class Controller{
    private String newLine = "\n";

    @FXML
    TextArea textArea;

    @FXML
    Spinner<Integer> quantitySpinner;

    @FXML
    Spinner<Integer> maxLegthOfProcessSpinner;

    @FXML
    Spinner<Integer> rangeOfCallsSpinner;

    @FXML
    ToggleGroup algorithms;

    @FXML
    protected void FCFS() {
        textArea.clear();
        textArea.appendText("Wybrano FCFS." + newLine + "Potwierdź wybór." + newLine);
    }

    @FXML
    protected void SJF() {
        textArea.clear();
        textArea.appendText("Wybrano SJF." + newLine + "Potwierdź wybór." + newLine);
    }

    @FXML
    protected void Rotacyjny() {
        textArea.clear();
        textArea.appendText("Wybrano Rotacyjny." + newLine + "Potwierdź wybór." + newLine);
    }

    @FXML
    protected void run() {
        RadioButton rb = (RadioButton) algorithms.getSelectedToggle();
        textArea.clear();
        if (rb != null) {
            switch (rb.getId()) {
                case "1":
                    FCFS fcfs = new FCFS(quantitySpinner.getValue());
                    fcfs.run();
                    break;
                case "2":
                    break;
                case "3":
                    break;
                default:
                    textArea.appendText("Nie zaznaczono z jakiego chcesz skorzystać algorytmu!");
            }
        } else {
            textArea.appendText("Nie zaznaczono z jakiego chcesz skorzystać algorytmu!");
        }
    }

    private class FCFS implements Algorithms {
        private int id, quantity;
        private ArrayList<Process> processes;
        private Queue<Process> queue;

        public FCFS(int quantity) {
            this.quantity = quantity;
            processes = new ArrayList<>();
            queue = new LinkedList<>();
            textArea.appendText("Utworzono następujące procesy:" + newLine);
            createProcesses();
            textArea.appendText(newLine);
        }

        public FCFS(){
            processes = new ArrayList<>();
            queue = new LinkedList<>();
        }

        public void createProcesses(){
            Random r = new Random();
            while(id < quantity){
                processes.add(new Process(id, (int)(r.nextDouble() * (maxLegthOfProcessSpinner.getValue()) + 1), (int)(r.nextDouble() * (rangeOfCallsSpinner.getValue() - 1))));
                id++;
            }
            display();
        }

        public void run() {
            int cycles = 0;
            double totalWaitingTime = 0, processesDone = 0; //cykle liczone od zerowego cyklu
            boolean done = false;

            if (processes.size() > 0) {
                while (!done) {
                    //sprawdza czy istnieje, bądź istnieją procesy do dodania do kolejki
                    int index;
                    do {
                        index = checkWhichIsCallingNow(cycles);
                        if (index >= 0) {
                            Process temp = processes.get(index);
                            if (queue.add(temp)) {
                                textArea.appendText(String.format("Cykl: %-5d | Dodano   | ID: %-5d | długość: %-5d", cycles, temp.getId(), temp.getLength()) + newLine);
                            }
                            processes.remove(index);
                        }
                    } while (index != -1);

                    //wykonywanie jednego cyklu instrukcji
                    if (queue.size() > 0) {
                        Process temp = queue.peek();
                        int id = temp.getId();
                        if (temp.getCurrentLength() == temp.getLength()) {
                            temp.setStartedAt(cycles);
                            temp.setWaitingTime(cycles - temp.getCallAt());
                        }
                        temp.setCurrentLength(temp.getCurrentLength() - 1);
                        if (temp.getCurrentLength() == 0) {
                            int waitingTime = temp.getWaitingTime();
                            totalWaitingTime += waitingTime;
                            queue.remove();
                            processesDone++;
                            textArea.appendText(String.format("Cykl: %-5d | Wykonano | ID: %-5d | czas oczekiwania: %-5d", cycles, id, waitingTime) + newLine);
                        }
                        if (queue.size() == 0 && processes.isEmpty()) {
                            done = true;
                        }
                    }

                    //kolejny cykl
                    cycles++;
                }
                textArea.appendText(newLine + "Średni czas oczekiwania: " + totalWaitingTime/processesDone);
            } else {
                textArea.appendText("Brak procesów do wykonania!");
            }
        }

        public void display(){
            if (processes.isEmpty()) {
                textArea.appendText("Brak procesów" + newLine);
            } else {
                for (int i = 0; i < processes.size(); i++) {
                    textArea.appendText(processes.get(i).toString() + newLine);
                }
            }
        }

        public int checkWhichIsCallingNow(int cycle) {
            int index = -1;

            for (int i = 0; i < processes.size(); i++){
                if (processes.get(i).getCallAt() == cycle) {
                    index = i;
                    break;
                }
            }

            return index;
        }
    }
}