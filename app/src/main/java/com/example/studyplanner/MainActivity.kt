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
    // Runs when the activity starts and sets up the app's screen.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Lets the screen extend behind the system bars.
        enableEdgeToEdge()
        // Displays our screen using the app's Material theme.
        setContent {
            MaterialTheme {
                StudyPlannerScreen()
            }
        }
    }
}

// Builds the screen for adding tasks and marking them complete.
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

    // Provides the screen layout and padding around system bars.
    Scaffold { screenPadding ->
        // Places the screen's contents vertically with extra spacing.
        Column(
            modifier = Modifier.fillMaxSize().padding(screenPadding).padding(16.dp)
        ) {
            // Shows the heading and adds a gap below it.
            Text("Study Planner", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))

            // Displays the title input and remembers each text change.
            OutlinedTextField(
                value = taskTitle,
                onValueChange = { taskTitle = it },
                label = { Text("Task title") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            // Adds the typed task when the button is pressed.
            Button(
                onClick = {
                    // Removes spaces at the ends, then clears the input.
                    val title = taskTitle.trim()
                    taskTitle = ""
                    // Starts a coroutine to save the new task.
                    scope.launch {
                        storage.addTask(title)
                    }
                },
                // Prevents adding an empty or spaces-only title.
                enabled = taskTitle.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add task")
            }

            // Adds a gap and shows the list heading.
            Spacer(modifier = Modifier.height(16.dp))
            Text("Tasks", style = MaterialTheme.typography.titleMedium)

            // Shows the tasks in a scrolling list.
            LazyColumn(modifier = Modifier.weight(1f)) {
                // Gives us each task and its position in the list.
                itemsIndexed(tasks) { index, task ->
                    // Places the checkbox and task title side by side.
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
