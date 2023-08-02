module Test.ChartCollectionTest where

--import Util.Value (foreignValue)
import HarborView.Maunaloa.Common 
    ( HtmlId(..)
    , ChartHeight(..)
    , ChartMapping(..)
    , ChartId(..)
    , ChartMappings
    )

chartMapping :: HtmlId -> ChartMapping
chartMapping levelCanvasId = 
    ChartMapping 
    { chartId: ChartId "chart"
    , canvasId: HtmlId "test-canvasId"
    , chartHeight: ChartHeight 500.0
    , levelCanvasId: levelCanvasId 
    }

chartMappingsWithoutChartLevel :: ChartMappings 
chartMappingsWithoutChartLevel = [chartMapping (HtmlId "")]
     
chartMappingWithChartLevel :: ChartMapping
chartMappingWithChartLevel = chartMapping (HtmlId "level-canvasid")

chartMappingsWithChartLevel :: ChartMappings 
chartMappingsWithChartLevel = [chartMappingWithChartLevel]


{--
testChartColletionSuite :: TestSuite
testChartColletionSuite = 
    suite "ChartCollection" do
        test "Without ChartCollection" do
            let collection = runExcept $ ChartCollection.fromMappings chartMappingsWithoutChartLevel TC.demox 
            let collection1 = unsafePartial $ fromRight collection
            let (Chart.Chart chart1) = unsafePartial $ fromJust $ Array.head collection1
            Assert.equal chart1.chartLevel Nothing 
        test "With ChartCollection" do
            let collection = runExcept $ ChartCollection.fromMappings chartMappingsWithChartLevel TC.demox 
            let collection1 = unsafePartial $ fromRight collection
            let (Chart.Chart chart1) = unsafePartial $ fromJust $ Array.head collection1
            Assert.equal (Just $ asChartLevel chartMappingWithChartLevel) chart1.chartLevel 
--}

