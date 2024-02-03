package org.iffat.models;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {
    @Test
    void testAccountName() {
        Account account = new Account("Iffat", new BigDecimal("1000.12345"));
//        account.setPerson("Iffat");
        String expected = "Iffat";
        String actual = account.getPerson();
        assertEquals(expected, actual);
        assertTrue(actual.equals("Iffat"));
    }
}