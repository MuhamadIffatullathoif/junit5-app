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

    @Test
    void testAccountBalance() {
        Account account = new Account("Iffat", new BigDecimal("1000.12345"));
        assertEquals(1000.12345, account.getBalance().doubleValue());
        assertFalse(account.getBalance().compareTo(BigDecimal.ZERO) < 0);
        assertTrue(account.getBalance().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    void testAccountReference() {
        Account account = new Account("John doe", new BigDecimal("8000.9996"));
        Account account2 = new Account("John doe", new BigDecimal("8000.9996"));
//        assertNotEquals(account2, account);
        assertEquals(account2, account);
    }
}