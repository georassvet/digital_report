package ru.riji.comparator.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
public class NsGroup {
    private String  host;
    private String  namespace;
    private List<String> containers;
}
