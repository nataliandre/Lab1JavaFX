package com.so.Algorithms;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.*;

public class Controller {
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
    Spinner<Integer> quantOfTime;

    @FXML
    CheckBox checkbox;

    @FXML
    ToggleGroup algorithms;

    @FXML
    protected void FCFS() {
        checkbox.setDisable(true);
        quantOfTime.setDisable(true);
        textArea.clear();
        textArea.appendText("Wybrano FCFS." + newLine + "Potwierdź wybór." + newLine);
    }

    @FXML
    protected void SJF() {
        checkbox.setDisable(false);
        quantOfTime.setDisable(true);
        textArea.clear();
        textArea.appendText("Wybrano SJF." + newLine + "Potwierdź wybór." + newLine);
    }

    @FXML
    protected void Rotacyjny() {
        checkbox.setDisable(true);
        quantOfTime.setDisable(false);
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
                    SJF sjf = new SJF(quantitySpinner.getValue());
                    sjf.run();
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

    private ArrayList<Process> processes;
    private Queue<Process> queue;

    private void createProcesses(int quantity) {
        Random r = new Random();

        for (int i = 0; i < quantity; i++) {
            processes.add(new Process(i, (int)(r.nextDouble() * (maxLegthOfProcessSpinner.getValue()) + 1), (int)(r.nextDouble() * (rangeOfCallsSpinner.getValue() - 1))));
        }

        display();
    }

    private void display(){
        if (processes.isEmpty()) {
            textArea.appendText("Brak procesów" + newLine);
        } else {
            for (int i = 0; i < processes.size(); i++) {
                textArea.appendText(processes.get(i).toString() + newLine);
            }
        }
    }

    private class FCFS implements Algorithms {
        public FCFS(int quantity) {
            processes = new ArrayList<>();
            createProcesses(quantity);
            queue = new LinkedList<>();
            textArea.appendText("Utworzono następujące procesy:" + newLine);
            textArea.appendText(newLine);
        }

        public FCFS(){
            processes = new ArrayList<>();
            queue = new LinkedList<>();
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
                        index = checkWhichIsBurstingNow(cycles);

                        if (index >= 0) {
                            Process temp = processes.get(index);

                            if (queue.add(temp)) {
                                textArea.appendText(String.format("Cykl: %-5d | Dodano   | ID: %-5d | długość: %-5d", cycles, temp.getId(), temp.getBurstTime()) + newLine);
                            }

                            processes.remove(index);
                        }
                    } while (index != -1);

                    //wykonywanie jednego cyklu instrukcji
                    if (queue.size() > 0) {
                        Process temp = queue.peek();
                        int id = temp.getId();

                        if (temp.getCurrentLength() == temp.getBurstTime()) {
                            temp.setStartedAt(cycles);
                            temp.setWaitTime(cycles - temp.getArrivalTime());
                        }

                        temp.setCurrentLength(temp.getCurrentLength() - 1);

                        if (temp.getCurrentLength() == 0) {
                            int waitingTime = temp.getWaitTime();
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

        private int checkWhichIsBurstingNow(int cycle) {
            int index = -1;

            for (int i = 0; i < processes.size(); i++){
                if (processes.get(i).getArrivalTime() == cycle) {
                    index = i;

                    break;
                }
            }

            return index;
        }
    }

    private class SJF {
        public SJF(int quantity) {

            processes = new ArrayList<>();
            createProcesses(quantity);
            queue = new LinkedList<>();
            textArea.appendText("Utworzono następujące procesy:" + newLine);
            textArea.appendText(newLine);
        }

        public void run() {
            int cycles = 0;
            double totalWaitingTime = 0, processesDone = 0;
            boolean done = false;
            ArrayList<Process> tempList = new ArrayList<>();

            if (processes.size() > 0) {
                while (!done) {
                    //sprawdza czy istnieje, bądź istnieją procesy do dodania do kolejki
                    int index;

                    do {
                        index = checkWhichIsBurstingNow(cycles);

                        if (index >= 0) {
                            Process temp = processes.get(index);

                            if (tempList.add(temp)) {
                                textArea.appendText(String.format("Cykl: %-5d | Dodano   | ID: %-5d | długość: %-5d", cycles, temp.getId(), temp.getBurstTime()) + newLine);
                            }

                            processes.remove(index);
                        }
                    } while (index != -1);

                    tempList.sort(new Compare());

                    //Obsługuje procesy według algorytmu SJF
                    if (tempList.size() > 0) {
                        Process temp = tempList.get(0);
                        int id = temp.getId();

                        if (temp.getCurrentLength() == temp.getBurstTime()) {
                            temp.setStartedAt(cycles);
                            temp.setWaitTime(cycles - temp.getArrivalTime());
                        }

                        temp.setCurrentLength(temp.getCurrentLength() - 1);

                        if (temp.getCurrentLength() == 0) {
                            int waitTime = temp.getWaitTime();
                            totalWaitingTime += waitTime;
                            tempList.remove(0);
                            processesDone++;
                            textArea.appendText(String.format("Cykl: %-5d | Wykonano | ID: %-5d | czas oczekiwania: %-5d", cycles, id, waitTime) + newLine);
                        }

                        if (tempList.size() == 0 && processes.isEmpty()) {
                            done = true;
                        }
                    }

                    cycles++;
                }

                textArea.appendText(newLine + "Średni czas oczekiwania: " + totalWaitingTime/processesDone);
            } else {
                textArea.appendText("Brak procesów do wykonania!");
            }
        }

        private int checkWhichIsBurstingNow(int cycle) {
            int index = -1;

            for (int i = 0; i < processes.size(); i++){
                if (processes.get(i).getArrivalTime() == cycle) {
                    index = i;

                    break;
                }
            }

            return index;
        }
    }
}