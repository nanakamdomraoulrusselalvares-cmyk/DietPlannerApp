package com.guyanne.dietplanner.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.guyanne.dietplanner.domain.model.ShoppingItem
import com.guyanne.dietplanner.viewmodel.ShoppingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListScreen(onBack: () -> Unit, viewModel: ShoppingViewModel = hiltViewModel()) {
    val items by viewModel.items.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var newItemName by remember { mutableStateOf("") }
    var newItemQty by remember { mutableStateOf("") }
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false; newItemName = ""; newItemQty = "" },
            title = { Text("Add Shopping Item", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = newItemName,
                        onValueChange = { newItemName = it },
                        label = { Text("Item Name") },
                        placeholder = { Text("e.g. Greek Yogurt") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = newItemQty,
                        onValueChange = { newItemQty = it },
                        label = { Text("Quantity") },
                        placeholder = { Text("e.g. 500g, 2 packs") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.addItem(newItemName, newItemQty)
                        showAddDialog = false; newItemName = ""; newItemQty = ""
                    },
                    shape = RoundedCornerShape(12.dp)
                ) { Text("Add Item") }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false; newItemName = ""; newItemQty = "" }) { Text("Cancel") }
            }
        )
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = { 
                    Column {
                        Text("Shopping List", fontWeight = FontWeight.ExtraBold)
                        if (items.isNotEmpty()) {
                            Text(
                                text = "${items.count { it.isChecked }}/${items.size} items collected",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (items.isNotEmpty()) {
                        IconButton(onClick = { viewModel.clearAll() }) {
                            Icon(Icons.Default.DeleteSweep, contentDescription = "Clear All")
                        }
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(16.dp),
                expanded = scrollBehavior.state.collapsedFraction < 0.5f,
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("New Item") }
            )
        }
    ) { padding ->
        if (items.isEmpty()) {
            EmptyShoppingState()
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val uncheckedItems = items.filter { !it.isChecked }
                val checkedItems = items.filter { it.isChecked }

                if (uncheckedItems.isNotEmpty()) {
                    items(uncheckedItems, key = { it.id }) { item ->
                        ShoppingItemRow(
                            item = item, 
                            onToggle = { viewModel.toggleItem(item) }, 
                            onDelete = { viewModel.deleteItem(item) }
                        )
                    }
                }

                if (checkedItems.isNotEmpty()) {
                    item {
                        Text(
                            "Completed",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                            modifier = Modifier.padding(top = 8.dp, bottom = 4.dp, start = 8.dp)
                        )
                    }
                    items(checkedItems, key = { it.id }) { item ->
                        ShoppingItemRow(
                            item = item, 
                            onToggle = { viewModel.toggleItem(item) }, 
                            onDelete = { viewModel.deleteItem(item) }
                        )
                    }
                }
                
                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }
    }
}

@Composable
fun EmptyShoppingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Surface(
                modifier = Modifier.size(120.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Outlined.ShoppingCart,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                "List is empty",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Add items you need for your meal plans.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

@Composable
fun ShoppingItemRow(item: ShoppingItem, onToggle: () -> Unit, onDelete: () -> Unit) {
    val backgroundColor by animateColorAsState(
        if (item.isChecked) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f) 
        else MaterialTheme.colorScheme.surface,
        label = "itemBg"
    )
    val elevation by animateDpAsState(
        if (item.isChecked) 0.dp else 2.dp,
        label = "itemElevation"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = item.isChecked,
                onCheckedChange = { onToggle() },
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary,
                    uncheckedColor = MaterialTheme.colorScheme.outline
                )
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = if (item.isChecked) FontWeight.Normal else FontWeight.SemiBold,
                    textDecoration = if (item.isChecked) TextDecoration.LineThrough else null,
                    color = if (item.isChecked) MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f) 
                            else MaterialTheme.colorScheme.onSurface
                )
                if (item.quantity.isNotBlank()) {
                    Text(
                        text = item.quantity,
                        style = MaterialTheme.typography.labelMedium,
                        color = if (item.isChecked) MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                                else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Close, 
                    contentDescription = "Remove", 
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
