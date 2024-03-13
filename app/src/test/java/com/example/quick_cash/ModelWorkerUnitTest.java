package com.example.quick_cash;

import static org.junit.Assert.*;

import com.example.quick_cash.Model.ModelWorker;

import org.junit.Test;

public class ModelWorkerUnitTest {

    /**
     * Testing model worker constructor initialization
     */
    @Test
    public void testModelWorkerConstructor() {
        ModelWorker worker = new ModelWorker(1, "Software Engineer");

        assertEquals(1, worker.getImg());
        assertEquals("Software Engineer", worker.getDes());
    }

    /**
     * testing setters and getters in model worker.
     */
    @Test
    public void testModelWorkerSettersAndGetters() {
        ModelWorker worker = new ModelWorker();

        worker.setImg(2);
        worker.setDes("Data Scientist");

        assertEquals(2, worker.getImg());
        assertEquals("Data Scientist", worker.getDes());
    }

    /**
     * testing default constructor in model worker.
     */
    @Test
    public void testModelWorkerDefaultConstructor() {
        ModelWorker worker = new ModelWorker();

        assertEquals(0, worker.getImg());
        assertNull(worker.getDes());
    }

}