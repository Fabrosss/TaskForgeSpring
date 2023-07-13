package com.example.TaskForgeSpring.model.DTO;

public record TaskDTO(
        long id,
        String topic,
        String description,
        long hours,
        java.time.LocalDate startingDate,
        java.time.LocalDate endingDate) {
}
