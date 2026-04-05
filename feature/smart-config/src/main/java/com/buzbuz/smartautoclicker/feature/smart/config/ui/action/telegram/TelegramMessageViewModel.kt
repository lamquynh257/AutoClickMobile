/*
 * Copyright (C) 2025 Kevin Buzeau
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.buzbuz.smartautoclicker.feature.smart.config.ui.action.telegram

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buzbuz.smartautoclicker.core.common.actions.text.appendCounterReference

import com.buzbuz.smartautoclicker.core.domain.model.action.TelegramMessage
import com.buzbuz.smartautoclicker.feature.smart.config.domain.EditionRepository

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.take

import javax.inject.Inject


class TelegramMessageViewModel @Inject constructor(
    private val editionRepository: EditionRepository,
) : ViewModel()  {

    /** The action being configured by the user. */
    private val configuredAction = editionRepository.editionState.editedActionState
        .mapNotNull { action -> action.value }
        .filterIsInstance<TelegramMessage>()

    private val editedActionHasChanged: StateFlow<Boolean> =
        editionRepository.editionState.editedActionState
            .map { it.hasChanged }
            .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    /** Tells if the user is currently editing an action. If that's not the case, dialog should be closed. */
    @OptIn(FlowPreview::class)
    val isEditingAction: Flow<Boolean> = editionRepository.isEditingAction
        .distinctUntilChanged()
        .debounce(1000)

    /** The name of the action. */
    val name: Flow<String?> = configuredAction
        .map { it.name }
        .take(1)
    /** Tells if the action name is valid or not. */
    val nameError: Flow<Boolean> = configuredAction.map { it.name?.isEmpty() ?: true }

    /** The text to be sent by the action. */
    val textToSend: Flow<String> = configuredAction
        .map { it.text }
        .take(1)

    val sendScreenshot: Flow<Boolean> = configuredAction
        .map { it.sendScreenshot }
        .take(1)

    val timeoutMs: Flow<Int?> = configuredAction
        .map { it.timeoutMs }
        .take(1)

    /** Tells if the configured action is valid and can be saved. */
    val isValidAction: Flow<Boolean> = editionRepository.editionState.editedActionState
        .map { it.canBeSaved }


    fun hasUnsavedModifications(): Boolean =
        editedActionHasChanged.value

    fun setName(newName: String) {
        editionRepository.editionState.getEditedAction<TelegramMessage>()?.let { action ->
            editionRepository.updateEditedAction(action.copy(name = "" + newName))
        }
    }

    fun setTextToSend(newText: String) {
        editionRepository.editionState.getEditedAction<TelegramMessage>()?.let { action ->
            editionRepository.updateEditedAction(action.copy(text = "" + newText))
        }
    }

    fun setSendScreenshot(enabled: Boolean) {
        editionRepository.editionState.getEditedAction<TelegramMessage>()?.let { action ->
            editionRepository.updateEditedAction(action.copy(sendScreenshot = enabled))
        }
    }

    fun setTimeoutMs(timeout: Int?) {
        editionRepository.editionState.getEditedAction<TelegramMessage>()?.let { action ->
            editionRepository.updateEditedAction(action.copy(timeoutMs = timeout))
        }
    }

    fun appendCounterReferenceToTextToSend(counterName: String) {
        editionRepository.editionState.getEditedAction<TelegramMessage>()?.let { action ->
            editionRepository.updateEditedAction(
                action.copy(text = "" + action.text.appendCounterReference(counterName))
            )
        }
    }
}
