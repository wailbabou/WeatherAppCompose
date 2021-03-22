package com.example.androiddevchallenge.ui.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Divider
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.models.OtherDayModel
import com.example.androiddevchallenge.ui.MainViewModel

@Composable
fun MySheetView(mainVM: MainViewModel){
    val selectedCity = mainVM.selectedCity.observeAsState()
    Surface(
        color = MaterialTheme.colors.background,
    ) {
        Column(
            modifier = Modifier
                .padding(32.dp)
        ) {
            Text(
                text = "Future Weather",
                style = MaterialTheme.typography.body1,
                modifier = Modifier.paddingFromBaseline(bottom = 8.dp)
            )
            Text (
                text = "${selectedCity.value?.city}",
                style = MaterialTheme.typography.caption,
                modifier = Modifier.paddingFromBaseline(bottom = 16.dp )
            )
            OtherDaysWeatherList(mainVM)
        }
    }
}

@Composable
fun OtherDaysWeatherList(mainVM: MainViewModel) {
    val selectedCity = mainVM.selectedCity.observeAsState()
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        items(selectedCity.value!!.otherDays){
            RowOtherDay(item = it)
        }
    }
}

@Composable
fun RowOtherDay(item : OtherDayModel){
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            Modifier
                .clip(CircleShape)
                .border(
                    border = BorderStroke(1.dp, MaterialTheme.colors.secondaryVariant),
                    shape = CircleShape
                )
        ) {
            Image(
                painter = painterResource(id = item.icon),
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .padding(16.dp)
                    .alignByBaseline(),
                contentScale = ContentScale.FillHeight
            )
        }
        Row(
            Modifier
                .border(
                    border = BorderStroke(1.dp, MaterialTheme.colors.secondaryVariant),
                    shape = MaterialTheme.shapes.medium
                )
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 32.dp)
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = item.degree,
                style = MaterialTheme.typography.body1
            )
            Spacer(Modifier.size(16.dp))
            Divider(
                color = MaterialTheme.colors.secondaryVariant,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp)
            )
            Spacer(Modifier.size(16.dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(align = Alignment.Center)
            ) {
                Text(
                    text = item.day,
                    style = MaterialTheme.typography.caption
                )
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.disabled) {
                    Text(
                        text = item.date,
                        style = MaterialTheme.typography.caption
                    )
                }
            }
        }
    }
}