package mobi.appcent.kalender

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Creates a fully customizable calendar with options to modify its visual appearance and style.
 *
 *@param dayAbbrList Day abbreviations from Monday to Sunday. e.g. ["Mon","Tues","Wed","Thu","Fri","Sat","Sun"].
 *@param dayAbbrBackgroundColor Background color of day abbreviations.
 *@param dayAbbrTextStyle Text style of day abbreviations.
 *@param dayAbbrBorderWidth Border width of day abbreviations. Set null to turn off this feature.
 *@param dayAbbrBorderColor Border color of day abbreviations.
 *@param monthTextStyle Style of the month text.
 *@param yearTextStyle Style of the year text.
 *@param backMonthArrow Drawable source for the back button.
 *@param nextMonthArrow Drawable source for the next button.
 *@param activeDayItemTextStyle Text style of day items for the current month.
 *@param passiveDayItemTextStyle Text style of day items for previous and next months.
 *@param dayItemPaddingValues Container padding of day items.
 *@param dayItemContainerColor Background color of day items.
 *@param dayItemShape Shape of day items.
 *@param dayItemBackground Drawable background source of day items. Set null to turn off this feature.
 *@param dayItemBackgroundContentScale Background scaling mode of day items.
 *@param dayItemBorderColor Border color of day items.
 *@param dayItemBorderWidth Border width of day items. Set null to turn off this feature.
 *@param calendarBackground Calendar background image. Set null to turn off this feature.
 *@param calendarBackgroundScale Background image scaling mode.
 *@param monthsVisible Visibility of the months section.
 */
@Composable
fun Kalender(
    dayAbbrList: List<String> = listOf("Mon","Tues","Wed","Thu","Fri","Sat","Sun"),
    dayAbbrBackgroundColor: Color = Color.DarkGray,
    dayAbbrTextStyle: TextStyle = MaterialTheme.typography.bodyMedium.copy(color = Color.White),
    dayAbbrBorderWidth: Dp? = 1.dp,
    dayAbbrBorderColor: Color = Color.White,
    monthTextStyle: TextStyle = MaterialTheme.typography.titleMedium,
    yearTextStyle: TextStyle = MaterialTheme.typography.titleMedium,
    backMonthArrow: Int = R.drawable.arrow_back_action,
    nextMonthArrow: Int = R.drawable.arrow_next_action,
    activeDayItemTextStyle: TextStyle = MaterialTheme.typography.titleSmall.copy(color = Color.White),
    passiveDayItemTextStyle: TextStyle = MaterialTheme.typography.titleSmall.copy(color = Color.DarkGray),
    dayItemPaddingValues: PaddingValues = PaddingValues(1.dp),
    dayItemContainerColor: Color = Color.LightGray,
    dayItemShape: Shape = MaterialTheme.shapes.small,
    @DrawableRes dayItemBackground: Int? = null,
    dayItemBackgroundContentScale: ContentScale = ContentScale.Crop,
    dayItemBorderColor: Color = Color.Black,
    dayItemBorderWidth: Dp? = null,
    @DrawableRes calendarBackground: Int? = null,
    calendarBackgroundScale: ContentScale = ContentScale.Crop,
    monthsVisible: Boolean = true
) {

    val kalenderData = remember { KalenderData() }
    val kalenderValues = kalenderData.kalenderValues.collectAsState()

    Column {
        Box {
            if (calendarBackground != null) {
                Image(
                    painter = painterResource(id = calendarBackground),
                    contentDescription = null,
                    contentScale = calendarBackgroundScale
                )
            }
            Column {
                if (monthsVisible) {
                    KalenderMonthsHeader(
                        monthTextStyle = monthTextStyle,
                        yearTextStyle = yearTextStyle,
                        backMonthArrow = backMonthArrow,
                        nextMonthArrow = nextMonthArrow,
                        kalenderData = kalenderData,
                        kalenderValues = kalenderValues
                    )
                }
                KalenderDayAbbreviations(
                    dayAbbrList = dayAbbrList,
                    backgroundColor = dayAbbrBackgroundColor,
                    textStyle = dayAbbrTextStyle,
                    borderWidth = dayAbbrBorderWidth,
                    borderColor = dayAbbrBorderColor
                )
                KalenderGrid(
                    activeTextStyle = activeDayItemTextStyle,
                    passiveTextStyle = passiveDayItemTextStyle,
                    paddingValues = dayItemPaddingValues,
                    containerColor = dayItemContainerColor,
                    shape = dayItemShape,
                    background = dayItemBackground,
                    backgroundContentScale = dayItemBackgroundContentScale,
                    borderColor = dayItemBorderColor,
                    borderWidth = dayItemBorderWidth,
                    kalenderValues = kalenderValues
                )
            }
        }
    }
}

@Composable
fun KalenderGrid(
    activeTextStyle: TextStyle,
    passiveTextStyle: TextStyle,
    paddingValues: PaddingValues,
    containerColor: Color,
    shape: Shape,
    @DrawableRes background: Int?,
    backgroundContentScale: ContentScale,
    borderColor: Color,
    borderWidth: Dp?,
    kalenderValues: State<KalenderValues?>
) {
    val days = kalenderValues.value?.days
    LazyVerticalGrid(columns = GridCells.Fixed(7)) {
        items(days?.size ?: 42) {
            val item = days?.get(it)
            KalenderItem(
                day = item?.day ?: 1,
                textStyle = if (item?.isActive==true) activeTextStyle else passiveTextStyle,
                paddingValues = paddingValues,
                containerColor = containerColor,
                shape = shape,
                background = background,
                backgroundContentScale = backgroundContentScale,
                borderColor = borderColor,
                borderWidth = borderWidth,
            )
        }
    }
}

@Composable
fun KalenderDayAbbreviations(
    dayAbbrList: List<String>,
    backgroundColor: Color,
    textStyle: TextStyle,
    borderWidth: Dp?,
    borderColor: Color
) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        dayAbbrList.forEach { day ->
            KalenderDayAbbreviationItem(
                dayAbbr = day,
                modifier = Modifier.weight(1f),
                backgroundColor = backgroundColor,
                textStyle = textStyle,
                borderWidth = borderWidth,
                borderColor = borderColor
            )
        }
    }
}

@Composable
fun KalenderMonthsHeader(
    monthTextStyle: TextStyle,
    yearTextStyle: TextStyle,
    backMonthArrow: Int,
    nextMonthArrow: Int,
    kalenderData: KalenderData,
    kalenderValues: State<KalenderValues?>
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = backMonthArrow),
            contentDescription = null,
            modifier = Modifier
                .clickable {
                    kalenderData.previousMonthAction()
                }
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = kalenderValues.value?.month ?: "",
                style = monthTextStyle
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = kalenderValues.value?.year ?: "",
                style = yearTextStyle
            )
        }
        Icon(
            painter = painterResource(id = nextMonthArrow),
            contentDescription = null,
            modifier = Modifier.clickable {
                kalenderData.nextMonthAction()
            }
        )
    }
}

@Composable
fun KalenderDayAbbreviationItem(
    dayAbbr: String,
    modifier: Modifier,
    backgroundColor: Color,
    textStyle: TextStyle,
    borderWidth: Dp?,
    borderColor: Color,
) {
    Card(
        border = if (borderWidth != null) BorderStroke(borderWidth, borderColor) else null,
        shape = RectangleShape,
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        modifier = modifier
    ) {
        Text(
            text = dayAbbr,
            style = textStyle,
            maxLines = 1,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 2.dp, bottom = 2.dp)
        )
    }
}

@Composable
fun KalenderItem(
    day: Int,
    textStyle: TextStyle,
    paddingValues: PaddingValues,
    containerColor: Color,
    shape: Shape,
    @DrawableRes background: Int?,
    backgroundContentScale: ContentScale,
    borderColor: Color,
    borderWidth: Dp?
) {
    Card(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(paddingValues),
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = if (borderWidth != null) BorderStroke(borderWidth, borderColor) else null
    ) {
        Box(contentAlignment = Alignment.Center) {
            if (background != null) {
                Image(
                    painter = painterResource(id = background),
                    contentDescription = null,
                    contentScale = backgroundContentScale
                )
            }
            Text(
                text = "$day",
                style = textStyle,
                textAlign = TextAlign.Center,
                maxLines = 1,
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentHeight(align = Alignment.CenterVertically)
            )
        }
    }
}