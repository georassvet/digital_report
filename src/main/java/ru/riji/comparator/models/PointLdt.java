package ru.riji.comparator.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PointLdt {
    private LocalDateTime x;
    private double y;
}
