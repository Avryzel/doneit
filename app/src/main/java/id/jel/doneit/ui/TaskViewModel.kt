package id.jel.doneit.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.jel.doneit.data.local.Task
import id.jel.doneit.data.local.TaskDao
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TaskViewModel(private val taskDao: TaskDao) : ViewModel() {
    val taskListState: StateFlow<List<Task>> = taskDao.getAllTasks()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun addNewTask(title: String, deadline: Long) {
        viewModelScope.launch {
            val newTask = Task(title = title, deadline = deadline)
            taskDao.insert(newTask)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            taskDao.delete(task)
        }
    }
}
