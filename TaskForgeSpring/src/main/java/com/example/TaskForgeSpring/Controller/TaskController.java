package com.example.TaskForgeSpring.Controller;

import com.example.TaskForgeSpring.CustomCorsConfigAnnotation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/task")
@CustomCorsConfigAnnotation
public class TaskController {

}
