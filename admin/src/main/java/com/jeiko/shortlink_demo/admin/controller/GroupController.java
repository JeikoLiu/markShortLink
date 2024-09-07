package com.jeiko.shortlink_demo.admin.controller;

import com.jeiko.shortlink_demo.admin.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

}
