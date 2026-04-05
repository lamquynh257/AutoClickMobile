/*
 * Copyright (C) 2025
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
package com.buzbuz.smartautoclicker.core.domain.model.action

import com.buzbuz.smartautoclicker.core.base.identifier.Identifier

/**
 * Action sending a notification message to a Telegram chat.
 *
 * @param id the unique identifier of the action.
 * @param eventId the identifier of the event generating this action.
 * @param name the name of the action.
 * @param priority the order in the action list. Lowest priority will always be executed first.
 * @param text the message content to send to Telegram.
 */
data class TelegramMessage(
    override val id: Identifier = Identifier(),
    override val eventId: Identifier = Identifier(),
    override val name: String? = null,
    override var priority: Int = 0,
    val text: String,
    val sendScreenshot: Boolean = false,
    val timeoutMs: Int? = null,
) : Action() {

    override fun hashCodeNoIds(): Int {
        var result = name?.hashCode() ?: 0
        result = 31 * result + text.hashCode()
        result = 31 * result + sendScreenshot.hashCode()
        result = 31 * result + (timeoutMs ?: 0)
        return result
    }

    override fun isComplete(): Boolean = super.isComplete() && text.isNotEmpty()

    override fun deepCopy(): Action = copy()
}
