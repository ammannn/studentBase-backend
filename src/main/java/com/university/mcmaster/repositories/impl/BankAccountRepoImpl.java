package com.university.mcmaster.repositories.impl;

import com.university.mcmaster.models.entities.BankAccount;
import com.university.mcmaster.repositories.BankAccountRepo;
import com.university.mcmaster.repositories.utils.FirebaseUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

@Repository
public class BankAccountRepoImpl extends FirebaseUtils<BankAccount> implements BankAccountRepo {
}
