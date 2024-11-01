package com.khalekuzzaman.just.cse.task.presentation.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.khalekuzzaman.just.cse.task.domain.model.ProductModel
import com.khalekuzzaman.just.cse.task.presentation.presentationlogic.PresentationFactory



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen() {
    val searchController = remember { PresentationFactory.provideSearchController() }
    val chartData by searchController.chartData.collectAsState(initial = null)
    val productList by searchController.products.collectAsState(initial = emptyList())

    var selectedPeriod by remember { mutableStateOf("1W") }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Search",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { /* Handle back action */ }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }

            )
        }){

        Column(
            modifier = Modifier.padding(it)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
           Column (modifier = Modifier.width(IntrinsicSize.Max)){
               chartData?.let { chart ->
                   CustomTabBar(
                       modifier=Modifier.widthIn(max = 400.dp),
                       timePeriods = chart.data.keys.toList(),
                       selectedPeriod = selectedPeriod,
                       onPeriodSelected = { selectedPeriod = it }
                   )
                   Spacer(modifier = Modifier.height(16.dp))
                   val xAxisData = chart.data[selectedPeriod]?.xAxisData ?: emptyList()
                   val yAxisData = chart.data[selectedPeriod]?.yAxisData ?: emptyList()
                   LineChart(
                       xAxisData = xAxisData,
                       yAxisData = yAxisData,
                       modifier = Modifier.width(300.dp).height(200.dp)
                   )
               }
           }
            Spacer(modifier = Modifier.height(32.dp))
            SpendingSummary(
                amountOfSpending = "$5000",
                dueDateString = "10 OCT",
                onPayEarlyPressed = {},
                bgColor = Color.Black,
                buttonColor = Color.Blue
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Display recent products if the product list is not empty
            RecentProduct(
                products = productList,
                modifier = Modifier.fillMaxWidth().heightIn(max = 400.dp)
            )

        }

    }

}


@Composable
fun SpendingSummary(
    amountOfSpending: String,
    dueDateString: String,
    onPayEarlyPressed: () -> Unit,
    bgColor: Color,
    buttonColor: Color
) {
    val textColor = if (bgColor.luminance() > 0.5) Color.Black else Color.White
    val buttonTextColor = if (buttonColor.luminance() > 0.5) Color.Black else Color.White
    Box(
        modifier = Modifier
            .background(color = bgColor, shape = RoundedCornerShape(12.dp))
            .padding(16.dp)
            .widthIn(min = 200.dp, max = 300.dp)
            .shadow(5.dp, shape = RoundedCornerShape(12.dp))
    ) {
        SpendingLayoutStrategy(
            labelSpending = {
                Text(
                    text = "Total Spending",
                    fontSize = 16.sp,
                    color = textColor
                )
            },
            dueDate = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Due Date: ",
                        fontSize = 16.sp,
                        color = textColor
                    )
                    Text(
                        text = dueDateString,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W400,
                        color = textColor
                    )
                }
            },
            cost = {
                Text(
                    text = amountOfSpending,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
            },
            action = {
                Button(
                    onClick = onPayEarlyPressed,
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
                ) {
                    Text(
                        text = "PAY EARLY",
                        color = buttonTextColor
                    )
                }
            }
        )
    }
}

@Composable
fun SpendingLayoutStrategy(
    labelSpending: @Composable () -> Unit,
    dueDate: @Composable () -> Unit,
    cost: @Composable () -> Unit,
    action: @Composable () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(modifier = Modifier.weight(1f)) {
                labelSpending()
            }
            dueDate()
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(modifier = Modifier.weight(1f)) {
                cost()
            }
            action()
        }
    }
}


@Composable
fun RecentProduct(
    products: List<ProductModel>,
    modifier: Modifier = Modifier
) {
    Column {
        Text(
            text = "Recent Products",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
    if (products.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(Modifier.size(50.dp))
        }
    } else {
        LazyColumn(
            modifier = modifier
                .background(Color(0xFFC8C8C8).copy(alpha = 0.7f), shape = RoundedCornerShape(12.dp))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(products) { product ->
                ProductItem(product = product)
            }
        }
    }


}

@Composable
fun ProductItem(product: ProductModel, modifier: Modifier = Modifier) {
    ProductLayoutStrategy(
        image = {
            AsyncImage(
                model = product.image,
                contentDescription = "Product image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
        },
        title = {
            Text(
                text = product.title,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        },
        price = {
            Text(
                text = "$${product.price}",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
            )
        },
        rating = {
            Text(
                text = "Rating: ${product.rating.rate} (${product.rating.count})",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    )
}

@Composable
fun ProductLayoutStrategy(
    image: @Composable () -> Unit,
    title: @Composable () -> Unit,
    price: @Composable () -> Unit,
    rating: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        image()

        Column(
            modifier = Modifier.weight(1f)
        ) {
            title()
            Spacer(modifier = Modifier.height(8.dp))
            rating()
        }

        price()
    }
}

@Composable
fun CustomTabBar(
    modifier: Modifier=Modifier,
    timePeriods: List<String>,
    selectedPeriod: String,
    onPeriodSelected: (String) -> Unit,
    selectedColor: Color = Color.Black,
    unselectedColor: Color = Color(0xFFF2F4F7),
    containerColor: Color = Color(0xFFF2F4F7),
    borderColor: Color = Color(0xFFF2F4F7),
) {
    Box(
        Modifier.fillMaxWidth()
            .background(
                color = containerColor,
                shape = RoundedCornerShape(4.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = modifier.padding(16.dp)//for thinness of tab bar
            ) {
            timePeriods.forEach { period ->
                TabItem(
                    period = period,
                    isSelected = selectedPeriod == period,
                    selectedColor = selectedColor,
                    unselectedColor = unselectedColor,
                    borderColor = borderColor,
                    onClick = { onPeriodSelected(period) }
                )
            }
        }
    }


}

@Composable
fun TabItem(
    period: String,
    isSelected: Boolean,
    selectedColor: Color,
    unselectedColor: Color,
    borderColor: Color = Color(0xFFF2F4F7),
    onClick: () -> Unit
) {
    val backgroundColor =
        animateColorAsState(if (isSelected) selectedColor else unselectedColor).value
    val textColor = if (backgroundColor.luminance() > 0.5) Color.Black else Color.White
    val borderStroke =
        if (isSelected) BorderStroke(1.dp, selectedColor) else BorderStroke(1.dp, borderColor)

    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(4.dp),
        border = borderStroke,
        modifier = Modifier
            .clickable { onClick() }
    ) {
        Text(
            text = period,
            color = textColor,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}


data class LineChartStyle(
    val curveLineColor: Color = Color.Blue,
    val gridColor: Color = Color.Gray,
    val pointColor: Color = Color.Blue,
    val xAxisColor: Color = Color.Gray,
    val yAxisColor: Color = Color.Gray,
    val wrapperBoxColor: Color = Color.Black,
    val curveLineWidth: Float = 2f,
    val gridLineWidth: Float = 1f,
    val pointSize: Float = 4f,
    val axisLineWidth: Float = 1f,
    val xAxisLabelStyle: TextStyle = TextStyle(
        color = Color.Black, fontSize = 10.sp, fontWeight = FontWeight.Bold
    ),
    val yAxisLabelStyle: TextStyle = TextStyle(
        color = Color.Black, fontSize = 10.sp, fontWeight = FontWeight.Bold
    ),
    val wrapperBoxTopLeftRadius: Float = 12f,
    val wrapperBoxBottomLeftRadius: Float = 12f
)

@Composable
fun LineChart(
    xAxisData: List<String>,
    yAxisData: List<Int>,
    style: LineChartStyle = LineChartStyle(),
    modifier: Modifier = Modifier
) {
    val textMeasurer = rememberTextMeasurer()

    Canvas(modifier = modifier.fillMaxSize()) {
        val maxY = yAxisData.maxOrNull() ?: 1
        val scaleFactor = size.height / maxY
        val xSpacing = size.width / (xAxisData.size - 1)

        // Generate points
        val points = yAxisData.mapIndexed { index, value ->
            Offset(x = index * xSpacing, y = size.height - (value * scaleFactor).toFloat())
        }

        // Draw grid lines
        drawGridLines(
            numYLabels = 5,
            xSpacing = xSpacing,
            style = style,
            xAxisDataSize = xAxisData.size
        )

        // Draw axes
        drawAxes(style = style)

        // Draw X-axis labels

        drawXLabels(
            xAxisData = xAxisData,
            xSpacing = xSpacing,
            style = style,
            textMeasurer = textMeasurer
        )

        // Draw Y-axis labels
        drawYLabels(maxY = maxY, numYLabels = 5, style = style, textMeasurer = textMeasurer)

        // Draw path
        drawPath(points = points, style = style)

        // Draw points on the path
        //  drawPoints(points, style = style)
    }
}

fun DrawScope.drawPath(points: List<Offset>, style: LineChartStyle) {
    if (points.size < 2) return

    val path = Path().apply {
        moveTo(points.first().x, points.first().y)
        for (i in 1 until points.size) {
            val midPoint = Offset(
                (points[i - 1].x + points[i].x) / 2,
                (points[i - 1].y + points[i].y) / 2
            )
            quadraticBezierTo(points[i - 1].x, points[i - 1].y, midPoint.x, midPoint.y)
        }
        lineTo(points.last().x, points.last().y)
    }

    drawPath(
        path = path,
        color = style.curveLineColor,
        style = Stroke(width = style.curveLineWidth)
    )
}

fun DrawScope.drawPoints(points: List<Offset>, style: LineChartStyle) {
    points.forEach { point ->
        drawCircle(
            color = style.pointColor,
            radius = style.pointSize,
            center = point
        )
    }
}


fun DrawScope.drawAxes(style: LineChartStyle) {
    drawLine(
        start = Offset(0f, size.height),
        end = Offset(size.width, size.height),
        color = style.xAxisColor,
        strokeWidth = style.axisLineWidth
    )
    drawLine(
        start = Offset(size.width, 0f),
        end = Offset(size.width, size.height),
        color = style.yAxisColor,
        strokeWidth = style.axisLineWidth
    )
}

fun DrawScope.drawGridLines(
    numYLabels: Int,
    xAxisDataSize: Int,
    xSpacing: Float,
    style: LineChartStyle
) {
    // Draw horizontal grid lines
    for (i in 1 until numYLabels) {
        val y = size.height - (i * (size.height / numYLabels))
        drawLine(
            start = Offset(0f, y),
            end = Offset(size.width, y),
            color = style.gridColor.copy(alpha = 0.3f),
            strokeWidth = style.gridLineWidth
        )
    }

    // Draw vertical grid lines only up to the number of x-axis data points
    for (i in 1 until xAxisDataSize) {
        val x = i * xSpacing
        if (x <= size.width) { // Ensure we don't draw lines beyond the canvas width
            drawLine(
                start = Offset(x, 0f),
                end = Offset(x, size.height),
                color = style.gridColor.copy(alpha = 0.3f),
                strokeWidth = style.gridLineWidth
            )
        }
    }
}


fun DrawScope.drawXLabels(
    xAxisData: List<String>,
    xSpacing: Float,
    style: LineChartStyle,
    textMeasurer: TextMeasurer
) {
    xAxisData.forEachIndexed { index, label ->
        val textLayoutResult = textMeasurer.measure(label, style = style.xAxisLabelStyle)
        val xPosition = index * xSpacing - textLayoutResult.size.width / 2
        val yPosition = size.height

        drawText(
            textLayoutResult,
            topLeft = Offset(xPosition, yPosition)
        )
    }
}

fun DrawScope.drawYLabels(
    maxY: Int,
    numYLabels: Int,
    style: LineChartStyle,
    textMeasurer: TextMeasurer
) {
    val yLabelInterval = (maxY / numYLabels).coerceAtLeast(1)

    for (i in 1..numYLabels) {
        val yValue = i * yLabelInterval
        val yPosition = size.height - (i * (size.height / numYLabels))
        val textLayoutResult = textMeasurer.measure("${yValue}k", style = style.yAxisLabelStyle)

        drawText(
            textLayoutResult,
            topLeft = Offset(size.width + 20, yPosition - textLayoutResult.size.height / 2)
        )
    }
}
