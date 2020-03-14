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

package com.sparetimedevs.bow.timer

import arrow.fx.IO
import arrow.fx.handleErrorWith
import arrow.fx.redeemWith
import arrow.fx.unsafeRunSync
import com.microsoft.azure.functions.ExecutionContext

fun <E> handleTimer(
    timerInfo: String,
    context: ExecutionContext,
    domainLogic: IO<E, Unit>,
    handleSuccess: (timerInfo: String, context: ExecutionContext) -> IO<Nothing, Unit> =
        ::handleSuccessWithDefaultHandler,
    handleDomainError: (timerInfo: String, context: ExecutionContext, e: E) -> IO<Nothing, Unit> =
        ::handleDomainErrorWithDefaultHandler,
    handleSystemFailure: (timerInfo: String, context: ExecutionContext, throwable: Throwable) -> IO<Nothing, Unit> =
        ::handleSystemFailureWithDefaultHandler
): Unit =
    domainLogic
        .redeemWith(
            { throwable: Throwable ->
                handleSystemFailure(timerInfo, context, throwable)
            },
            { e: E ->
                handleDomainError(timerInfo, context, e)
            },
            { _ ->
                handleSuccess(timerInfo, context)
            }
        )
        .handleErrorWith { handleSystemFailureWithDefaultHandler(timerInfo, context, it) }
        .unsafeRunSync()