module HarborView.Maunaloa.Core where

import Prelude

import Control.Monad.Reader (runReader)
import Data.Either (Either(..))
import Data.Int (toNumber)
import Data.Number.Format (toString)
import Data.Maybe (Maybe(..))
import Effect (Effect)
import Effect.Aff  (launchAff_, Aff)
import Effect.Class (liftEffect)
import Effect.Console (logShow)
import Halogen.Aff as HA


import HarborView.Maunaloa.LevelLine 
  ( Line(..)
  , clear
  )
import HarborView.Maunaloa.ChartTransform as ChartTransform 
import HarborView.Maunaloa.JsonCharts (fetchCharts)
import HarborView.Maunaloa.MaunaloaError (handleErrorAff)
import HarborView.Maunaloa.Repository as Repository 
import HarborView.Maunaloa.Common as Common
import HarborView.Maunaloa.Common 
  ( ChartMapping(..)
  , ChartMappings
  , ChartHeight(..)
  , ChartId(..)
  , ChartType(..)
  , Drop(..)
  , Take(..)
  , Env(..)
  , HtmlId(..)
  , StockTicker(..)
  , ChartType(..)
  , ChartWidth(..)
  , Scaling(..)
  )
import HarborView.Maunaloa.ChartCollection 
  as ChartCollection 

createEnv :: ChartType -> StockTicker -> Drop -> Take -> ChartMappings -> Env
createEnv ctype tik curDrop curTake mappings = 
  Env
  { ticker: tik 
  , dropAmt: curDrop
  , takeAmt: curTake
  , chartType: ctype 
  , mappings: mappings
  , globalChartWidth: ChartWidth 1750.0
  , scaling: Scaling 1.1
  }

createEnvEmpty :: ChartMappings -> Env
createEnvEmpty mappings = 
  Env
  { ticker: StockTicker "-"
  , dropAmt: Drop 0 
  , takeAmt: Take 0
  , chartType: DayChart 
  , mappings: mappings
  , globalChartWidth: ChartWidth 1750.0
  , scaling: Scaling 1.0
  }


reposIdFor :: ChartType -> StockTicker -> String 
reposIdFor chartType (StockTicker ticker) = 
  ticker <> ":" <> (toString $ toNumber $ Common.chartTypeAsInt chartType)

resetChart :: ChartType -> StockTicker -> Effect Unit
resetChart chartType ticker = 
  let
    reposId = reposIdFor chartType ticker 
  in
  Repository.resetChart reposId

resetCharts :: Effect Unit
resetCharts = 
  Repository.resetCharts 

paint :: ChartType -> ChartMappings -> StockTicker -> Drop -> Take -> Effect Unit
paint EmptyChartType _ _ _ _ = 
  pure unit
paint chartType mappings ticker dropAmt takeAmt = 
    let
      curEnv = createEnv chartType ticker dropAmt takeAmt mappings
      reposId = reposIdFor chartType ticker 
      cachedResponse =  Repository.getJsonResponse reposId
      (StockTicker tickerS) = ticker
    in 
    logShow ("StockTicker: " <> tickerS) *>
    case cachedResponse of 
      Just cachedResponse1 ->
        let 
          collection = runReader (ChartTransform.transform cachedResponse1) curEnv
        in
        logShow "Fetched response from repository" *>
        ChartCollection.paint chartType collection
      Nothing ->
        launchAff_ $
          fetchCharts ticker chartType >>= \charts ->
            case charts of 
              Left err ->
                handleErrorAff err 
              Right jsonChartResponse ->
                let 
                  collection = runReader (ChartTransform.transform jsonChartResponse) curEnv
                in
                (liftEffect $ Repository.setJsonResponse reposId jsonChartResponse) *>
                ChartCollection.paintAff chartType collection


paintEmpty :: ChartMappings -> Effect Unit
paintEmpty mappings = 
  let 
    curEnv = createEnvEmpty mappings
    collection = runReader (ChartTransform.transformEmpty) curEnv
  in
  let 
    (ChartCollection.EmptyChartCollection coll) = collection
  in
  logShow "__paintEmpty__" *>
  logShow coll *>
  ChartCollection.paintEmpty collection

clearLevelLines :: Int -> Effect Unit
clearLevelLines cti =
  logShow "clearLevelLines" *>
  clear cti

chartTypeAsMappings :: ChartType -> ChartMappings
chartTypeAsMappings DayChart = 
  let 
    mainChart = 
      ChartMapping 
      { chartId: ChartId "chart"
      , canvasId: HtmlId "chart-1"
      , chartHeight: ChartHeight 600.0
      , levelCanvasId: HtmlId "levellines-1"
      , addLevelId: HtmlId "btn-levelline-1"
      , fetchLevelId: HtmlId "btn-persistent-levelline-1"
      }
    osc = 
      ChartMapping 
      { chartId: ChartId "chart2"
      , canvasId: HtmlId "osc-1"
      , chartHeight: ChartHeight 200.0
      , levelCanvasId: HtmlId ""
      , addLevelId: HtmlId ""
      , fetchLevelId: HtmlId ""
      }
    volume = 
      ChartMapping 
      { chartId: ChartId "chart3"
      , canvasId: HtmlId "vol-1"
      , chartHeight: ChartHeight 110.0
      , levelCanvasId: HtmlId ""
      , addLevelId: HtmlId ""
      , fetchLevelId: HtmlId ""
      }
  in 
  [ mainChart, osc, volume ]
chartTypeAsMappings WeekChart = 
  let 
    mainChart = 
      ChartMapping 
      { chartId: ChartId "chart"
      , canvasId: HtmlId "chart-2"
      , chartHeight: ChartHeight 600.0
      , levelCanvasId: HtmlId "levellines-2"
      , addLevelId: HtmlId "btn-levelline-2"
      , fetchLevelId: HtmlId "btn-persistent-levelline-2"
      }
    osc = 
      ChartMapping 
      { chartId: ChartId "chart2"
      , canvasId: HtmlId "osc-2"
      , chartHeight: ChartHeight 200.0
      , levelCanvasId: HtmlId ""
      , addLevelId: HtmlId ""
      , fetchLevelId: HtmlId ""
      }
    volume = 
      ChartMapping 
      { chartId: ChartId "chart3"
      , canvasId: HtmlId "vol-2"
      , chartHeight: ChartHeight 110.0
      , levelCanvasId: HtmlId ""
      , addLevelId: HtmlId ""
      , fetchLevelId: HtmlId ""
      }
  in 
  [ mainChart, osc, volume ]
chartTypeAsMappings MonthChart = 
  let 
    mainChart = 
      ChartMapping 
      { chartId: ChartId "chart"
      , canvasId: HtmlId "chart-3"
      , chartHeight: ChartHeight 600.0
      , levelCanvasId: HtmlId "levellines-3"
      , addLevelId: HtmlId "btn-levelline-3"
      , fetchLevelId: HtmlId "btn-persistent-levelline-3"
      }
    osc = 
      ChartMapping 
      { chartId: ChartId "chart2"
      , canvasId: HtmlId "osc-3"
      , chartHeight: ChartHeight 200.0
      , levelCanvasId: HtmlId ""
      , addLevelId: HtmlId ""
      , fetchLevelId: HtmlId ""
      }
    volume = 
      ChartMapping 
      { chartId: ChartId "chart3"
      , canvasId: HtmlId "vol-3"
      , chartHeight: ChartHeight 110.0
      , levelCanvasId: HtmlId ""
      , addLevelId: HtmlId ""
      , fetchLevelId: HtmlId ""
      }
  in 
  [ mainChart, osc, volume ]
chartTypeAsMappings EmptyChartType = 
  let 
    mainChart = 
      ChartMapping 
      { chartId: ChartId "chart"
      , canvasId: HtmlId "chart-3"
      , chartHeight: ChartHeight 600.0
      , levelCanvasId: HtmlId "levellines-3"
      , addLevelId: HtmlId "btn-levelline-3"
      , fetchLevelId: HtmlId "btn-persistent-levelline-3"
      }
    osc = 
      ChartMapping 
      { chartId: ChartId "chart2"
      , canvasId: HtmlId "osc-3"
      , chartHeight: ChartHeight 200.0
      , levelCanvasId: HtmlId ""
      , addLevelId: HtmlId ""
      , fetchLevelId: HtmlId ""
      }
    volume = 
      ChartMapping 
      { chartId: ChartId "chart3"
      , canvasId: HtmlId "vol-3"
      , chartHeight: ChartHeight 110.0
      , levelCanvasId: HtmlId ""
      , addLevelId: HtmlId ""
      , fetchLevelId: HtmlId ""
      }
  in 
  [ mainChart, osc, volume ]



  {-
        DAY: {
            MAIN_CHART: 'chart-1',
            VOLUME: 'vol-1',
            OSC: 'osc-1',
            LEVEL_LINES: 'levellines-1',
            BTN_LEVELLINE: "btn-levelline-1",
            BTN_RISCLINES: "btn-persistent-levelline-1",
            BTN_DEL_RISCLINES: "btn-del-persistent-levelline-1"
        },
        WEEK: {
            MAIN_CHART: 'chart-2',
            VOLUME: 'vol-2',
            OSC: 'osc-2',
            LEVEL_LINES: 'levellines-2',
            BTN_LEVELLINE: "btn-levelline-2",
            BTN_RISCLINES: "btn-persistent-levelline-2",
            BTN_DEL_RISCLINES: "btn-del-persistent-levelline-2"
        },
        MONTH: {
            MAIN_CHART: 'chart-3',
            VOLUME: 'vol-3',
            OSC: 'osc-3',
            LEVEL_LINES: 'levellines-3',
            BTN_LEVELLINE: "btn-levelline-3",
            BTN_RISCLINES: "btn-persistent-levelline-3",
            BTN_DEL_RISCLINES: "btn-del-persistent-levelline-3"
        }
    };
-}
