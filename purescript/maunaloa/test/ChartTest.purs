module Test.ChartTest where

import Prelude

import Test.Unit.Assert as Assert
import Test.Unit (suite, test, TestSuite)
import Data.Maybe (fromJust)
import Data.Array as Array

import Partial.Unsafe (unsafePartial)

import HarborView.Maunaloa.Common 
  ( ValueRange(..)
  , HtmlId(..)
  , ChartWidth(..)
  , ChartHeight(..)
  , ChartId(..)
  , ChartMapping(..)
  )
--import Util.Value (foreignValue)
import HarborView.Maunaloa.Chart 
    ( Chart(..)
    , valueRangeFor 
    )

import HarborView.Maunaloa.Line as L
import Test.VRulerTest as VT -- (moreOrLessEq,chartDim,pad0,pad1)

cid :: ChartId
cid = ChartId "chart"

canvId :: HtmlId
canvId = HtmlId "canvasId"

chartW :: ChartWidth
chartW = ChartWidth 200.0

chartH :: ChartHeight
chartH = ChartHeight 600.0

testMapping :: ChartMapping
testMapping = ChartMapping
  { chartId: cid
  , canvasId: canvId
  , chartHeight: chartH
  , levelCanvasId: HtmlId ""
  }

echart :: Chart
echart = Chart {
    lines: [[360.0,600.0,330.0,0.0,210.0]]
  , candlesticks: []
  , bars: []
  , vruler : VT.testVRuler 
  , w: chartW
  , mapping: testMapping 
  --, h: chartH
  --, canvasId: canvId
  --, chartLevel: Nothing 
}

getLines :: Chart -> L.Lines
getLines (Chart {lines}) = lines
getLines (ChartWithoutTicker _) = []
getLines EmptyChart = []

getLine :: Chart -> L.Line
getLine c =  
  let
    lx = getLines c
  in
  unsafePartial $ fromJust $ Array.head lx

testChartSuite :: TestSuite
testChartSuite = 
    suite "TestChartSuite" do
        test "valueRangeFor" do
            let vr = valueRangeFor [10.0,35.0]
            let expVr = ValueRange { minVal: 10.0, maxVal: 35.0 }
            Assert.equal expVr vr
        {--
        test "readChart chart2 and chart3 are null" do
            let chart = runExcept $ C.readChart cid canvId chartW chartH Nothing TC.demox 
            let rchart = unsafePartial $ fromRight chart
            Assert.equal true $ isRight chart
            let rline = getLine rchart
            let eline = getLine echart
            let result = Array.zipWith TC.moreOrLessEq rline eline
            Assert.equal [true,true,true,true,true] result
        test "readChart with ChartLevel" do
            let chart = runExcept $ C.readChart cid canvId chartW chartH (Just chartLevel) TC.demox 
            let rchart = unsafePartial $ fromRight chart
            let cl = getChartLevel rchart
            Assert.equal chartLevel cl
        test "Create HRuler" do
            let ruler = runExcept $ C.readHRuler chartW TC.demox
            Assert.equal true $ isRight ruler
            let mr = unsafePartial $ fromRight ruler
            Assert.equal true $ isJust mr
        --}


