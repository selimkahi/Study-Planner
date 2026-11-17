package com.example.studyplanner

import androidx.lifecycle.ViewModelProvider
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertSame
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StudyPlannerViewModelTest {
    @Test
    fun viewModelSurvivesActivityRecreation() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()

        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            instrumentation.waitForIdleSync()
            lateinit var originalViewModel: StudyPlannerViewModel

            scenario.onActivity { activity ->
                originalViewModel = ViewModelProvider(activity)[StudyPlannerViewModel::class.java]
            }

            // Android recreates the Activity during changes such as rotation.
            scenario.recreate()
            instrumentation.waitForIdleSync()

            scenario.onActivity { activity ->
                val restoredViewModel = ViewModelProvider(activity)[StudyPlannerViewModel::class.java]
                assertSame(originalViewModel, restoredViewModel)
            }
        }
    }
}
