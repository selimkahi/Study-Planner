package com.example.studyplanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                StudyPlannerScreen()
            }
        }
    }
}

@Composable
fun StudyPlannerScreen() {
    // Keeps the typed title while Compose redraws the screen.
    var taskTitle by remember { mutableStateOf("") }
    // Gets the Android context needed by task storage.
    val context = LocalContext.current
    // Keeps one storage object for saving and loading tasks.
    val storage = remember { TaskStorage(context.applicationContext) }
    // Reads saved tasks and updates the screen when they change.
    val tasks by storage.tasks.collectAsState(initial = emptyList())
    // Lets us save data without freezing the screen.
    val scope = rememberCoroutineScope()


    Scaffold { screenPadding ->

        Column(
            modifier = Modifier.fillMaxSize().padding(screenPadding).padding(16.dp)
        ) {
            Text("Study Planner ", style = MaterialTheme.typography.headlineMedium)

            OutlinedTextField(
                value = taskTitle,
                onValueChange = { taskTitle = it },
                label = { Text("Task title") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = {
                    val title = taskTitle.trim()
                    taskTitle = ""
                    scope.launch {
                        storage.addTask(title)
                    }
                },
                enabled = taskTitle.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add task")
            }

            // Shows the tasks in a scrolling list.
            LazyColumn(modifier = Modifier.weight(1f)) {
                itemsIndexed(tasks) { index, task ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Shows whether this task is complete.
                        Checkbox(
                            checked = task.isCompleted,
                            onCheckedChange = { checked ->
                                // Saves this task's new checked or unchecked value.
                                scope.launch {
                                    storage.setCompleted(index, checked)
                                }
                            },
                            // Describes the checkbox for screen readers.
                            modifier = Modifier.semantics {
                                contentDescription = "Complete ${task.title}"
                            }
                        )
                        // Shows the title with a line through it when complete.
                        Text(
                            text = task.title,
                            textDecoration = if (task.isCompleted) {
                                TextDecoration.LineThrough
                            } else {
                                TextDecoration.None
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}
