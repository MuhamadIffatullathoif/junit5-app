package org.iffat.models;

import org.iffat.exceptions.InsufficientMoneyException;
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

    @Test
    void testAccountDebit() {
        Account account = new Account("Iffat", new BigDecimal("1000.12345"));
        account.debit(new BigDecimal(100));
        assertNotNull(account.getBalance());
        assertEquals(900, account.getBalance().intValue());
        assertEquals("900.12345", account.getBalance().toPlainString());
    }

    @Test
    void testAccountCredit() {
        Account account = new Account("Iffat", new BigDecimal("1000.12345"));
        account.credit(new BigDecimal(100));
        assertNotNull(account.getBalance());
        assertEquals(1100, account.getBalance().intValue());
        assertEquals("1100.12345", account.getBalance().toPlainString());
    }

    @Test
    void testAccountInsufficientMoneyException() {
        Account account = new Account("Iffat", new BigDecimal("1000.12345"));
        Exception exception = assertThrows(InsufficientMoneyException.class, () -> {
            account.debit(new BigDecimal(1500));
        });
        String actual = exception.getMessage();
        String expected = "Insufficient Money";
        assertEquals(expected, actual);
    }

    @Test
    void testAccountTransferMoney() {
        Account account1 = new Account("Iffat", new BigDecimal("2500"));
        Account account2 = new Account("John Doe", new BigDecimal("1500.12345"));

        Bank bank = new Bank();
        bank.setName("BRI");
        bank.transfer(account2, account1, new BigDecimal(500));

        assertEquals("1000.12345", account2.getBalance().toPlainString());
        assertEquals("3000", account1.getBalance().toPlainString());
    }

    @Test
    void testRelationBankAccount() {
        Account account1 = new Account("Iffat", new BigDecimal("2500"));
        Account account2 = new Account("John Doe", new BigDecimal("1500.12345"));

        Bank bank = new Bank();
        bank.addAccount(account1);
        bank.addAccount(account2);
        bank.setName("BRI");
        bank.transfer(account2, account1, new BigDecimal(500));

        assertEquals("1000.12345", account2.getBalance().toPlainString());
        assertEquals("3000", account1.getBalance().toPlainString());

        assertEquals(2, bank.getAccounts().size());
    }
}