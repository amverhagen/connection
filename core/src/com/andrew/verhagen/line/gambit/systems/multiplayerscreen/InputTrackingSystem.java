package com.andrew.verhagen.line.gambit.systems.multiplayerscreen;

import com.artemis.BaseSystem;

import java.util.Arrays;

public class InputTrackingSystem extends BaseSystem {

    private int currentInput;
    private final TrackedInput[] inputs;

    public InputTrackingSystem(int numberOfInputs) {
        this.setEnabled(false);
        this.currentInput = 0;
        this.inputs = new TrackedInput[numberOfInputs];
    }

    public void addInput(byte inputType) {

        this.inputs[currentInput % inputs.length] = new TrackedInput(currentInput, inputType);
        this.processSystem();
        this.currentInput++;
        System.out.println(Arrays.toString(this.inputs));
    }

    private class TrackedInput {

        int inputNumber;
        byte inputType;

        public TrackedInput(int inputNumber, byte inputType) {
            this.inputNumber = inputNumber;
            this.inputType = inputType;
        }

        @Override
        public String toString() {
            return inputNumber + " with input " + inputType;
        }
    }

    @Override
    protected void processSystem() {
        //sort input from newest to oldest
        //set network buffer to input protocol
        //add current input number to buffer
        //add input bytes to buffer
    }
}
