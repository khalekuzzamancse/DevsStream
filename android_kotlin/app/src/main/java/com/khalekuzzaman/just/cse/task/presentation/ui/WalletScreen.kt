package com.khalekuzzaman.just.cse.task.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.khalekuzzaman.just.cse.task.domain.model.BreakdownModel
import com.khalekuzzaman.just.cse.task.presentation.presentationlogic.PresentationFactory
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletScreen() {
    // TODO: Refactor this to use a ViewModel to hold the controller and manage state
    val homeController = remember { PresentationFactory.provideHomeController() }
    val spendReport by homeController.spendReport.collectAsState(null)
    val breakdownData by homeController.breakdownData.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { /* Handle back action */ }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                title = {
                    Text(
                        text = "Wallet",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.W500,
                            fontSize = 16.sp
                        )
                    )
                },
                actions = {
                    IconButton(
                        onClick = { /* Handle account action */ },
                        modifier = Modifier.padding(end = 16.dp)
                    ) {
                        Icon(Icons.Default.AccountCircle, contentDescription = "Account")
                    }
                }
            )
        },
        content = { paddingValues ->

            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                // TODO: Refactor this to use a ViewModel to hold the controller

                Bars(
                    period = spendReport?.period ?: "Loading...",
                    typeOfCost = "Spend Chart",
                    costs = spendReport?.spend?.schedules?.flatMap {

                        listOf(it.firstSchedule, it.secondSchedule, it.thirdSchedule, it.fourthSchedule, it.fifthSchedule)
                    } ?: listOf(0.0, 0.0, 0.0, 0.0, 0.0),
                    currencyType = spendReport?.currency ?: "$"
                )

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier.background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(243, 243, 253), // Start with the extracted background color
                                Color(243, 243, 253).copy(alpha = 0f) // Fade to transparent at the top right
                            ),
                            start = Offset(0f, Float.POSITIVE_INFINITY), // Start from the bottom left
                            end = Offset(Float.POSITIVE_INFINITY, 0f) // End at the top right
                        )
                    )
                ) {
                    BreakDown(
                        modifier = Modifier.padding(start = 8.dp),
                        itemData = breakdownData)

                    Spacer(modifier = Modifier.height(16.dp))

                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(64.dp),
                            modifier = Modifier.wrapContentWidth(),
                            elevation = CardDefaults.cardElevation(1.dp)
                        ) {
                            AccountInfo(
                                expiryDate = "12/24",
                                ccv = "123",
                                onDeletePressed = { /* Handle delete action */ },
                                onSkipPressed = { /* Handle skip action */ },
                                onAccountNumberChanged = { /* Handle account number change */ },
                                onPasswordChanged = { /* Handle password change */ }
                            )
                        }
                    }
                }
            }
        }
    )
}




@Composable
fun Bars(period: String, typeOfCost: String, costs: List<Double>, currencyType: String = "$") {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = period,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            )
            Text(
                text = typeOfCost,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        BarChart(costs = costs, currencySymbol = currencyType)
    }
}


@Composable
fun BarChart(costs: List<Double>, currencySymbol: String) {
    val maxCost = costs.maxOrNull() ?: 0.0

    Row(
        modifier = Modifier.wrapContentWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        costs.forEach { cost ->
            Bar(
                cost = cost,
                maxCost = maxCost,
                barColor = Color(0xFF7F00FF),
                currencySymbol = currencySymbol
            )
        }
    }
}

@Composable
fun Bar(
    cost: Double,
    maxCost: Double,
    barColor: Color = Color(0xFF7F00FF),
    currencySymbol: String = "$"
) {
    val barHeight = (150 * cost / maxCost).coerceAtLeast(0.0)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier.width(IntrinsicSize.Min) // Ensure the width is based on the widest child
    ) {
        Box(
            modifier = Modifier
                .height(barHeight.dp)
                .fillMaxWidth() // Ensures the bar width matches the text width
                .background(
                    color = barColor,
                    shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                )
                .shadow(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
                    clip = false
                )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Display cost below the bar with currency symbol
        Text(
            text = "$currencySymbol${cost}",
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp, end = 8.dp)//end padding will increase the bar width
        )
    }
}



@Composable
fun BreakDown(modifier: Modifier=Modifier,itemData: List<BreakdownModel>) {
    // Prepare sorted data
    val sortedItems = itemData
        .sortedByDescending { it.percentage.replace("%", "").toDoubleOrNull() ?: 0.0 }
        .mapIndexed { index, data ->
            data to index // Pair each item with its position index
        }

    NestedHorizontalScroller(
        modifier=modifier,
        header = {
            Text(
                text = "Breakdown",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(16.dp)
            )
        },
        children = sortedItems.map { (data, position) ->
            {
                BreakdownItem(
                    percentage = data.percentage,
                    label = data.label,
                    position = position,
                    circleColor = Color.White,
                    percentageTextColor = Color.Red,
                    labelColor = Color.Gray
                )
            }
        }
    )
}



@Composable
fun BreakdownItem(
    percentage: String,
    label: String,
    circleColor: Color = Color.White,
    percentageTextColor: Color = Color.Red,
    labelColor: Color = Color.Gray,
    labelOpacity: Float = 0.6f,
    position: Int
) {
    val calculatedOpacity = (1.0 - (position * 0.15)).coerceIn(0.1, 1.0)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(IntrinsicSize.Max)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .customShadow(
                    backgroundColor = circleColor,
                    blurRadius = 8.dp,
                    shadowColor = Color.Black.copy(alpha = 0.1f),
                    borderRadius = 50.dp,
                    padding = 0.dp)
                .size(60.dp)


        ) {
            Text(
                text = percentage,
                style = TextStyle(
                    color = percentageTextColor.copy(alpha = calculatedOpacity.toFloat()),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = label,
            style = TextStyle(
                color = labelColor.copy(alpha = labelOpacity),
                fontSize = 14.sp
            )
        )
    }
}


@Composable
fun AccountInfo(expiryDate: String, ccv: String, onDeletePressed: () -> Unit, onSkipPressed: () -> Unit,
    onAccountNumberChanged: (String) -> Unit, onPasswordChanged: (String) -> Unit
) {
    val labelTextStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.W400)

    // TODO: This state management is just for demo purposes.
    var accountNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(
        modifier = Modifier.widthIn(max = 400.dp)
            .padding(32.dp)
    ) {
        LayoutStrategy(
            labelAccount = {
                Text(text = "Account Number:", style = labelTextStyle)
            },
            labelPassword = {
                Text(text = "Password:", style = labelTextStyle)
            },
            expireDate = {
                Text(text = "Expire Date: $expiryDate", style = labelTextStyle)
            },
            ccv = {
                Text(text = "CCV: $ccv", style = labelTextStyle)
            },
            fieldAccountNo = {
                CustomTextField(
                    modifier = Modifier.width(200.dp),
                    visualTransformation = AccountNumberVisualTransformation(),
                    label = "",
                    value = accountNumber,
                    onValueChange = {
                        accountNumber = it
                        onAccountNumberChanged(it)
                    }
                )
            },
            fieldPassword = {
                CustomTextField(
                    modifier = Modifier.width(200.dp),
                    value = password,
                    visualTransformation = PasswordVisualTransformation(),
                    onValueChange = {
                        password = it
                        onPasswordChanged(it)
                    },
                    keyboardType = KeyboardType.Password,
                    label = ""
                )
            },
            deleteButton = {
                Button(
                    onClick = onDeletePressed,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7F00FF)),
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text("Delete Card", color = Color.White)
                }
            },
            skipButton = {
                Button(
                    onClick = onSkipPressed,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                ) {
                    Text("SKIP", color = Color.White, fontSize = 16.sp)
                }
            }
        )
    }
}


class AccountNumberVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val originalText = text.text
        val cleanedInput = originalText.replace(Regex("\\D"), "")

        // Format the input by adding spaces every 4 characters
        val formattedText = StringBuilder()
        var spaceCount = 0

        for (i in cleanedInput.indices) {
            if (i > 0 && i % 4 == 0) {
                formattedText.append(' ')
                spaceCount++
            }
            formattedText.append(cleanedInput[i])
        }

        // Create a valid offset mapping for cursor positioning
        val transformedText = formattedText.toString()
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 0) return offset
                val spacesBeforeOffset = (0 until offset).count { it % 4 == 0 && it > 0 }
                return (offset + spacesBeforeOffset).coerceAtMost(transformedText.length)
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 0) return offset
                val spacesBeforeOffset = (0 until offset).count { transformedText[it] == ' ' }
                return (offset - spacesBeforeOffset).coerceAtMost(cleanedInput.length)
            }
        }

        return TransformedText(
            text = AnnotatedString(transformedText),
            offsetMapping = offsetMapping
        )
    }
}

@Composable
fun LayoutStrategy(
    labelAccount: @Composable () -> Unit,
    labelPassword: @Composable () -> Unit,
    expireDate: @Composable () -> Unit,
    ccv: @Composable () -> Unit,
    fieldAccountNo: @Composable () -> Unit,
    fieldPassword: @Composable () -> Unit,
    deleteButton: @Composable () -> Unit,
    skipButton: @Composable () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(1f)) {
                labelAccount()
            }
            fieldAccountNo()
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(1f)) {
                labelPassword()
            }
            fieldPassword()
        }

        CardInfoLayoutStrategy(
            expireDate = expireDate,
            ccv = ccv,
            deleteButton = deleteButton
        )

        Spacer(modifier = Modifier.height(8.dp))

        skipButton()
    }
}

@Composable
fun CardInfoLayoutStrategy(
    expireDate: @Composable () -> Unit,
    ccv: @Composable () -> Unit,
    deleteButton: @Composable () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.Start
        ) {
            expireDate()
            Spacer(modifier = Modifier.height(4.dp))
            ccv()
        }
        Box(modifier = Modifier.align(Alignment.CenterVertically)) {
            deleteButton()
        }
    }
}
