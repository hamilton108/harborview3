module HarborView.Maunaloa.ChartCollection where

import Prelude

import Effect.Aff (Aff)
import Effect (Effect)
import Effect.Class (liftEffect)
import Effect.Console (logShow)
import Data.Traversable (traverse_)

import HarborView.Maunaloa.Chart as C
import HarborView.Maunaloa.Chart (Chart)
import HarborView.Maunaloa.HRuler as H
import HarborView.Maunaloa.Common 
  ( ChartType
  , StockTicker
  )


newtype ChartCollection = ChartCollection 
  { ticker :: StockTicker 
  , charts :: Array Chart -- List C.Chart
  , hruler :: H.HRuler
  }

instance showChartCollection :: Show ChartCollection where
  show (ChartCollection coll) = "(ChartCollection " <> show coll <> ")"

newtype EmptyChartCollection = EmptyChartCollection (Array Chart)

-- globalChartWidth :: ChartWidth
-- globalChartWidth = ChartWidth 1650.0

-- mappingToChartLevel :: ChartMapping -> Maybe ChartLevel 
-- mappingToChartLevel (ChartMapping {levelCanvasId, addLevelId, fetchLevelId}) = 
--     let 
--         (HtmlId lcaid) = levelCanvasId
--     in
--     if length lcaid == 0 then
--         Nothing
--     else
--         Just
--         { levelCanvasId: levelCanvasId
--         , addLevelId: addLevelId
--         , fetchLevelId: fetchLevelId
--         }


-- findChartPredicate :: Chart -> Boolean
-- findChartPredicate (Chart chart) =
--     chart.chartLevel /= Nothing
-- findChartPredicate (ChartWithoutTicker _) =
--     true
-- findChartPredicate EmptyChart =
--     false

-- findLevelLineChart :: Array Chart -> Maybe Chart
-- findLevelLineChart charts = 
--     Array.find findChartPredicate charts

-- levelLines :: ChartType -> StockTicker -> Array Chart -> Effect Unit
-- levelLines ct ticker charts = 
--     let
--         levelLine = Array.find findChartPredicate charts
--     in
--     case levelLine of 
--         Nothing ->
--             logShow "ERROR! (levelLines) No levelLine!" *>
--             pure unit
--         Just (Chart levelLine1) ->
--             let 
--                 caid = unsafePartial $ fromJust levelLine1.chartLevel
--             in
--             LevelLine.initEvents ct ticker levelLine1.vruler caid
--         _ ->
--             pure unit

paint :: ChartType -> ChartCollection -> Effect Unit
paint ct (ChartCollection coll) = 
  let 
    paint_ = C.paint coll.hruler ct
  in
  traverse_ paint_ coll.charts 
  -- levelLines ct coll.ticker coll.charts

paintAff :: ChartType -> ChartCollection -> Aff Unit
paintAff ct coll = 
  liftEffect $ paint ct coll
    
paintEmpty :: EmptyChartCollection -> Effect Unit
paintEmpty (EmptyChartCollection coll) = 
  logShow coll *>
  traverse_ C.paintEmpty coll