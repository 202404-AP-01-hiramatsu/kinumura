package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.dto.TodoCreateRequest;
import com.example.demo.entity.Todo;
import com.example.demo.mapper.TodoMapper;

@RestController
public class TodoController {

    private final TodoMapper todoMapper;

    public TodoController(TodoMapper todoMapper) {
        this.todoMapper = todoMapper;
    }

    @GetMapping("/todos")
    public List<Todo> getTodos() {
        return todoMapper.findAll();
    }

    @PostMapping("/todos")
    @ResponseStatus(HttpStatus.CREATED)
    public Todo createTodo(@RequestBody TodoCreateRequest request) {
        String title = StringUtils.hasText(request.getTask()) ? request.getTask() : request.getTitle();
        if (!StringUtils.hasText(title)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "task or title is required");
        }

        Todo todo = new Todo();
        todo.setTitle(title.trim());
        todoMapper.insert(todo);
        return todo;
    }
}
