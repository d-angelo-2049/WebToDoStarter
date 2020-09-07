package com.example.demo.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.example.demo.entity.Task;

@SpringJUnitConfig
@SpringBootTest
@ActiveProfiles("unit")
@DisplayName("TaskServiceImplの結合テスト")
class TaskServiceImplTest {

  @Autowired
  private TaskService taskService;

  @Test
  @DisplayName("タスクが取得できない場合のテスト")
  void testGetTaskFormReturnNull() {

    try {
      Optional<Task> task = taskService.getTask(0);
    } catch (TaskNotFoundException e) {
      assertEquals(e.getMessage(), "update error: not found task:0");
    }
  }

  @Test
  @DisplayName("全件検索のテスト")
  void testFindAllCheckCount() {
    List<Task> taskList = taskService.findAll();
    assertEquals(taskList.size(), 2);

  }


  @Test
  @DisplayName("1件のタスクが取得できた場合のテスト")
  void testGetTaskFormReturnOne() {
    Optional<Task> taskOpt = taskService.getTask(1);
    Task task = taskOpt.get();
    assertEquals("JUnitを学習", task.getTitle());
  }


}