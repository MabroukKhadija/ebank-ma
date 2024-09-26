package com.example.e_banking_backend.services;

import com.example.e_banking_backend.entities.*;
import com.example.e_banking_backend.enums.OperationType;
import com.example.e_banking_backend.exceptions.BalanceNotSufficentException;
import com.example.e_banking_backend.exceptions.BankAccountNotException;
import com.example.e_banking_backend.exceptions.CustomerNotFoundException;
import com.example.e_banking_backend.repositories.AccountOperationRepository;
import com.example.e_banking_backend.repositories.BankAccountRepository;
import com.example.e_banking_backend.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class BankAccountServiceImpl implements BankAccountService {

    private final CustomerRepository customerRepository;
    private final BankAccountRepository bankAccountRepository;
    private final AccountOperationRepository accountOperationRepository;



    @Override
    public Customer saveCustomer(Customer customer) {
        log.info("saving new Customer");
            return customerRepository.save(customer);
    }

    @Override
    public CurrentAccount saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if(customer==null)
            throw new CustomerNotFoundException("Customer not found");

        CurrentAccount currentAccount= new CurrentAccount();

        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreatedAt(new Date());
        currentAccount.setBalance(initialBalance);
        currentAccount .setCustomer(customer);
        currentAccount.setOverDraft(overDraft);
        CurrentAccount savedBankAccount = bankAccountRepository.save(currentAccount);
        return savedBankAccount;
    }

    @Override
    public SavingAccount saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if(customer==null)
            throw new CustomerNotFoundException("Customer not found");

        SavingAccount savingAccount= new SavingAccount();

        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreatedAt(new Date());
        savingAccount.setBalance(initialBalance);
        savingAccount .setCustomer(customer);
        savingAccount.setInterestRate(interestRate);
        SavingAccount savedBankAccount = bankAccountRepository.save(savingAccount);
        return savedBankAccount;
    }


    @Override
    public List<Customer> listCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public BankAccount getBankAccount(String accountId) throws BankAccountNotException {
       BankAccount bankAccount=bankAccountRepository.findById(accountId)
               .orElseThrow(()->new BankAccountNotException("BankAccount not found"));

        return bankAccount;
    }

    @Override
    public void debit(String accountId, double amount, String DESCRIPTION) throws BankAccountNotException, BalanceNotSufficentException {
    BankAccount bankAccount=getBankAccount(accountId);
    if(bankAccount.getBalance()<amount)
        throw new BalanceNotSufficentException("Balance not sufficent");
        AccountOperation accountOperation=new AccountOperation();
        accountOperation.setType(OperationType. DEBIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(DESCRIPTION);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance()-amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void credit(String accountId, double amount, String DESCRIPTION) throws BankAccountNotException {
        BankAccount bankAccount=getBankAccount(accountId);

        AccountOperation accountOperation=new AccountOperation();
        accountOperation.setType(OperationType. CREDIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(DESCRIPTION);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance()+amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount) throws BalanceNotSufficentException, BankAccountNotException {
        debit(accountIdSource,amount,"Transfer to "+accountIdDestination);
        credit(accountIdDestination,amount,"Transfer from "+accountIdSource);

    }
    @Override
    public List<BankAccount> bankAccountList(){
        return bankAccountRepository.findAll();
    }
}
