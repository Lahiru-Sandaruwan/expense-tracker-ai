package com.lahiru.expense_tracker_ai.controller;

import com.lahiru.expense_tracker_ai.model.Expense;
import com.lahiru.expense_tracker_ai.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ExpenseController {

    private final ExpenseService expenseService;

    // POST /api/expenses
    @PostMapping
    public ResponseEntity<Expense> addExpense(@RequestBody Expense expense) throws IOException {
        Expense savedExpense = expenseService.saveExpense(expense);
        return ResponseEntity.ok(savedExpense);
    }

    // GET /api/expenses/{userId}
    @GetMapping("/{userId}")
    public ResponseEntity<List<Expense>> getExpensesByUser(@PathVariable String userId) {
        List<Expense> expenses = expenseService.getAllExpenses(userId);
        return ResponseEntity.ok(expenses);
    }
    // PUT /api/expenses/{expenseId}
    @PutMapping("/{expenseId}")
    public ResponseEntity<Expense> updateExpense(@PathVariable String expenseId, @RequestBody Expense expense) throws IOException {
        Expense updatedExpense = expenseService.updateExpense(expenseId, expense);
        return ResponseEntity.ok(updatedExpense);
    }

    // DELETE /api/expenses/{expenseId}
    @DeleteMapping("/{expenseId}")
    public ResponseEntity<Void> deleteExpense(@PathVariable String expenseId) {
        expenseService.deleteExpense(expenseId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/summary/{year}/{month}")
    public ResponseEntity<Map<String, String>> getMonthlySummary(
            @RequestParam String expUserId,
            @PathVariable int year,
            @PathVariable int month) throws IOException {

        String summary = expenseService.generateMonthlySummary(expUserId, year, month);
        return ResponseEntity.ok(Map.of("summary", summary));
    }

}