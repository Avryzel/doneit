package id.jel.doneit.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import id.jel.doneit.data.local.Task
import id.jel.doneit.worker.NotificationWorker
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: TaskViewModel) {
    val tasks by viewModel.taskListState.collectAsState()
    var taskTitle by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("DoneIt Tasks") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = taskTitle,
                    onValueChange = { taskTitle = it },
                    label = { Text("What needs to be done?") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = {
                    if (taskTitle.isNotBlank()) {
                        val oneMinute = 60 * 1000L
                        val deadlineTime = System.currentTimeMillis() + oneMinute

                        viewModel.addNewTask(taskTitle, deadlineTime)

                        val workRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
                            .setInitialDelay(1, TimeUnit.MINUTES)
                            .setInputData(workDataOf("TASK_TITLE" to taskTitle))
                            .build()

                        WorkManager.getInstance(context).enqueue(workRequest)

                        taskTitle = ""
                        keyboardController?.hide()
                    }
                }) {
                    Text("Add")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (tasks.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No tasks yet! Time to be productive. 🚀",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(tasks, key = { it.id }) { task ->
                        TaskItem(
                            task = task,
                            onDelete = { viewModel.deleteTask(task) },
                            onStatusChange = { isChecked ->
                                viewModel.updateTaskStatus(task, isChecked)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TaskItem(
    task: Task,
    onDelete: () -> Unit,
    onStatusChange: (Boolean) -> Unit
) {
    val date = Date(task.deadline)
    val formatter = SimpleDateFormat("MMM dd, yyyy - HH:mm", Locale.getDefault())
    val formattedDate = formatter.format(date)

    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.status,
                onCheckedChange = onStatusChange
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(text = task.title, style = MaterialTheme.typography.bodyLarge)

                Text(
                    text = "Created: $formattedDate",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }
}