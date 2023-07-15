package com.example.bankofwords.objects;

import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class User {
    private String username;
    private String email;
    private LocalDateTime joinDate;

    public String getFormattedJoinDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d'" + getDayOfMonthSuffix(joinDate.getDayOfMonth()) + "', yyyy");
        return joinDate.format(formatter);
    }

    private String getDayOfMonthSuffix(final int n) {
        if (n >= 11 && n <= 13) {
            return "th";
        }
        return switch (n % 10) {
            case 1 -> "st";
            case 2 -> "nd";
            case 3 -> "rd";
            default -> "th";
        };
    }
}