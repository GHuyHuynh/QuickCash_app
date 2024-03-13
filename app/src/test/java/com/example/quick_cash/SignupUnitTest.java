package com.example.quick_cash;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

import com.example.quick_cash.Credentials.SignupActivity;

/**
 * JUnit test class for Signup class
 */
public class SignupUnitTest {
    static SignupActivity signupActivity;

    @BeforeClass
    public static void setup(){signupActivity = new SignupActivity();}

    /**
     * Test if email is valid
     * True if email is valid
     */
    @Test
    public void emailIsValid(){
        assertTrue(signupActivity.isValidEmail("test@dal.ca"));
    }

    /**
     * Test if email is invalid, do not fit the Regex
     * False if email is wrong syntax
     */
    @Test
    public void emailIsInvalid(){
        assertFalse(signupActivity.isValidEmail("test@dal"));
    }

    /**
     * Test the password has special character
     * True if the password has special char
     */
    @Test
    public void passwordHasChar(){
        assertTrue(signupActivity.isValidPasswordChar("password@"));
    }

    /**
     * Test the password that do not have special char
     * False if do not have special char
     */
    @Test
    public void passwordDoesNotHaveChar(){
        assertFalse(signupActivity.isValidPasswordChar("password"));
    }

    /**
     * Test if password is below length
     * False for password below length
     */
    @Test
    public void minLengthPasswordCheck(){
        assertFalse(signupActivity.isValidPasswordLen("test"));
    }

    /**
     * Test for password exceed maximum length
     * False on password exceed length
     */
    @Test
    public void maxLengthPasswordCheck(){
        assertFalse(signupActivity.isValidPasswordLen("thisislongerthan20characters"));
    }

    /**
     * Test if password is the correct Regex
     * True on correct password
     */
    @Test
    public void correctLengthPasswordCheck(){
        assertTrue(signupActivity.isValidPasswordLen("thisislongenough"));
    }

}
