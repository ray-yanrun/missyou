package com.lin.missyou.service;

import com.lin.missyou.model.Activity;
import com.lin.missyou.repository.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    @Override
    public Activity getByName(String name) {
        return activityRepository.findByName(name);
    }
}
