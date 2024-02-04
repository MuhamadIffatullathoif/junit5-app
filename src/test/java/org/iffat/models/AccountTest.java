package org.iffat.models;

import org.iffat.exceptions.InsufficientMoneyException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.junit.jupiter.api.Assumptions.assumingThat;

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

    @Nested
    @DisplayName("Testing checking account attributes")
    class AccountBalanceTest {
        @Test
        @DisplayName("name")
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
        @DisplayName("account balance, which is not null, greater than zero, expected value.")
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
    }

    @Nested
    class AccountOperationTest {
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
        void testAccountTransferMoney() {
            Account account1 = new Account("Iffat", new BigDecimal("2500"));
            Account account2 = new Account("John Doe", new BigDecimal("1500.12345"));

            Bank bank = new Bank();
            bank.setName("BRI");
            bank.transfer(account2, account1, new BigDecimal(500));

            assertEquals("1000.12345", account2.getBalance().toPlainString());
            assertEquals("3000", account1.getBalance().toPlainString());
        }
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

    @Nested
    class OperatingSystemTest {
        @Test
        @EnabledOnOs(OS.WINDOWS)
        void testInsideWindows() {

        }

        @Test
        @EnabledOnOs({OS.LINUX, OS.MAC})
        void testInsideLinuxMac() {

        }

        @Test
        @DisabledOnOs(OS.WINDOWS)
        void testNoWindows() {

        }
    }

    @Nested
    class JavaVersionTest {
        @Test
        @EnabledOnJre(JRE.JAVA_8)
        void testInsideJdk8() {

        }

        @Test
        @EnabledOnJre(JRE.JAVA_17)
        void testInsideJdk17() {

        }

        @Test
        @DisabledOnJre(JRE.JAVA_17)
        void testNoJdk17() {

        }
    }

    @Nested
    class SystemPropertiesTest {
        @Test
        void printSystemProperties() {
            Properties properties = System.getProperties();
            properties.forEach((key, value) -> System.out.println(key + ":" + value));
        }

        @Test
        @EnabledIfSystemProperty(named = "java.version", matches = ".*17.*")
        void testJavaVersion() {

        }

        @Test
        @DisabledIfSystemProperty(named = "os.arch", matches = ".*32.*")
        void testInside64() {

        }

        @Test
        @EnabledIfSystemProperty(named = "os.arch", matches = ".*32.*")
        void testNo64() {

        }

        @Test
        @EnabledIfSystemProperty(named = "user.name", matches = "iffat")
        void testUsername() {
        }

        @Test
        @EnabledIfSystemProperty(named = "ENV", matches = "dev")
        void testDev() {

        }
    }

    @Nested
    class VariableEnvironmentTest {
        @Test
        void printVariablesEnvironment() {
            Map<String, String> getenv = System.getenv();
            getenv.forEach((key, value) -> {
                System.out.println(key + ":" + value);
            });
        }

        @Test
        @EnabledIfEnvironmentVariable(named = "JAVA_HOME", matches = ".*jdk17.0.7.*")
        void testJavaHome() {

        }

        @Test
        @EnabledIfEnvironmentVariable(named = "NUMBER_OF_PROCESSORS", matches = "8")
        void testProcessor() {

        }

        @Test
        @EnabledIfEnvironmentVariable(named = "ENVIRONMENT", matches = "dev")
        void testEnv() {

        }

        @Test
        @DisabledIfEnvironmentVariable(named = "ENVIRONMENT", matches = "prod")
        void testEnvProdDisabled() {

        }
    }



    @Test
    @DisplayName("test Account Balance Dev")
    void testAccountBalanceDev() {
        boolean inDev = "dev".equals(System.getProperty("ENV"));
        assumeTrue(inDev);
        account = new Account("Iffat", new BigDecimal("1000.12345"));
        assertEquals(1000.12345, account.getBalance().doubleValue());
        assertFalse(account.getBalance().compareTo(BigDecimal.ZERO) < 0);
        assertTrue(account.getBalance().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    @DisplayName("test Account Balance Dev 2")
    void testAccountBalanceDev2() {
        boolean inDev = "dev".equals(System.getProperty("ENV"));
        assumingThat(inDev, () -> {
            account = new Account("Iffat", new BigDecimal("1000.12345"));
            assertEquals(1000.12345, account.getBalance().doubleValue());
        });
        assertFalse(account.getBalance().compareTo(BigDecimal.ZERO) < 0);
        assertTrue(account.getBalance().compareTo(BigDecimal.ZERO) > 0);
    }

    @DisplayName("test Account Debit Repeat")
    @RepeatedTest(value = 5, name = "{displayName} - Repeat number {currentRepetition} of {totalRepetitions}")
    void testAccountDebitRepeat(RepetitionInfo info) {
        if (info.getCurrentRepetition() == 3) {
            System.out.println("Repetition of " + info.getCurrentRepetition());
        }
        account = new Account("Iffat", new BigDecimal("1000.12345"));
        account.debit(new BigDecimal(100));
        assertNotNull(account.getBalance());
        assertEquals(900, account.getBalance().intValue());
        assertEquals("900.12345", account.getBalance().toPlainString());
    }

    @Nested
    class testParameterizedTests {
        @ParameterizedTest(name = "number {index} executing with courage {0} - {argumentsWithNames}")
        @ValueSource(strings = {"100","200","300","500","700","1000.12345"})
        void testAccountDebitValueSource(String amount) {
            account.debit(new BigDecimal(amount));
            assertNotNull(account.getBalance());
            assertTrue(account.getBalance().compareTo(BigDecimal.ZERO) > 0);
        }

        @ParameterizedTest(name = "number {index} executing with courage {0} - {argumentsWithNames}")
        @CsvSource({"1,100","2,200","3,300","4,500","5,700","6,1000.12345"})
        void testAccountDebitCSVSource(String index, String amount) {
            System.out.println(index + " -> " + amount);
            account.debit(new BigDecimal(amount));
            assertNotNull(account.getBalance());
            assertTrue(account.getBalance().compareTo(BigDecimal.ZERO) > 0);
        }

        @ParameterizedTest(name = "number {index} executing with courage {0} - {argumentsWithNames}")
        @CsvSource({"200,100,Moti,Joni","250,200,Pepe,Pepe","300,300,maria,Maria","510,500,Pepa,Pepa","750,700,Lucas,Luca","1000.12345,1000.12345,Reno,Reno"})
        void testAccountDebitCSVSource2(String balance, String amount, String expected, String actual) {
            System.out.println(balance + " -> " + amount);
            account.setBalance(new BigDecimal(balance));
            account.debit(new BigDecimal(amount));
            account.setPerson(actual);
            assertNotNull(account.getBalance());
            assertEquals(expected, account.getPerson());
            assertTrue(account.getBalance().compareTo(BigDecimal.ZERO) > 0);
        }

        @ParameterizedTest(name = "number {index} executing with courage {0} - {argumentsWithNames}")
        @CsvFileSource(resources = "/data.csv")
        void testAccountDebitCSVFileSource(String amount) {
            account.debit(new BigDecimal(amount));
            assertNotNull(account.getBalance());
            assertTrue(account.getBalance().compareTo(BigDecimal.ZERO) > 0);
        }

        @ParameterizedTest(name = "number {index} executing with courage {0} - {argumentsWithNames}")
        @CsvFileSource(resources = "/data2.csv")
        void testAccountDebitCSVFileSource2(String balance, String amount, String expected, String actual) {
            account.setBalance(new BigDecimal(balance));
            account.debit(new BigDecimal(amount));
            account.setPerson(actual);

            assertNotNull(account.getBalance());
            assertEquals(expected, account.getPerson());
            assertTrue(account.getBalance().compareTo(BigDecimal.ZERO) > 0);

            assertTrue(account.getBalance().compareTo(BigDecimal.ZERO) > 0);
        }
    }

    @ParameterizedTest(name = "number {index} executing with courage {0} - {argumentsWithNames}")
    @MethodSource("amountList")
    void testAccountDebitMethodSource(String amount) {
        account.debit(new BigDecimal(amount));
        assertNotNull(account.getBalance());
        assertTrue(account.getBalance().compareTo(BigDecimal.ZERO) > 0);
    }

    static List<String> amountList() {
        return Arrays.asList("100","200","300","500","700","1000.12345");
    }
}