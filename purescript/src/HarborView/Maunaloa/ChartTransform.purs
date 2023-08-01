module HarborView.Maunaloa.ChartTransform where
  
import Prelude


{-
import Effect.Class 
    ( liftEffect
    )
import Effect (Effect)
import Effect.Aff 
    ( Aff
    , launchAff_
    )

import Affjax as Affjax
import Affjax.ResponseFormat as ResponseFormat
-}

--import Data.Either (Either(..))
import Data.Array 
  ( take
  , drop
  , filter
  , concat
  , (:)
  )

--import Data.Tuple (fst,snd)
--import Data.Nullable 
--    ( Nullable
--    )
import Control.Monad.Reader 
  ( Reader 
  , ask 
  )
import Data.Foldable 
  ( minimum
  , maximum
  , minimumBy
  , maximumBy
  )
import Data.Ord
  ( abs
  )
import Partial.Unsafe 
  ( unsafePartial
  )
import Data.Maybe 
  ( Maybe(..)
  , fromJust 
  , fromMaybe
  )

import HarborView.Maunaloa.Common as Common
import HarborView.Maunaloa.Common 
  ( ChartId(..)
  , ChartMapping(..)
  , ChartType(..)
  , ChartWidth
  , Drop(..)
  , Env(..)
  , Scaling(..)
  , StockTicker(..)
  , Take(..)
  , UnixTime(..)
  , ValueRange(..)
  )
import HarborView.Maunaloa.JsonCharts 
  ( JsonChartResponse 
  , JsonChart
  , JsonChartWindow(..)
  , JsonCandlestick
  )
import HarborView.Maunaloa.Chart as Chart
import HarborView.Maunaloa.Chart
  ( Chart(..)
  )
import HarborView.Maunaloa.ChartCollection
  ( ChartCollection(..)
  , EmptyChartCollection(..)
  )
import HarborView.Maunaloa.Line
  ( lineToPix
  )
import HarborView.Maunaloa.Candlestick
  ( candleToPix
  )
import HarborView.Maunaloa.Bar
  ( Bars
  , barToPix
  )
import HarborView.Maunaloa.HRuler as H

nullValueRange :: ValueRange
nullValueRange = ValueRange { minVal: 0.0, maxVal: 0.0 }

days :: String -> String
days ticker =
  Common.mainURL <> "/days/" <> ticker

slice :: forall a. Drop -> Take -> Array a -> Array a
slice (Drop dropAmount) (Take takeAmount) vals =
  if dropAmount == 0 then
    take takeAmount vals
  else
    take takeAmount $ drop dropAmount vals


minMaxCndl :: Array JsonCandlestick -> Maybe ValueRange
minMaxCndl cndl = 
  minimumBy (\x y -> if x.l < y.l then LT else GT) cndl >>= \mib ->
  maximumBy (\x y -> if x.h > y.h then GT else LT) cndl >>= \mab ->
  Just $ ValueRange { minVal: mib.l, maxVal: mab.h }

minMaxArray :: Array Number -> Maybe ValueRange
minMaxArray ar = 
  minimum ar >>= \mib ->
  maximum ar >>= \mab ->
  Just $ ValueRange { minVal: mib, maxVal: mab }

minMaxRanges :: Scaling -> Array (Maybe ValueRange) -> ValueRange
minMaxRanges (Scaling scale) vals = 
  let 
    mmas = map (\y -> fromMaybe nullValueRange y) $ filter (\x -> x /= Nothing) vals
    result = 
      fromMaybe nullValueRange $
        minimumBy (\(ValueRange x) (ValueRange y) -> if x.minVal < y.minVal then LT else GT) mmas >>= \mib ->
        maximumBy (\(ValueRange x) (ValueRange y) -> if x.maxVal > y.maxVal then GT else LT) mmas >>= \mab ->
          let 
            (ValueRange mibx) = mib
            (ValueRange mabx) = mab
          in
          Just $ ValueRange { minVal: mibx.minVal / scale, maxVal: mabx.maxVal * scale}
  in
  result 

normalizeLine :: Array Number -> Array Number
normalizeLine line = 
  let 
    (ValueRange vr) = fromMaybe (Common.valueRange (-1.0) 1.0) $ minMaxArray line
    scalingFactor = max (abs vr.minVal) vr.maxVal
    scalingFn x = x / scalingFactor
  in
  map scalingFn line


chartValueRange ::
  Array (Array Number)
  -> Array (Array Number)
  -> Array JsonCandlestick
  -> Scaling
  -> ValueRange 
chartValueRange lx bars cx scaling =
  let 
    minMaxLines = 
      map minMaxArray lx 

    minMaxBars = 
      map minMaxArray bars

    minMaxCandlesticks = 
      minMaxCndl cx

    allVr = minMaxCandlesticks : (concat [minMaxLines,minMaxBars]) 
  in
  minMaxRanges scaling allVr


chartWindowBar_ :: Drop -> Take -> Bars -> Scaling -> Int -> JsonChartWindow
chartWindowBar_ dropAmt takeAmt c scaling numVlines =
  let
    bars_ =
      map (slice dropAmt takeAmt) c

    valueRange =
      chartValueRange [] bars_ [] scaling 

  in
  JsonChartWindowBar
  { bars: bars_
  , valueRange: valueRange
  , numVlines: numVlines 
  }

chartWindow_ :: Drop -> Take -> JsonChart -> Scaling -> Boolean -> Int -> JsonChartWindow
chartWindow_ dropAmt takeAmt c scaling doNormalizeLines numVlines =
  let
    lines_ = 
      let 
        tmp = map (slice dropAmt takeAmt) $ fromMaybe [] c.lines
      in
      if doNormalizeLines == true then
        map normalizeLine tmp
      else
        tmp

    bars_ =
      map (slice dropAmt takeAmt) $ fromMaybe [] c.bars

    cndl_ =
      slice dropAmt takeAmt $ fromMaybe [] c.candlesticks

    valueRange =
      chartValueRange lines_ [] cndl_ scaling 

  in
  JsonChartWindow
  { lines: lines_
  , candlesticks: cndl_
  , valueRange: valueRange
  , numVlines: numVlines 
  }


chartWindow :: Drop -> Take -> JsonChart -> Scaling -> Boolean -> Int -> JsonChartWindow
chartWindow dropAmt takeAmt c scaling doNormalizeLines numVlines =
  let 
    bars = fromMaybe [] c.bars
  in
  if bars == [] then
    chartWindow_ dropAmt takeAmt c scaling doNormalizeLines numVlines 
  else
    chartWindowBar_ dropAmt takeAmt bars scaling numVlines 

transformMapping1 :: JsonChartWindow -> ChartWidth -> ChartMapping -> Chart 
transformMapping1 (JsonChartWindow ec) chartWidth cm@(ChartMapping mapping) = 
  let 
    h = mapping.chartHeight
    vr = Chart.vruler ec.valueRange chartWidth h
    linesToPix = map (lineToPix vr) ec.lines
    cndlToPix = map (candleToPix vr) ec.candlesticks
  in
  Chart 
    { lines: linesToPix
    , candlesticks: cndlToPix
    , bars: []
    , vruler: vr 
    , w: chartWidth 
    , mapping: cm
    }
transformMapping1 (JsonChartWindowBar ec) chartWidth cm@(ChartMapping mapping) =
  let 
    h = mapping.chartHeight
    vr = Chart.vruler (Common.valueRangeZeroBased ec.valueRange) chartWidth h
    barsToPix = map (barToPix vr) ec.bars
  in
  Chart 
    { lines: []
    , candlesticks: []
    , bars: barsToPix
    , vruler: vr 
    , w: chartWidth 
    , mapping: cm
    }
transformMapping1 JsonChartWindowEmpty _ _ = EmptyChart


transformMapping :: forall r. 
  { globalChartWidth :: ChartWidth
  , dropAmt :: Drop 
  , takeAmt :: Take  
  , scaling :: Scaling | r } 
  -> JsonChartResponse 
  -> ChartMapping 
  -> Chart 
transformMapping env response cm@(ChartMapping mapping) =
  let 
    chartWidth = env.globalChartWidth 
    dropAmt = env.dropAmt 
    takeAmt = env.takeAmt 
    scaling = env.scaling
  in
  case mapping.chartId of
    ChartId "chart" -> 
      let 
        cw = chartWindow dropAmt takeAmt response.chart scaling false 10
      in
      transformMapping1 cw chartWidth cm 
    ChartId "chart2" -> 
      let 
        cw = chartWindow dropAmt takeAmt response.chart2 (Scaling 1.00) true 10
      in
      transformMapping1 cw chartWidth cm 
    ChartId "chart3" -> 
      let 
        cw = chartWindow dropAmt takeAmt response.chart3 (Scaling 1.00) false 10
      in
      transformMapping1 cw chartWidth cm 
    _ -> 
        EmptyChart


incMonths :: ChartType -> Int
incMonths DayChart = 1
incMonths WeekChart = 3
incMonths MonthChart = 6
incMonths EmptyChartType = 0

transform :: JsonChartResponse -> Reader Env ChartCollection
transform response = 
  ask >>= \(Env env) ->
  let 
    xaxis = slice env.dropAmt env.takeAmt response.xAxis
    tm = UnixTime response.minDx 
    ruler = H.create env.globalChartWidth tm xaxis Chart.padding (incMonths env.chartType)
    ruler1 = unsafePartial (fromJust ruler)
    charts1 = map (transformMapping env response) env.mappings
  in
  pure $ 
  ChartCollection 
    { ticker: (StockTicker response.ticker)
    , charts: charts1
    , hruler: ruler1 
    }

transformMappingEmpty :: ChartWidth -> ChartMapping -> Chart 
transformMappingEmpty chartWidth (ChartMapping mapping) =
  ChartWithoutTicker 
    { canvasId: mapping.canvasId 
    , w: chartWidth 
    , h: mapping.chartHeight 
    }

transformEmpty :: Reader Env EmptyChartCollection
transformEmpty = 
  ask >>= \(Env env) ->
  let 
    charts = map (transformMappingEmpty env.globalChartWidth) env.mappings
  in
  pure $ EmptyChartCollection charts


{-
demo = 
    let 
        padding = Padding { bottom: 0.0, left: 50.0, right: 50.0, top: 0.0}
        xaxis = [4226,4225,4224,4221,4220,4219,4218,4217,4214,4213,4212,4211,4210,4207,4206,4205,4204,4203,4200,4199,4198,4197,
                4196,4193,4192,4191,4190,4189,4186,4185,4184,4183,4182,4179,4178,4177,4176,4175,4172,4171,4170,4169,4168,4165,
                4164,4163,4162,4158,4157,4156,4155,4151,4149,4148,4147,4144,4143,4142,4141,4140,4137,4136,4135,4134,4133,4130,
                4129,4128,4127,4126,4123,4122,4121,4120,4119,4116,4115,4114,4113,4107,4106,4105,4102,4101,4100,4099,4098,4095,
                4094,4093]
        tm = UnixTime 1262304000000.0
        ruler = H.create globalChartWidth tm xaxis padding
    in 
    logShow ruler   
-}