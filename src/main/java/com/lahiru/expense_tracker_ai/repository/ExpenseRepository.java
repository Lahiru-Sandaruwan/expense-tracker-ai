package com.lahiru.expense_tracker_ai.repository;

import com.lahiru.expense_tracker_ai.model.Expense;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepository extends MongoRepository<Expense, String> {
    List<Expense> findByExpUserId(String userId);
}
