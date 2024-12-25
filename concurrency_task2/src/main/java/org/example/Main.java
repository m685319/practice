package org.example;

import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

class BankAccount {
    private final int accountNumber;
    private BigDecimal balance;
    private final ReentrantLock lock = new ReentrantLock();

    BankAccount(int accountNumber) {
        this.accountNumber = accountNumber;
        this.balance = BigDecimal.ZERO;
    }

    public void deposit(BigDecimal amount) {
        lock.lock();
        try {
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Amount must be positive");
            }
            balance = balance.add(amount);
        } finally {
            lock.unlock();
        }
    }

    public void withdraw(BigDecimal amount) {
        lock.lock();
        try {
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Amount must be positive");
            }
            if (amount.compareTo(balance) > 0) {
                throw new IllegalArgumentException("Amount must be less than balance");
            }
            balance = balance.subtract(amount);
        } finally {
            lock.unlock();
        }
    }

    public BigDecimal getBalance() {
        lock.lock();
        try {
            return balance;
        } finally {
            lock.unlock();
        }
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public ReentrantLock getLock() {
        return lock;
    }
}

class ConcurrentBank {
    private final ConcurrentHashMap<Integer, BankAccount> accounts = new ConcurrentHashMap<>();
    private final AtomicInteger accountNumberGenerator = new AtomicInteger(1);

    public BankAccount createAccount() {
        int accountNumber = accountNumberGenerator.getAndIncrement();
        BankAccount account = new BankAccount(accountNumber);
        accounts.put(accountNumber, account);
        return account;
    }

    public void transfer(int fromAccount, int toAccount, BigDecimal amount) {
        if(fromAccount == toAccount) {
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }
        BankAccount from = accounts.get(fromAccount);
        BankAccount to = accounts.get(toAccount);

        if(from == null || to == null) {
            throw new IllegalArgumentException("One or both accounts do not exist");
        }

        BankAccount firstLock = fromAccount < toAccount ? from : to;
        BankAccount secondLock = fromAccount < toAccount ? to : from;

        firstLock.getLock().lock();
        secondLock.getLock().lock();

        try {
            from.withdraw(amount);
            to.deposit(amount);
        } finally {
            secondLock.getLock().unlock();
            firstLock.getLock().unlock();
        }

    }

    public BigDecimal getTotalBalance() {
        return accounts.values().stream()
                .map(BankAccount::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

class Main {
    public static void main(String[] args) {
        ConcurrentBank bank = new ConcurrentBank();

        BankAccount account1 = bank.createAccount();
        BankAccount account2 = bank.createAccount();

        account1.deposit(new BigDecimal("1000"));
        account2.deposit(new BigDecimal("500"));

        System.out.println("Initial Balances:");
        System.out.println("Account 1: " + account1.getBalance());
        System.out.println("Account 2: " + account2.getBalance());

        bank.transfer(account1.getAccountNumber(), account2.getAccountNumber(), new BigDecimal("200"));

        System.out.println("Balances after transfer:");
        System.out.println("Account 1: " + account1.getBalance());
        System.out.println("Account 2: " + account2.getBalance());

        System.out.println("Total Balance: " + bank.getTotalBalance());
    }
}