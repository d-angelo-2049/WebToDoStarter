package com.example.demo.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.text.html.Option;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;

import com.example.demo.entity.Task;
import com.example.demo.repository.TaskDao;

@ExtendWith(MockitoExtension.class)
@DisplayName("TaskServiceImplの単体テスト")
class TaskServiceImplUnitTest {


  @Mock
  private TaskDao dao;

  @InjectMocks
  private TaskServiceImpl taskServiceImpl;

  @Test
  @DisplayName("テーブルtaskの全件取得で0件の場合のテスト")
  void testFindAllReturnEmptyList() {

    List<Task> emptyList = new ArrayList<>();
    when(dao.findAll()).thenReturn(emptyList);

    List<Task> actualList = taskServiceImpl.findAll();

    verify(dao, times(1)).findAll();
    assertEquals(0, actualList.size());

  }

  @Test
  @DisplayName("テーブルTaskの全件取得で2件の場合のテスト")
  void testFindAllReturnList() {

    //モックから返すListに2つのTaskオブジェクトをセット
    List<Task> list = new ArrayList<>();
    Task task1 = new Task();
    Task task2 = new Task();
    list.add(task1);
    list.add(task2);

    // モッククラスのI/Oをセット（findAll()の型と異なる戻り値はNG）
    when(dao.findAll()).thenReturn(list);

    // サービスを実行
    List<Task> actualList = taskServiceImpl.findAll();

    // モックの指定メソッドの実行回数を検査
    verify(dao, times(1)).findAll();

    // 戻り値の検査(expected, actual)
    assertEquals(2, actualList.size());

  }

  @Test
  @DisplayName("タスクが取得できない場合のテスト")
  void testGetTaskThrowException() {

    when(dao.findById(0)).thenThrow(new EmptyResultDataAccessException(1));
    try {
      Optional<Task> task0 = taskServiceImpl.getTask(0);

    } catch (TaskNotFoundException e) {
      assertEquals(e.getMessage(), "update error: not found task:" + 0);

    }
  }

  @Test
  @DisplayName("タスクを1件取得した場合のテスト")
  void testGetTaskReturnOne() {

    Task task = new Task();
    Optional<Task> taskOpt = Optional.ofNullable(task);
    when(dao.findById(1)).thenReturn(taskOpt);
    Optional<Task> actual = taskServiceImpl.getTask(1);

    verify(dao, times(1)).findById(1);
    assertTrue(actual.isPresent());
  }

  @Test
  @DisplayName("削除対象が存在しない場合、例外が発生することを確認するテスト")
  void throwNotFoundException() {

    when(dao.deleteById(0)).thenReturn(0);

    try {
      taskServiceImpl.deleteById(0);
    } catch (TaskNotFoundException e) {
      assertEquals(e.getMessage(), "delete error: not found id:" + 0);
    }
  }


}
