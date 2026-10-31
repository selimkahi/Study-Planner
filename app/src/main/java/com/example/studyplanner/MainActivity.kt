package com.example.studyplanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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
    var taskTitle by remember { mutableStateOf("") }
    val tasks = remember { mutableStateListOf<String>() }

    Scaffold { screenPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(screenPadding).padding(16.dp)
        ) {
            Text("Study Planner", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = taskTitle,
                onValueChange = { taskTitle = it },
                label = { Text("Task title") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    tasks.add(taskTitle.trim())
                    taskTitle = ""
                },
                enabled = taskTitle.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add task")
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("Tasks", style = MaterialTheme.typography.titleMedium)

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(tasks) { task ->
                    Text(task, modifier = Modifier.padding(vertical = 8.dp))
                }
            }
        }
    }
}
