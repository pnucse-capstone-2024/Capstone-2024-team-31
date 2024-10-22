package com.wasin.presentation.monitoring

import android.annotation.SuppressLint
import android.text.Layout
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisGuidelineComponent
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisLabelComponent
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.cartesianLayerPadding
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoZoomState
import com.patrykandpatrick.vico.compose.common.component.fixed
import com.patrykandpatrick.vico.compose.common.component.rememberLayeredComponent
import com.patrykandpatrick.vico.compose.common.component.rememberShapeComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.common.component.shadow
import com.patrykandpatrick.vico.compose.common.component.shapeComponent
import com.patrykandpatrick.vico.compose.common.data.rememberExtraLambda
import com.patrykandpatrick.vico.compose.common.dimensions
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.compose.common.rememberHorizontalLegend
import com.patrykandpatrick.vico.compose.common.shape.markerCorneredShape
import com.patrykandpatrick.vico.compose.common.shape.rounded
import com.patrykandpatrick.vico.compose.common.vicoTheme
import com.patrykandpatrick.vico.core.cartesian.CartesianDrawingContext
import com.patrykandpatrick.vico.core.cartesian.CartesianMeasuringContext
import com.patrykandpatrick.vico.core.cartesian.HorizontalDimensions
import com.patrykandpatrick.vico.core.cartesian.Zoom
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModel
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.core.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.core.cartesian.marker.CartesianMarker
import com.patrykandpatrick.vico.core.cartesian.marker.DefaultCartesianMarker
import com.patrykandpatrick.vico.core.common.Insets
import com.patrykandpatrick.vico.core.common.LayeredComponent
import com.patrykandpatrick.vico.core.common.Legend
import com.patrykandpatrick.vico.core.common.LegendItem
import com.patrykandpatrick.vico.core.common.component.Shadow
import com.patrykandpatrick.vico.core.common.component.ShapeComponent
import com.patrykandpatrick.vico.core.common.component.TextComponent
import com.patrykandpatrick.vico.core.common.copyColor
import com.patrykandpatrick.vico.core.common.shape.Corner
import com.patrykandpatrick.vico.core.common.shape.CorneredShape
import com.wasin.data.model.monitoring.FindMultipleMonitorResponse
import com.wasin.presentation._common.FilterDropDownButton
import com.wasin.presentation._common.GrayDivider
import com.wasin.presentation._common.MyCircularProgress
import com.wasin.presentation._common.MyEmptyContent
import com.wasin.presentation._common.metricColor
import com.wasin.presentation._theme.gray_808080
import com.wasin.presentation._theme.main_blue
import com.wasin.presentation._util.LaunchedEffectEvent
import com.wasin.presentation._util.NoRippleInteractionSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit


@Composable
fun MonitoringScreen(
    navController: NavController,
    viewModel: MonitoringViewModel = hiltViewModel()
) {
    val refreshState = rememberSwipeRefreshState(isRefreshing = false)
    LaunchedEffectEvent(viewModel.eventFlow)
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        FilterMonitoring(
            modifier = Modifier.align(Alignment.End),
            time = viewModel.activeTime.value.kor,
            timeClick = { viewModel.refreshTime(it) }
        )
        MonitoringTabLayer(
            state = viewModel.monitoring.value,
            selectedTabIndex = viewModel.selectedTabIndex.intValue,
            onTabClick = { tabIndex, metricId ->
                viewModel.onTabClick(tabIndex, metricId)
            }
        )
    }
}

@Composable
fun FilterMonitoring(
    modifier: Modifier,
    time: String,
    timeClick: (Int) -> Unit = {}
) {
    FilterDropDownButton(
        modifier = modifier.padding(top = 10.dp, start = 7.dp),
        text = time,
        selectList = TimeEnum.entries.map { it.kor }.toList(),
        onClick = timeClick
    )
}

@Composable
fun MonitoringTabLayer(
    state: MonitoringMultipleState,
    selectedTabIndex: Int,
    onTabClick: (Int, Long) -> Unit,
){
    val modelProducer = remember { CartesianChartModelProducer() }
    if (state.isLoading) {
        MyCircularProgress()
    }
    else if (state.errorMessage.isNotEmpty()) {
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            MyEmptyContent(
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
    }
    else  {
        LaunchedEffect(key1 = state) {
            withContext(Dispatchers.Default) {
                modelProducer.runTransaction {
                    lineSeries {
                        state.metrics.graphList.forEach {
                            series(x = it.timeList, y = it.valueList)
                        }
                    }
                }
            }
        }
        MonitoringTabLayerContent(
            metrics = state.metrics,
            selectedTabIndex = selectedTabIndex,
            onTabClick = onTabClick,
            modelProducer = modelProducer,
        )
    }
}

@Composable
fun MonitoringTabLayerContent(
    metrics: FindMultipleMonitorResponse,
    selectedTabIndex: Int,
    onTabClick: (Int, Long) -> Unit,
    modelProducer: CartesianChartModelProducer
) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState (selectedTabIndex) { metrics.metricList.size }

    LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
        if (!pagerState.isScrollInProgress) {
            val index = pagerState.currentPage
            if (index < metrics.metricList.size) {
                onTabClick(index, metrics.metricList[index].metricId)
            }
        }
    }
    Column {
        TopMetricList(
            selectedTabIndex = selectedTabIndex,
            tabs = metrics.metricList,
            onTabClick = { index ->
                if (index < metrics.metricList.size) {
                    onTabClick(index, metrics.metricList[index].metricId)
                    scope.launch { pagerState.animateScrollToPage(index) }
                }
            }
        )
        HorizontalPager(
            state = pagerState,
            pageSpacing = 15.dp,
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.Top
        ) { page ->
            MonitoringChart(
                modelProducer = modelProducer,
                graph = metrics.graphList,
                isSamePage = page == selectedTabIndex
            )
        }
    }
}

@Composable
fun TopMetricList(
    selectedTabIndex: Int,
    tabs: List<FindMultipleMonitorResponse.MonitoringMetric>,
    onTabClick: (Int) -> Unit
) {
    ScrollableTabRow(
        selectedTabIndex = selectedTabIndex,
        contentColor = Color.White,
        edgePadding = 0.dp,
        containerColor = Color.White,
        indicator = { TabLayerIndicator(it, selectedTabIndex) },
        divider = { GrayDivider() },
        modifier = Modifier.fillMaxWidth()
    ) {
        tabs.forEachIndexed { index, value ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = { onTabClick(index) },
                text = { TabText(value.metric) },
                interactionSource = NoRippleInteractionSource,
                selectedContentColor = main_blue,
                unselectedContentColor = gray_808080
            )
        }
    }
}

@Composable
private fun TabText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleSmall,
    )
}

@Composable
private fun TabLayerIndicator(
    tabPositionList: List<TabPosition>,
    selectedTabIndex: Int
) {
    TabRowDefaults.SecondaryIndicator(
        modifier = Modifier.tabIndicatorOffset(
            currentTabPosition = tabPositionList[selectedTabIndex]
        ),
        color = main_blue
    )
}

@Composable
private fun MonitoringChart(
    modelProducer: CartesianChartModelProducer,
    graph: List<FindMultipleMonitorResponse.MonitoringGraph>,
    isSamePage: Boolean
) {
    val dateTimeFormatter = SimpleDateFormat("yyyy MM-dd HH:mm:ss", Locale.US)

    if (!isSamePage) {
        MyCircularProgress()
    }
    else {
        val size = graph.size
        CartesianChartHost(
            chart = rememberCartesianChart(
                rememberLineCartesianLayer(
                    LineCartesianLayer.LineProvider.series(
                        (0..size).toList().map {
                            LineCartesianLayer.rememberLine(
                                fill = remember {
                                    LineCartesianLayer.LineFill.single(
                                        fill(
                                            metricColor[it % size]
                                        )
                                    )
                                },
                                areaFill = null
                            )
                        }
                    )
                ),
                startAxis = VerticalAxis.rememberStart(
                    label = rememberStartAxisLabel(),
                    horizontalLabelPosition = VerticalAxis.HorizontalLabelPosition.Inside,
                    guideline = null,
                ),
                bottomAxis = HorizontalAxis.rememberBottom(
                    guideline = null,
                    itemPlacer = HorizontalAxis.ItemPlacer.segmented(),
                    valueFormatter = { _, value, _ ->
                        dateTimeFormatter.format(
                            Date(TimeUnit.MILLISECONDS.toMillis(value.toLong()))
                        )
                    }
                ),
                marker = rememberMarker(),
                layerPadding = cartesianLayerPadding(
                    scalableStartPadding = 16.dp,
                    scalableEndPadding = 16.dp
                ),
                legend = rememberLegend(graph),
            ),
            modelProducer = modelProducer,
            modifier = Modifier.fillMaxHeight(),
            zoomState = rememberVicoZoomState(
                zoomEnabled = true,
                initialZoom = Zoom.Content
            ),
        )
    }
}

@Composable
private fun rememberStartAxisLabel() =
    rememberAxisLabelComponent(
        color = Color.Black,
        margins = dimensions(4.dp),
        padding = dimensions(8.dp, 2.dp),
        background = rememberShapeComponent(main_blue.copy(0.3f), CorneredShape.rounded(4.dp)),
    )

@Composable
private fun rememberLegend(
    graph: List<FindMultipleMonitorResponse.MonitoringGraph>
): Legend<CartesianMeasuringContext, CartesianDrawingContext> {
    val labelComponent = rememberTextComponent(vicoTheme.textColor)
    return rememberHorizontalLegend(
        items = rememberExtraLambda {
            graph.forEachIndexed { i, g ->
                add(
                    LegendItem(
                        icon = shapeComponent(metricColor[i % graph.size], CorneredShape.Pill),
                        labelComponent = labelComponent,
                        label = g.labels
                    )
                )
            }
        },
        iconSize = 8.dp,
        iconPadding = 8.dp,
        spacing = 8.dp,
        padding = dimensions(top = 8.dp),
    )
}

@Composable
internal fun rememberMarker(
    labelPosition: DefaultCartesianMarker.LabelPosition = DefaultCartesianMarker.LabelPosition.Top,
    showIndicator: Boolean = true,
): CartesianMarker {
    val labelBackgroundShape = markerCorneredShape(Corner.FullyRounded)
    val labelBackground =
        rememberShapeComponent(
            color = MaterialTheme.colorScheme.surfaceBright,
            shape = labelBackgroundShape,
            shadow =
            shadow(radius = LABEL_BACKGROUND_SHADOW_RADIUS_DP.dp, dy = LABEL_BACKGROUND_SHADOW_DY_DP.dp),
        )
    val label =
        rememberTextComponent(
            color = MaterialTheme.colorScheme.onSurface,
            textAlignment = Layout.Alignment.ALIGN_CENTER,
            padding = dimensions(8.dp, 4.dp),
            background = labelBackground,
            minWidth = TextComponent.MinWidth.fixed(40.dp),
        )
    val indicatorFrontComponent = rememberShapeComponent(MaterialTheme.colorScheme.surface, CorneredShape.Pill)
    val indicatorCenterComponent = rememberShapeComponent(shape = CorneredShape.Pill)
    val indicatorRearComponent = rememberShapeComponent(shape = CorneredShape.Pill)
    val indicator =
        rememberLayeredComponent(
            rear = indicatorRearComponent,
            front =
            rememberLayeredComponent(
                rear = indicatorCenterComponent,
                front = indicatorFrontComponent,
                padding = dimensions(5.dp),
            ),
            padding = dimensions(10.dp),
        )
    val guideline = rememberAxisGuidelineComponent()
    return remember(label, labelPosition, indicator, showIndicator, guideline) {
        @SuppressLint("RestrictedApi")
        object :
            DefaultCartesianMarker(
                label = label,
                labelPosition = labelPosition,
                indicator =
                if (showIndicator) {
                    { color ->
                        LayeredComponent(
                            rear = ShapeComponent(color.copyColor(alpha = 0.15f), CorneredShape.Pill),
                            front =
                            LayeredComponent(
                                rear =
                                ShapeComponent(
                                    color = color,
                                    shape = CorneredShape.Pill,
                                    shadow = Shadow(radiusDp = 12f, color = color),
                                ),
                                front = indicatorFrontComponent,
                                padding = dimensions(5.dp),
                            ),
                            padding = dimensions(10.dp),
                        )
                    }
                } else {
                    null
                },
                indicatorSizeDp = 36f,
                guideline = guideline,
            ) {
            override fun updateInsets(
                context: CartesianMeasuringContext,
                horizontalDimensions: HorizontalDimensions,
                model: CartesianChartModel,
                insets: Insets,
            ) {
                with(context) {
                    val baseShadowInsetDp =
                        CLIPPING_FREE_SHADOW_RADIUS_MULTIPLIER * LABEL_BACKGROUND_SHADOW_RADIUS_DP
                    var topInset = (baseShadowInsetDp - LABEL_BACKGROUND_SHADOW_DY_DP).pixels
                    var bottomInset = (baseShadowInsetDp + LABEL_BACKGROUND_SHADOW_DY_DP).pixels
                    when (labelPosition) {
                        LabelPosition.Top,
                        LabelPosition.AbovePoint -> topInset += label.getHeight(context) + tickSizeDp.pixels
                        LabelPosition.Bottom -> bottomInset += label.getHeight(context) + tickSizeDp.pixels
                        LabelPosition.AroundPoint -> {}
                    }
                    insets.ensureValuesAtLeast(top = topInset, bottom = bottomInset)
                }
            }
        }
    }
}

private const val LABEL_BACKGROUND_SHADOW_RADIUS_DP = 4f
private const val LABEL_BACKGROUND_SHADOW_DY_DP = 2f
private const val CLIPPING_FREE_SHADOW_RADIUS_MULTIPLIER = 1.4f
