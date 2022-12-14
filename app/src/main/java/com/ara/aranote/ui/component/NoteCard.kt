package com.ara.aranote.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import com.ara.aranote.domain.entity.Note
import com.ara.aranote.util.HDateTime

@Composable
fun NoteCard(
    modifier: Modifier = Modifier,
    note: Note,
    noteColor: Long,
    onClick: () -> Unit,
) {
    Card(
        modifier = modifier
            .padding(
                bottom = 3.dp,
                top = 3.dp,
                start = 3.dp,
                end = 3.dp
            )
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = 4.dp,
    ) {
        val contentColor = if (Color(noteColor).luminance() > 0.5f) Color.Black else Color.White
        Surface(
            contentColor = contentColor,
            color = Color(noteColor),
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = note.text,
                    style = MaterialTheme.typography.body2.copy(
                        textDirection = TextDirection.Content,
                    ),
                    maxLines = 10,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(7.dp)
                )
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.disabled) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(3.dp)
                            .padding(top = 5.dp),
                    ) {
                        Text(
                            text = HDateTime.gerPrettyDateTime(note.addedDateTime),
                            style = MaterialTheme.typography.caption,
                        )
                        if (note.alarmDateTime != null)
                            Icon(
                                imageVector = Icons.Default.Alarm,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                    }
                }
            }
        }
    }
}
