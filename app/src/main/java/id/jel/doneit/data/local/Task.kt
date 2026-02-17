package id.jel.doneit.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "task_title") val title: String,
    @ColumnInfo(name = "task_deadline") val deadline: Long,
    @ColumnInfo(name = "task_status") val status: Boolean = false
)