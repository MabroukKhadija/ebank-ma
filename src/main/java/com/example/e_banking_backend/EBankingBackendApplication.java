package com.example.e_banking_backend;

import com.example.e_banking_backend.entities.*;
import com.example.e_banking_backend.enums.AccountStatus;
import com.example.e_banking_backend.enums.OperationType;
import com.example.e_banking_backend.exceptions.BalanceNotSufficentException;
import com.example.e_banking_backend.exceptions.BankAccountNotException;
import com.example.e_banking_backend.exceptions.CustomerNotFoundException;
import com.example.e_banking_backend.repositories.AccountOperationRepository;
import com.example.e_banking_backend.repositories.BankAccountRepository;
import com.example.e_banking_backend.repositories.CustomerRepository;
import com.example.e_banking_backend.services.BankAccountService;
import com.example.e_banking_backend.services.BankService;
import jdk.swing.interop.SwingInterOpUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EBankingBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(EBankingBackendApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(BankAccountService bankAccountService) {
		return args -> {
		Stream.of("Hassan","Imane","Mohamed").forEach(name -> {
				Customer customer = new Customer();
				customer.setName(name);
				customer.setEmail(name+"@gmail.com");
			bankAccountService.saveCustomer(customer);

		});
		bankAccountService.listCustomers().forEach(customer ->{
            try {

                bankAccountService.saveCurrentBankAccount(Math.random()*90000,9000,customer.getId());
            	bankAccountService.saveSavingBankAccount(Math.random()*120000,5.5,customer.getId());
				List<BankAccount> bankAccounts= bankAccountService.bankAccountList();
				for(BankAccount bankAccount : bankAccounts) {
					for (int i = 0; i < 10; i++) {
						bankAccountService.credit(bankAccount.getId(), 10000 + Math.random() * 120000, "Credit");
						bankAccountService.debit(bankAccount.getId(), 1000 + Math.random() * 9000, "Debit");
					}

				}

			} catch (CustomerNotFoundException e) {
                e.printStackTrace();

		} catch (BankAccountNotException | BalanceNotSufficentException e) {
				e.printStackTrace();
            }
        });

		};
	}
	//@Bean
	CommandLineRunner start(CustomerRepository customerRepository,
							BankAccountRepository bankAccountRepository,
							AccountOperationRepository accountOperationRepository
	) {
		return args -> {
			Stream.of("Hassan", "yassine", "Aicha").forEach(name -> {
				Customer customer = new Customer();
				customer.setName(name);
				customer.setEmail(name + "@gamil.com");
				customerRepository.save(customer);
			});

			customerRepository.findAll().forEach(cust -> {
				CurrentAccount currentAccount = new CurrentAccount();
				currentAccount.setId(UUID.randomUUID().toString());
				currentAccount.setBalance(Math.random() * 90000);
				currentAccount.setCreatedAt(new Date());
				currentAccount.setStatus(AccountStatus.CREATED);
				currentAccount.setCustomer(cust);
				currentAccount.setOverDraft(9000);
				bankAccountRepository.save(currentAccount);


				SavingAccount savingAccount = new SavingAccount();
				savingAccount.setId(UUID.randomUUID().toString());
				savingAccount.setBalance(Math.random() * 90000);
				savingAccount.setCreatedAt(new Date());
				savingAccount.setStatus(AccountStatus.CREATED);
				savingAccount.setCustomer(cust);
				savingAccount.setInterestRate(5.5);
				bankAccountRepository.save(savingAccount);

			});
			bankAccountRepository.findAll().forEach(acc -> {
				for (int i = 0; i < 10; i++) {
					AccountOperation accountOperation = new AccountOperation();
					accountOperation.setOperationDate(new Date());
					accountOperation.setAmount(Math.random() * 12000);
					accountOperation.setType(Math.random() > 0.5 ? OperationType.DEBIT : OperationType.CREDIT);
					accountOperation.setBankAccount(acc);
					accountOperationRepository.save(accountOperation);

				}

			});


		};

	}
}
