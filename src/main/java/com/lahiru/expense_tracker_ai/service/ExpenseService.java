package com.lahiru.expense_tracker_ai.service;

import com.lahiru.expense_tracker_ai.model.Expense;
import com.lahiru.expense_tracker_ai.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final OpenAIService openAIService;

    public Expense saveExpense(Expense expense) throws IOException {
        if (expense.getDescription() != null && (expense.getCategory() == null || expense.getCategory().isEmpty())) {
            String predictedCategory = openAIService.getCategoryFromGPT(expense.getDescription());
            expense.setCategory(predictedCategory);
        }

        expense.setCreatedAt(LocalDateTime.now());
        return expenseRepository.save(expense);
    }

    public List<Expense> getAllExpenses(String userId) {
        return expenseRepository.findByExpUserId(userId);
    }

    public Expense updateExpense(String expenseId, Expense updatedExpense) throws IOException {
        Expense existing = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new RuntimeException("Expense not found with ID: " + expenseId));

        existing.setDescription(updatedExpense.getDescription());
        existing.setAmount(updatedExpense.getAmount());
        //existing.setDate(updatedExpense.getDate());

        if (updatedExpense.getCategory() == null || updatedExpense.getCategory().isEmpty()) {
            String predictedCategory = openAIService.getCategoryFromGPT(updatedExpense.getDescription());
            existing.setCategory(predictedCategory);
        } else {
            existing.setCategory(updatedExpense.getCategory());
        }

        return expenseRepository.save(existing);
    }

    public void deleteExpense(String expenseId) {
        if (!expenseRepository.existsById(expenseId)) {
            throw new RuntimeException("Expense not found with ID: " + expenseId);
        }
        expenseRepository.deleteById(expenseId);
    }

    public String generateMonthlySummary(String expUserId, int year, int month) throws IOException {
        LocalDateTime start = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime end = start.plusMonths(1);

        List<Expense> monthlyExpenses = expenseRepository.findByExpUserId("user123").stream()
                .filter(exp -> exp.getCreatedAt().isAfter(start.minusNanos(1)) && exp.getCreatedAt().isBefore(end))
                .toList();

        if (monthlyExpenses.isEmpty()) {
            return "No expenses found for " + year + "-" + String.format("%02d", month) + ".";
        }

        StringBuilder prompt = new StringBuilder("Summarize the following expenses in a short paragraph: ");
        prompt.append("Only include key insights such as total spent, top categories, and biggest expense. List:\n");

        for (Expense exp : monthlyExpenses) {
            prompt.append(String.format("- %s: %.2f LKR on %s\n",
                    exp.getDescription(), exp.getAmount(), exp.getCategory()));
        }

        return openAIService.getSummaryFromGPT(prompt.toString());
    }

}
