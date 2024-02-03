package org.iffat.models;

import org.iffat.exceptions.InsufficientMoneyException;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AccountTest {
    Account account;

    @BeforeEach
    void setUp() {
        account = new Account("Iffat", new BigDecimal("1000.12345"));
        System.out.println("Before each...");
    }

    @AfterEach
    void tearDown() {
        System.out.println("After each");
    }

    @BeforeAll
    static void beforeAll() {
        System.out.println("Before All");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("After All");
    }

    @Test
    @DisplayName("testing the name of the checking account")
    void testAccountName() {
        account = new Account("Iffat", new BigDecimal("1000.12345"));
//        account.setPerson("Iffat");
        String expected = "Iffat";
        String actual = account.getPerson();
        assertNotNull(actual, "The account cannot be null");
        assertEquals(expected, actual, "The account name is not what was expected: was expected " + expected + " however it was " + actual);
        assertTrue(actual.equals("Iffat"), "expected account name must be equal to the actual");
    }

    @Test
    @DisplayName("testing the current account balance, which is not null, greater than zero, expected value.")
    void testAccountBalance() {
        account = new Account("Iffat", new BigDecimal("1000.12345"));
        assertEquals(1000.12345, account.getBalance().doubleValue());
        assertFalse(account.getBalance().compareTo(BigDecimal.ZERO) < 0);
        assertTrue(account.getBalance().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    @DisplayName("testing references that are equal with the equals method.")
    void testAccountReference() {
        account = new Account("John doe", new BigDecimal("8000.9996"));
        Account account2 = new Account("John doe", new BigDecimal("8000.9996"));
//        assertNotEquals(account2, account);
        assertEquals(account2, account);
    }

    @Test
    void testAccountDebit() {
        account = new Account("Iffat", new BigDecimal("1000.12345"));
        account.debit(new BigDecimal(100));
        assertNotNull(account.getBalance());
        assertEquals(900, account.getBalance().intValue());
        assertEquals("900.12345", account.getBalance().toPlainString());
    }

    @Test
    void testAccountCredit() {
        account = new Account("Iffat", new BigDecimal("1000.12345"));
        account.credit(new BigDecimal(100));
        assertNotNull(account.getBalance());
        assertEquals(1100, account.getBalance().intValue());
        assertEquals("1100.12345", account.getBalance().toPlainString());
    }

    @Test
    void testAccountInsufficientMoneyException() {
        account = new Account("Iffat", new BigDecimal("1000.12345"));
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
    @Disabled
    @DisplayName("Testing relationship bank and account with assertAll")
    void testRelationBankAccount() {
        fail();
        Account account1 = new Account("Iffat", new BigDecimal("2500"));
        Account account2 = new Account("John Doe", new BigDecimal("1500.12345"));

        Bank bank = new Bank();
        bank.addAccount(account1);
        bank.addAccount(account2);
        bank.setName("BRI");
        bank.transfer(account2, account1, new BigDecimal(500));

        assertAll(() -> assertEquals("1000.12345", account2.getBalance().toPlainString()),
                () -> assertEquals("3000", account1.getBalance().toPlainString()),
                () -> assertEquals(2, bank.getAccounts().size()),
                () -> assertEquals("BRI", account1.getBank().getName()),
                () -> assertEquals("Iffat", bank.getAccounts().stream()
                        .filter(account -> account.getPerson().equals("Iffat"))
                        .findFirst().get().getPerson()),
                () -> assertTrue(bank.getAccounts().stream()
                        .anyMatch(account -> account.getPerson().equals("Iffat"))));
    }
}