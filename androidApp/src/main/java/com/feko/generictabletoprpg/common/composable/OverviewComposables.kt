package com.feko.generictabletoprpg.com.feko.generictabletoprpg.common.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.feko.generictabletoprpg.common.Common
import com.feko.generictabletoprpg.common.Identifiable
import com.feko.generictabletoprpg.common.Named
import com.feko.generictabletoprpg.common.OverviewViewModel
import com.feko.generictabletoprpg.theme.Typography

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun <TViewModel, T> OverviewScreen(
    navController: NavHostController,
    viewModel: TViewModel,
    listItem: @Composable (T, NavHostController) -> Unit,
    uniqueListItemKey: (Any) -> Any = { (it as Identifiable).id },
    isFabEnabled: Boolean = false,
    onFabClicked: () -> Unit = {},
    isFabDropdownMenuEnabled: Boolean = false,
    dropdownMenuContent: @Composable () -> Unit = {},
    alertDialogComposable: @Composable () -> Unit = {}
) where TViewModel : OverviewViewModel<T>,
        T : Any {
    val listItems by viewModel.items.collectAsState(listOf())
    val searchString by viewModel.searchString.collectAsState("")
    Column(Modifier.padding(8.dp)) {
        Common.SearchTextField(searchString) {
            viewModel.searchStringUpdated(it)
        }
        Spacer(Modifier.height(8.dp))
        if (listItems.isNotEmpty()) {
            LazyColumn(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    listItems,
                    key = uniqueListItemKey
                ) { item ->
                    listItem(item, navController)
                }
            }
        } else {
            Box(Modifier.fillMaxSize()) {
                Column(
                    Modifier
                        .wrapContentHeight()
                        .fillMaxWidth()
                        .align(Alignment.Center)
                ) {
                    Icon(
                        Icons.Default.List, "",
                        Modifier
                            .size(80.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Nothing here...",
                        Modifier.align(Alignment.CenterHorizontally),
                        style = Typography.titleLarge
                    )
                }
            }
        }
    }
    if (isFabEnabled) {
        val expanded by viewModel.isFabDropdownMenuExpanded.collectAsState(false)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        )
        {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {},
                modifier = Modifier
                    .wrapContentSize()
                    .align(Alignment.BottomEnd)
            ) {
                if (isFabDropdownMenuEnabled) {
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { viewModel.onDismissFabDropdownMenuRequested() },
                        content = {
                            dropdownMenuContent()
                        }
                    )
                }
                FloatingActionButton(
                    onClick = {
                        if (isFabDropdownMenuEnabled) {
                            viewModel.toggleFabDropdownMenu()
                        } else {
                            onFabClicked()
                        }
                    },
                    Modifier
                        .size(48.dp)
                        .menuAnchor()
                ) {
                    Icon(Icons.Default.Add, "")
                }
            }
        }
    }
    val isDialogVisible by viewModel.isDialogVisible.collectAsState(false)
    if (isDialogVisible) {
        alertDialogComposable()
    }
}

@Composable
fun <T> OverviewListItem(
    item: T,
    navController: NavHostController,
    getNavRoute: (T) -> String
) {
    ListItem(
        headlineContent = {
            Text((item as Named).name)
        },
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate(getNavRoute(item))
            })
}