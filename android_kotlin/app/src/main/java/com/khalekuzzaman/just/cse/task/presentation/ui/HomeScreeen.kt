package com.khalekuzzaman.just.cse.task.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Smartphone
import androidx.compose.material.icons.outlined.Tv
import androidx.compose.material.icons.outlined.Wifi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import coil3.compose.AsyncImage

data class LoanModel(
    val model: String,
    val imageLink: String,
    val price: String,
    val date: String,
    val rating: Int,
    val ratingMax: Int
)



 class HomeController {
     val user: String = "George"
     val cards: List<CardInfo> = listOf(
         CardInfo(
             cardName = "VISA",
             cardNo = "* * * 3854",
             dueDate = "10 OCT",
             amount = "$5001.86",
             color = Color.Black
         ),
         CardInfo(
             cardName = "VISA",
             cardNo = "* * * 3854",
             dueDate = "10 OCT",
             amount = "$5001.86",
             color = Color.Blue)
     )
     val activeLoanItems = listOf(
         LoanModel(
             model = "Model X",
             imageLink = "https://img.freepik.com/premium-vector/red-city-car-vector-illustration_648968-44.jpg?w=740",
             price = "\$399/M",
             date = "5th OCT",
             rating = 48,
             ratingMax = 60
         ),
         LoanModel(
             model = "Nokia Y",
             imageLink = "https://auspost.com.au/shop/static/WFS/AusPost-Shop-Site/-/AusPost-Shop-auspost-B2CWebShop/en_AU/feat-cat/mobile-phones/category-carousel/MP_UnlockedPhones_3.jpg",
             price = "\$299/M",
             date = "20 OCT",
             rating = 36,
             ratingMax = 50
         )
     )
     val billPayments = listOf(
         BillPayment(title = "Electricity Bill", icon = Icons.Outlined.Lightbulb),
         BillPayment(title = "Internet Recharge", icon = Icons.Outlined.Wifi),
         BillPayment(title = "Cable Bill", icon = Icons.Outlined.Tv),
         BillPayment(title = "Mobile Recharge", icon = Icons.Outlined.Smartphone)
     )

 }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(controller: HomeController = HomeController()) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "✌️ Hey ${controller.user}!",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium)
                    )
                },
                actions = {
                    IconButton(onClick = { /* Handle search */ }) {
                        Icon(Icons.Outlined.Search, contentDescription = "Search")
                    }
                    IconButton(
                        onClick = { /* Handle notifications */ },
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Icon(Icons.Outlined.NotificationsNone, contentDescription = "Notifications")
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(12.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Cards(cards = controller.cards)
                PayBillSection(items = controller.billPayments)
                ActiveLoanSection(loanItems = controller.activeLoanItems)
            }
        }
    )
}



data class CardInfo(
    val cardName: String,
    val cardNo: String,
    val dueDate: String,
    val amount: String,
    val color: Color
)

@Composable
fun Cards(cards: List<CardInfo>) {
        NestedHorizontalScroller(
            showIndicator = true,
            children = cards.map { cardInfo ->
                {
                    CardItem(cardInfo = cardInfo, modifier = Modifier.widthIn(max = 300.dp))
                }
            }
        )

}

@Composable
fun CardItem(modifier: Modifier=Modifier,cardInfo: CardInfo) {
    val textColor = if (cardInfo.color.luminance() > 0.5f) Color.Black else Color.White
    val buttonColor = Color.White
    val buttonTextColor = if (buttonColor.luminance() > 0.5f) Color.Black else Color.White
    val shadowColor = Color.Black.copy(alpha = 0.1f)

    CardLayoutStrategy(
        modifier = modifier
            .customShadow(
            blurRadius = 5.dp,
            borderRadius = 12.dp,
            offset = Offset(0f, 4f),
            backgroundColor = cardInfo.color,
            shadowColor = shadowColor,
            padding = 16.dp
        ),
        cardName = {
            Text(
                text = cardInfo.cardName,
                style = TextStyle(fontSize = 18.sp, color = textColor, fontWeight = FontWeight.Medium, fontStyle = FontStyle.Italic)
            )
        },
        cardNo = {
            Text(
                text = cardInfo.cardNo,
                style = TextStyle(fontSize = 18.sp, color = textColor, fontWeight = FontWeight.Medium)
            )
        },
        dueDate = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Due date",
                    style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Light, color = textColor)
                )
                Text(
                    text = cardInfo.dueDate,
                    style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Normal, color = textColor)
                )
            }
        },
        labelEarly = {
            Text(
                text = "EARLY",
                style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Light, color = textColor)
            )
        },
        amount = {
            Text(
                text = cardInfo.amount,
                style = TextStyle(fontSize = 20.sp, color = textColor, fontWeight = FontWeight.Medium)
            )
        },
        action = {
            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("PAY", color = buttonTextColor)
            }
        }
    )
}

@Composable
fun CardLayoutStrategy(
    modifier: Modifier = Modifier,
    cardName: @Composable () -> Unit,
    cardNo: @Composable () -> Unit,
    dueDate: @Composable () -> Unit,
    labelEarly: @Composable () -> Unit,
    amount: @Composable () -> Unit,
    action: @Composable () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(1f)) {
                cardName()
            }
            cardNo()
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(1f)) {
                dueDate()
            }
            labelEarly()
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(1f)) {
                amount()
            }
            action()
        }
    }
}


data class BillPayment(
    val title: String,
    val icon: ImageVector
)

@Composable
fun PayBillSection(items: List<BillPayment>) {
    NestedHorizontalScroller(
        header = {
            Text(
                text = "Bill Payments",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        },
        children = items.map { item ->
            {
                PayBillItem(
                    label = item.title,
                    icon = item.icon,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
            }
        }
    )
}

@Composable
fun PayBillItem(modifier: Modifier=Modifier,label: String, icon: ImageVector) {
    val bgColor = Color(0xFFF2F4F7)
    val shadowColor = Color.Black.copy(alpha = 0.1f)
    val iconColor = if (bgColor.luminance() > 0.5) Color.Black else Color.White

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier
                .shadow(
                elevation = 1.dp,
                shape = RoundedCornerShape(4.dp),
                ambientColor = shadowColor.copy(alpha = 0.1f),
                spotColor = shadowColor.copy(alpha = 0.1f))
                .clip(RoundedCornerShape(4.dp))
                .background(bgColor)
                .padding(12.dp)
        )
        label.split(" ").forEach { word ->
            Text(
                text = word,
                style = TextStyle(fontSize = 14.sp),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ActiveLoanSection(loanItems: List<LoanModel>) {
    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {
        NestedHorizontalScroller(
            header = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Active Loans",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "See all",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            },
            children = loanItems.map { item ->
                {
                    LoanItem(data = item, modifier = Modifier.width(280.dp).padding(end = 8.dp))
                }
            },
        )
    }
}



@Composable
fun LoanItem(data: LoanModel, modifier: Modifier = Modifier) {
    val medium = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFECEFF1)
        ),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        LoanItemLayoutStrategy(
            price = {
                Text(
                    text = data.price,
                    style = medium
                )
            },
            model = {
                Text(
                    text = data.model,
                    style = medium
                )
            },
            date = {
                Text(
                    text = data.date,
                    style = medium
                )
            },
            rating = {
                Row {
                    Text(
                        text = "${data.rating}",
                        style = medium.copy(color = Color.Blue)
                    )
                    Text(
                        text = "/${data.ratingMax}",
                        style = medium.copy(color = Color.Black)
                    )
                }
            },
            image = {
                AsyncImage(
                    model = data.imageLink,
                    contentDescription = "product image",
                    modifier = Modifier.size(50.dp)
                )
            },
            ratingBar = {
                LinearProgressIndicator(
                    progress = data.rating / data.ratingMax.toFloat(),
                    color = Color.Blue,
                    trackColor = Color.Gray.copy(alpha = 0.3f)
                )
            },
            modifier = Modifier.padding(8.dp)
        )
    }
}
@Composable
fun LoanItemLayoutStrategy(
    price: @Composable () -> Unit,
    model: @Composable () -> Unit,
    date: @Composable () -> Unit,
    rating: @Composable () -> Unit,
    image: @Composable () -> Unit,
    ratingBar: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        image()
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                price()
                Column {
                    date()
                    Spacer(Modifier.height(4.dp))
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                model()
                Row {
                    rating()
                }
            }
            ratingBar()
        }
    }
}