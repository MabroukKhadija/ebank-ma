package com.example.e_banking_backend.services;

import com.example.e_banking_backend.entities.BankAccount;
import com.example.e_banking_backend.entities.CurrentAccount;
import com.example.e_banking_backend.entities.SavingAccount;
import com.example.e_banking_backend.repositories.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BankService {
    @Autowired
    private BankAccountRepository bankAccountRepository;
    public void consulter(){
        BankAccount bankAccount =
                bankAccountRepository.findById("05786b43-1b87-4ad1-b8b8-19f5fe8db4e6").orElse(null);
        if (bankAccount != null) {
            System.out.println(bankAccount.getId());
            System.out.println(bankAccount.getBalance());
            System.out.println(bankAccount.getStatus());
            System.out.println(bankAccount.getCreatedAt());
            System.out.println(bankAccount.getCustomer().getName());
            System.out.println(bankAccount.getClass().getSimpleName());
            if (bankAccount instanceof CurrentAccount) {
                System.out.println("Over Draft =>" + ((CurrentAccount) bankAccount).getOverDraft());
            } else if (bankAccount instanceof SavingAccount) {
                System.out.println("Rate=>" + ((SavingAccount) bankAccount).getInterestRate());
            }
            bankAccount.getAccountOperations().forEach(op -> {
                System.out.println(op.getType() + " /t" + op.getOperationDate() + "/t" + op.getAmount());

            });
        }
    }

}
