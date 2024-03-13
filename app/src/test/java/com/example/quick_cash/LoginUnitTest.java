package com.example.quick_cash;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import com.example.quick_cash.Credentials.LoginActivity;

/**
 * JUnit test class for Login class
 */
public class LoginUnitTest {
    static LoginActivity loginActivity;

    @BeforeClass
    public static void setup() {
        loginActivity = new LoginActivity();
    }

    /**
     * Test for empty username
     * False if username is empty
     */
    @Test
    public void emptyUsername() {
        assertFalse(loginActivity.emptyUsername(""));
    }

    /**
     * Test for non empty username
     * True if username is not empty
     */
    @Test
    public void nonEmptyUsername() {
        assertTrue(loginActivity.emptyUsername("abc123"));
    }

    /**
     * Test for empty password
     * False if password is empty
     */
    @Test
    public void emptyPassword() {
        assertFalse(loginActivity.emptyPassword(""));
    }

    /**
     * Test for non empty password
     * True if password is not empty
     */
    @Test
    public void nonEmptyPassword() {
        assertTrue(loginActivity.emptyPassword("!password"));
    }

}
