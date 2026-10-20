package com.example.campuscohort09

import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import  androidx.compose.foundation.lazy.items


// Import Packages
import  androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import  java.text.NumberFormat
import  java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

// What is the Locale?
import  java.util.Locale



data class  CampusResource(
    @param:StringRes val namesRes: Int,
    @param:StringRes val category: Int,
    @param:StringRes val hoursRes: Int,
    val lastUpdate: LocalDate,
    val reviewCount: Int,
    val capacity: Int,
    val fee: Double
)


val sampleResource = listOf(
    CampusResource(
        namesRes = R.string.resource_intl_office_name,
        category = R.string.resource_intl_office_category,
        hoursRes = R.string.resource_intl_office_hours,
        lastUpdate = LocalDate.of(2026,8,5),
        reviewCount = 3,
        capacity = 40,
        fee = 2.99
    ),
    CampusResource(
        namesRes = R.string.resource_library_name,
        category = R.string.resource_library_category,
        hoursRes = R.string.resource_library_hours,
        lastUpdate = LocalDate.of(2026,6,12),
        reviewCount = 13,
        capacity = 20,
        fee = 5.99
    ),
    CampusResource(
        namesRes = R.string.resource_veterans_name,
        category = R.string.resource_veterans_category,
        hoursRes = R.string.resource_veterans_hours,
        lastUpdate = LocalDate.of(2026,2,1),
        reviewCount = 1,
        capacity = 10,
        fee = 0.99
    )
)

class MainActivity: ComponentActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme{
                CampusConnectScreen()
            }
        }
    }
}
@Composable
fun CampusConnectScreen(){
    // Spacing values come from res/values/dimens.xml
    Column(modifier = Modifier.padding(all = dimensionResource(R.dimen.padding_screen))) {
        Text(stringResource(R.string.app_title), style = MaterialTheme.typography.headlineMedium)
        Text(stringResource(id = R.string.app_subtitle), style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_section)))

        Text(
            stringResource(R.string.resources_section_header),
            style=MaterialTheme.typography.titleSmall,
            modifier = Modifier.semantics{
                heading()
                contentDescription = "Resource Header Section"
            }
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_list_header)))

        LazyColumn {
            items(sampleResource){ resource ->
                ResourceCard(resource)
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_card_gap)))
            }
        }


    }
}



//COMPONENT
@Composable
fun ResourceCard(resource: CampusResource){

    val context = LocalContext.current
    val locale = Locale.getDefault()

    // DATE FORMAT
    val dateFullFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).withLocale(locale)
    val dateShortFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(locale)

    // CURRENCY FORMAT
    val currencyFormatter = NumberFormat.getCurrencyInstance(locale)
    val integerFormatter = NumberFormat.getIntegerInstance(locale)


    val name = stringResource(resource.namesRes)
    val  category = stringResource(resource.category)

    val hours = stringResource(
        R.string.hours_label,
        stringResource(resource.hoursRes)
    )

    val reviewText = context.resources.getQuantityString(
        R.plurals.review_count,
        resource.reviewCount,
        resource.reviewCount
    )

    val capacityFormatted = integerFormatter.format(resource.capacity)


    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(dimensionResource(R.dimen.padding_card_inner))) {

            Text(
               name,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                category,
                style = MaterialTheme.typography.bodySmall
            )

            Text(
               hours
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_list_header)))



            Text(stringResource(R.string.capacity_label, capacityFormatted))


            Text(reviewText)

            if (resource.fee > 0.0){
                Text(
                    stringResource(R.string.fee_label ,
                        currencyFormatter.format(resource.fee))
                )
            }


            Text(stringResource(R.string.updated_short_label, resource.lastUpdate.format(dateShortFormatter)))

            Text(stringResource(R.string.last_updated_label, resource.lastUpdate.format(dateFullFormatter)))


        }
    }

}
