package com.example.campuscohort09

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.campuscohort09.ui.theme.CampusCohort09Theme
import java.text.DateFormat
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Calendar
import java.util.Currency
import java.util.Date

// Holds the data for one campus resource
data class CampusResource(
    @param:StringRes val nameRes: Int,
    @param:StringRes val categoryRes: Int,
    val openHour: Int,          // 24-hour clock, e.g. 9 = 9:00 AM
    val closeHour: Int,         // e.g. 17 = 5:00 PM
    val lastUpdated: LocalDate,
    val reviewCount: Int,
    val fee: Double? = null     // null means no fee
)

val campusResources = listOf(
    CampusResource(
        nameRes = R.string.resource_intl_office_name,
        categoryRes = R.string.resource_intl_office_category,
        openHour = 9,
        closeHour = 17,
        lastUpdated = LocalDate.of(2026, 10, 18),
        reviewCount = 1
    ),
    CampusResource(
        nameRes = R.string.resource_library_name,
        categoryRes = R.string.resource_library_category,
        openHour = 7,
        closeHour = 19,
        lastUpdated = LocalDate.of(2026, 10, 15),
        reviewCount = 24,
        fee = 0.10
    ),
    CampusResource(
        nameRes = R.string.resource_veterans_name,
        categoryRes = R.string.resource_veterans_category,
        openHour = 8,
        closeHour = 21,
        lastUpdated = LocalDate.of(2026, 10, 10),
        reviewCount = 8
    )
)

// Turns an hour (like 17) into a Date so DateFormat can format it
fun timeOf(hour: Int): Date {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.HOUR_OF_DAY, hour)
    calendar.set(Calendar.MINUTE, 0)
    return calendar.time
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CampusCohort09Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CampusConnectScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun CampusConnectScreen(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = stringResource(R.string.app_title),
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = stringResource(R.string.app_subtitles),
                style = MaterialTheme.typography.bodyLarge
            )
        }

        items(campusResources) { resource ->
            ResourceCard(resource)
        }

        item {
            InfoSection(
                titleRes = R.string.section_orientation_title,
                descriptionRes = R.string.section_orientation_description
            )
        }
    }
}

@Composable
fun ResourceCard(resource: CampusResource, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    // Detect the user's current locale (updates automatically when the device language changes)
    val locale = LocalConfiguration.current.locales[0]

    // DATE FORMAT (java.time)
    val dateFormatter = DateTimeFormatter
        .ofLocalizedDate(FormatStyle.FULL)
        .withLocale(locale)

    // TIME FORMAT (java.text.DateFormat)
    val timeFormatter = DateFormat.getTimeInstance(DateFormat.SHORT, locale)

    // CURRENCY FORMAT (java.text.NumberFormat)
    val currencyFormatter = NumberFormat.getCurrencyInstance(locale).apply {
        currency = Currency.getInstance("USD") // the fee is charged in U.S. dollars
    }

    val name = stringResource(resource.nameRes)
    val category = stringResource(resource.categoryRes)

    val hours = stringResource(
        R.string.hours_label,
        timeFormatter.format(timeOf(resource.openHour)),
        timeFormatter.format(timeOf(resource.closeHour))
    )

    // PLURALS
    val reviewText = context.resources.getQuantityString(
        R.plurals.review_count,
        resource.reviewCount,   // decides which plural form to use
        resource.reviewCount    // fills in %1$d
    )

    val feeText = resource.fee?.let { fee ->
        stringResource(R.string.fee_label, currencyFormatter.format(fee))
    }

    val lastUpdated = stringResource(
        R.string.last_update_label,
        resource.lastUpdated.format(dateFormatter)
    )

    Card(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = category,
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = hours,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = reviewText,
                style = MaterialTheme.typography.bodyMedium
            )
            if (feeText != null) {
                Text(
                    text = feeText,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Text(
                text = lastUpdated,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun InfoSection(
    @StringRes titleRes: Int,
    @StringRes descriptionRes: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = stringResource(titleRes),
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = stringResource(descriptionRes),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CampusConnectPreview() {
    CampusCohort09Theme {
        CampusConnectScreen()
    }
}