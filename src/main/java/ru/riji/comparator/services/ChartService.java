package ru.riji.comparator.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.riji.comparator.dao.ChartDao;
import ru.riji.comparator.dto.ChartDto;
import ru.riji.comparator.dto.TestDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChartService {

    @Autowired
    private ChartDao chartDao;

    public List<ChartDto> getChartsByProjectId(int projectId) {
        return chartDao.getByProjectId(projectId).stream().map(ChartDto::new).collect(Collectors.toList());
    }
}
