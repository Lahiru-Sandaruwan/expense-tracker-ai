package com.lahiru.expense_tracker_ai.model;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "expenses")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Expense {
    @Id
    private String id;
    private String expUserId;
    private String description;
    private double amount;
    private String category;
    private LocalDateTime createdAt;
}

