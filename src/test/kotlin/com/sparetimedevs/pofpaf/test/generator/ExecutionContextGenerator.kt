/*
 * Copyright (c) 2020 sparetimedevs and respective authors and developers.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sparetimedevs.pofpaf.test.generator

import com.microsoft.azure.functions.ExecutionContext
import com.microsoft.azure.functions.TraceContext
import io.kotlintest.properties.Gen
import java.util.logging.Logger

internal class ExecutionContextGenerator : Gen<ExecutionContext> {
    override fun constants(): List<ExecutionContext> = emptyList()
    override fun random(): Sequence<ExecutionContext> = generateSequence {
        ExecutionContextTestImpl(
                Gen.string().random().first(),
                TraceContextGenerator().random().first(),
                Logger.getGlobal(),
                Gen.string().random().first()
        )
    }
}

private class TraceContextGenerator : Gen<TraceContext> {
    override fun constants(): List<TraceContext> = emptyList()
    override fun random(): Sequence<TraceContext> = generateSequence {
        ExecutionTraceContextTestImpl(
                Gen.string().random().first(),
                Gen.string().random().first(),
                Gen.mapOfStringAndStringGenerator().random().first()
        )
    }
}