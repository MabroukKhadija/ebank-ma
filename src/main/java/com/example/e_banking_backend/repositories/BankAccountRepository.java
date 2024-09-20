package com.example.e_banking_backend.repositories;

import com.example.e_banking_backend.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface BankAccountRepository extends JpaRepository<BankAccount, String> {
}
