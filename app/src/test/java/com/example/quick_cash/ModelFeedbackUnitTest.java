package com.example.quick_cash;

import static org.junit.Assert.*;

import com.example.quick_cash.Model.ModelFeedback;

import org.junit.Test;

public class ModelFeedbackUnitTest {

    /**
     * Testing ModelFeedBack constructor initialization
     */
    @Test
    public void testModelFeedbackConstructor() {
        ModelFeedback feedback = new ModelFeedback("testUser", "Great job!");

        assertEquals("testUser", feedback.getUsername());
        assertEquals("Great job!", feedback.getComment());
    }

    /**
     * Testing set and get in ModelFeedback
     */
    @Test
    public void testModelFeedbackSettersAndGetters() {
        ModelFeedback feedback = new ModelFeedback("User", "Excellent work!");

        feedback.setUsername("anotherUser");
        feedback.setComment("Nice work!");

        assertEquals("anotherUser", feedback.getUsername());
        assertEquals("Nice work!", feedback.getComment());
    }

}