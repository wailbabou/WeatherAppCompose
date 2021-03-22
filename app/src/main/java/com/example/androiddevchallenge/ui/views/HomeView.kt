package com.example.androiddevchallenge.ui.views

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.R
import com.example.androiddevchallenge.models.OtherDayModel
import com.example.androiddevchallenge.models.WeatherModel
import com.example.androiddevchallenge.ui.MainViewModel
import com.example.androiddevchallenge.ui.theme.gray
import kotlinx.coroutines.launch

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
                    FavoritesView(
                        modifier = Modifier.padding(16.dp),
                        mainViewModel = mainVM
                    )
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
                    text = stringResource(R.string.good_morning),
                    style = MaterialTheme.typography.body1
                )
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.disabled) {
                    Text(
                        text = "6 August, Saturday",
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
                    contentDescription = stringResource(R.string.profile_picture),
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
    onClick : (item: WeatherModel) -> Unit
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
    onClick : (item: WeatherModel) -> Unit
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
                color = gray
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
                    contentDescription = city.iconDesc,
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
                    color = MaterialTheme.colors.secondaryVariant,
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
fun FavoritesView(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel
){
    val favoriteCity = mainViewModel.favoriteCity.observeAsState()
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.your_favorite),
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
                    text = favoriteCity.value!!.city,
                    style = MaterialTheme.typography.body2,
                    color = gray
                )
                Row(
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = favoriteCity.value!!.degree,
                        style = MaterialTheme.typography.h1
                    )
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        Image(
                            painter = painterResource(id = favoriteCity.value!!.icon),
                            contentDescription = favoriteCity.value!!.iconDesc,
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
