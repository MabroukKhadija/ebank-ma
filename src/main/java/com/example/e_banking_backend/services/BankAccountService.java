package com.example.e_banking_backend.services;

import com.example.e_banking_backend.entities.BankAccount;
import com.example.e_banking_backend.entities.CurrentAccount;
import com.example.e_banking_backend.entities.Customer;
import com.example.e_banking_backend.entities.SavingAccount;
import com.example.e_banking_backend.exceptions.BalanceNotSufficentException;
import com.example.e_banking_backend.exceptions.BankAccountNotException;
import com.example.e_banking_backend.exceptions.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {

    Customer saveCustomer(Customer customer);
    CurrentAccount saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException;

    SavingAccount saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException;
    List<Customer> listCustomers();

    BankAccount getBankAccount(String accountId) throws BankAccountNotException;
    void debit(String accountId, double amount, String DESCRIPTION) throws BankAccountNotException, BalanceNotSufficentException;
    void credit(String accountId, double amount, String DESCRIPTION) throws BankAccountNotException;
    void transfer(String accountIdSource, String accountIdDestination,double amount) throws BalanceNotSufficentException, BankAccountNotException;

    List<BankAccount> bankAccountList();
}
