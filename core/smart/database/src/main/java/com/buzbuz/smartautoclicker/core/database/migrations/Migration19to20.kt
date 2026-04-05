package com.buzbuz.smartautoclicker.core.database.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

import com.buzbuz.smartautoclicker.core.database.ACTION_TABLE

/** Add telegram message configs to the action_table. */
class Migration19to20 : Migration(19, 20) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE `$ACTION_TABLE` ADD COLUMN `telegram_message_send_screenshot` INTEGER DEFAULT 0")
        db.execSQL("ALTER TABLE `$ACTION_TABLE` ADD COLUMN `telegram_message_timeout_ms` INTEGER")
    }
}
