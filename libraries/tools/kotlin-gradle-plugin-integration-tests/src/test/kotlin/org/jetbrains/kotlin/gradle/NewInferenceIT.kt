/*
 * Copyright 2010-2018 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license
 * that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.gradle

import org.jetbrains.kotlin.gradle.util.getFileByName
import org.jetbrains.kotlin.gradle.util.modify
import org.junit.Test
import java.io.File

class NewInferenceIT : BaseGradleIT() {
    companion object {
        private const val LOCAL_PROPERTIES = "local.properties"
        private const val GRADLE_PROPERTIES = "gradle.properties"
    }

    @Test
    fun testNewInferenceProjectDSL() {
        val project = Project("newInferenceProjectDSL")
        project.build("assemble") {
            assertSuccessful()
            assertContains("args.newInferenceState=enable")
        }

        project.projectDir.getFileByName("build.gradle").modify {
            it.replace("newInference 'enable'", "newInference 'disable'")
        }

        project.build("assemble") {
            assertFailed()
            assertContains("args.newInferenceState=disable")
        }
    }

    @Test
    fun testNewInferenceEnabledGradleProperties() {
        jvmProject.doTest("enable", GRADLE_PROPERTIES)
    }

    @Test
    fun testNewInferenceJsEnabledGradleProperties() {
        jsProject.doTest("enable", GRADLE_PROPERTIES)
    }

    @Test
    fun testNewInferenceDisabledGradleProperties() {
        jvmProject.doTest("disable", GRADLE_PROPERTIES)
    }

    @Test
    fun testNewInferenceJsDisabledGradleProperties() {
        jsProject.doTest("disable", GRADLE_PROPERTIES)
    }

    @Test
    fun testNewInferenceDefault() {
        jvmProject.doTest("disable", null)
    }

    @Test
    fun testNewInferenceJsDefault() {
        jsProject.doTest("disable", null)
    }

    @Test
    fun testNewInferenceEnabledLocalProperties() {
        jvmProject.doTest("enable", LOCAL_PROPERTIES)
    }

    @Test
    fun testNewInferenceJsEnabledLocalProperties() {
        jsProject.doTest("enable", LOCAL_PROPERTIES)
    }

    @Test
    fun testNewInferenceDisabledLocalProperties() {
        jvmProject.doTest("disable", LOCAL_PROPERTIES)
    }

    @Test
    fun testNewInferenceJsDisabledLocalProperties() {
        jsProject.doTest("disable", LOCAL_PROPERTIES)
    }

    private val jvmProject: Project
        get() = Project("kotlinProject")

    private val jsProject: Project
        get() = Project("kotlin2JsProject")

    private fun Project.doTest(inferenceState: String, propertyFileName: String?) {
        if (propertyFileName != null) {
            setupWorkingDir()
            val propertyFile = File(projectDir, propertyFileName)
            val newInferenceProperty = "kotlin.newInference=$inferenceState"
            propertyFile.writeText(newInferenceProperty)
        }

        build("build") {
            assertContains("args.newInferenceState=$inferenceState")
        }
    }
}