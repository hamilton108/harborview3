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
import HarborView.Maunaloa.Common 
  ( ChartMapping(..)
  , ChartMappings
  , Drop(..)
  , Take(..)
  , Env(..)
  , StockTicker(..)
  , ChartType(..)
  , ChartWidth(..)
  , Scaling(..)
  , asChartType
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
  , chartType: asChartType 1 
  , mappings: mappings
  , globalChartWidth: ChartWidth 1750.0
  , scaling: Scaling 1.0
  }


reposIdFor :: Int -> String -> String 
reposIdFor chartTypeId ticker = 
  ticker <> ":" <> (toString $ toNumber chartTypeId)

resetChart :: Int -> String -> Effect Unit
resetChart chartTypeId ticker = 
  let
    reposId = reposIdFor chartTypeId ticker 
  in
  Repository.resetChart reposId

resetCharts :: Effect Unit
resetCharts = 
  Repository.resetCharts 

paint :: Int -> ChartMappings -> String -> Int -> Int -> Effect Unit
paint chartTypeId mappings ticker dropAmt takeAmt = 
  if chartTypeId == 4 then
    pure unit
  else
    let
      curTicker = StockTicker ticker 
      curChartType = asChartType chartTypeId
      curEnv = createEnv curChartType curTicker (Drop dropAmt) (Take takeAmt) mappings
      reposId = reposIdFor chartTypeId ticker 
      cachedResponse =  Repository.getJsonResponse reposId
    in 
    logShow ("StockTicker: " <> ticker) *>
    case cachedResponse of 
      Just cachedResponse1 ->
        let 
          collection = runReader (ChartTransform.transform cachedResponse1) curEnv
        in
        logShow "Fetched response from repository" *>
        ChartCollection.paint curChartType collection
      Nothing ->
        launchAff_ $
          fetchCharts curTicker curChartType >>= \charts ->
            case charts of 
              Left err ->
                handleErrorAff err 
              Right jsonChartResponse ->
                let 
                  collection = runReader (ChartTransform.transform jsonChartResponse) curEnv
                in
                (liftEffect $ Repository.setJsonResponse reposId jsonChartResponse) *>
                ChartCollection.paintAff curChartType collection


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

  {-
chartTypeAsMapping :: ChartType -> ChartMapping
chartTypeAsMapping DayChart = 
  ChartMapping 
  { chartId :: 
  , canvasId :: HtmlId
  , chartHeight :: ChartHeight 
  , levelCanvasId :: HtmlId
  , addLevelId :: HtmlId
  , fetchLevelId :: HtmlId
  }

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
