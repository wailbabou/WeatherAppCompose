/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.Card
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.models.OtherDayModel
import com.example.androiddevchallenge.ui.theme.MyTheme
import com.example.androiddevchallenge.models.WeatherModel
import com.example.androiddevchallenge.ui.MainViewModel
import com.example.androiddevchallenge.ui.theme.lightBlue
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                MyApp()
            }
        }
    }
}

// Start building your app here!
@ExperimentalMaterialApi
@Composable
fun MyApp() {
    val mainVM = MainViewModel()
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )
    val coroutineScope = rememberCoroutineScope()

    Surface(color = MaterialTheme.colors.background) {
        BottomSheetScaffold(
            scaffoldState = bottomSheetScaffoldState,
            sheetContent = {
                MySheetView(mainVM = mainVM)
            },
            sheetPeekHeight = 0.dp,
            sheetShape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)
        ) {
            Scaffold(
                bottomBar = {
                    MyBottomApp()
                }
            ) {
                Column {
                    HeaderView(modifier = Modifier.padding(16.dp))
                    CitiesListView(mainVM = mainVM){
                        coroutineScope.launch {
                            mainVM.setSelectedCity(it)
                            if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                                bottomSheetScaffoldState.bottomSheetState.expand()
                            } else {
                                bottomSheetScaffoldState.bottomSheetState.collapse()
                            }
                        }
                    }
                    FavoritesView(modifier = Modifier.padding(16.dp))
                }
            }
        }
    }
}

@Composable
fun MyBottomApp(){
    BottomNavigation(
        backgroundColor = MaterialTheme.colors.background
    ) {
        class Nav(val title: String, val resource: ImageVector)

        val navs = listOf(
            Nav("Home", Icons.Filled.Spa),
            Nav("Settings", Icons.Filled.Settings)
        )
        var selected by remember { mutableStateOf(0) }
        navs.forEachIndexed { index, nav ->
            BottomNavigationItem(
                selected = selected == index,
                label = {
                    Text(
                        text = nav.title.toUpperCase(),
                        style = MaterialTheme.typography.caption,
                        color = MaterialTheme.colors.onBackground
                    )
                },
                icon = {
                    Icon(nav.resource,
                        contentDescription = nav.title,
                        modifier = Modifier.size(18.dp)
                    )
                },
                onClick = {
                    selected = index
                }
            )
        }
    }
}

@Composable
fun HeaderView(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.Top,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Text(
                    text = "Good Morning",
                    style = MaterialTheme.typography.body1
                )
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.disabled) {
                    Text(
                        text = "27 March, Monday",
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier.paddingFromBaseline(top = 32.dp)
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.user),
                    contentDescription = null,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .shadow(1.dp, CircleShape)
                )
            }

        }
    }
}

@Composable
fun CitiesListView(
    modifier: Modifier = Modifier,
    mainVM: MainViewModel,
    onClick : (item:WeatherModel) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp ,
            top = 4.dp,
            bottom = 4.dp
        ),
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(mainVM.items) { city ->
            CityView(
                city = city,
            ){
                onClick(it)
            }
        }
    }
}

@Composable
fun CityView(
    city: WeatherModel,
    modifier : Modifier = Modifier,
    onClick : (item:WeatherModel) -> Unit
) {
    Card(
        elevation = 1.dp,
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
            .width(250.dp)
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colors.secondary)
                .clickable {
                    onClick(city)
                }
                .padding(24.dp)

        ) {
            Text(
                text = city.city,
                style = MaterialTheme.typography.body2,
                color = Color(0xFFb7bac3)
            )
            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = city.degree,
                    style = MaterialTheme.typography.h1
                )
                Spacer(Modifier.size(32.dp))
                Image(
                    painter = painterResource(id = city.icon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                        .alignByBaseline(),
                    contentScale = ContentScale.FillHeight
                )
            }
            MoreDetailsView()
        }
    }
}

@Composable
fun MoreDetailsView(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            elevation = 1.dp,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier
                .fillMaxWidth()

        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .wrapContentSize(align = Alignment.Center),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "9 km/h",
                        style = MaterialTheme.typography.caption
                    )
                    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.disabled) {
                        Text(
                            text = "Wind",
                            style = MaterialTheme.typography.caption
                        )
                    }
                }
                Divider(
                    color = Color.LightGray,
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp)
                        //.padding(horizontal = 8.dp)
                )
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "79%",
                        style = MaterialTheme.typography.caption
                    )
                    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.disabled) {
                        Text(
                            text = "Humidity",
                            style = MaterialTheme.typography.caption
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FavoritesView(modifier: Modifier = Modifier){
    Column(modifier = modifier) {
        Text(
            text = "Your favorite",
            style = MaterialTheme.typography.h5
        )
        Spacer(Modifier.size(16.dp))
        Card(
            elevation = 1.dp,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colors.secondary)
                    .padding(24.dp)
            ) {
                Text(
                    text = "Algiers",
                    style = MaterialTheme.typography.body2,
                    color = Color(0xFFb7bac3)
                )
                Row(
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = "10°",
                        style = MaterialTheme.typography.h1
                    )
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.windy_cloudy_night),
                            contentDescription = null,
                            modifier = Modifier
                                .size(80.dp)
                                .alignByBaseline(),
                            contentScale = ContentScale.FillHeight
                        )
                    }
                }
            }
        }
    }
}

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
                    border = BorderStroke(1.dp, lightBlue),
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
                    border = BorderStroke(1.dp, lightBlue),
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
                color = lightBlue,
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

@ExperimentalMaterialApi
@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    MyTheme {
        MyApp()
    }
}

@ExperimentalMaterialApi
@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
    MyTheme(darkTheme = true) {
        MyApp()
    }
}
