module HarborView.Maunaloa.Chart where
  
import Prelude

import Data.Maybe (Maybe(..))
import Graphics.Canvas as Canvas -- (Context2D,Canvas)
import Effect (Effect)
import Effect.Console (logShow)

import HarborView.Maunaloa.Common 
  ( ValueRange(..)
  , Padding(..)
  , ChartWidth(..)
  , ChartHeight(..)
  , HtmlId(..)
  , ChartMapping(..)
  , Pix(..)
  , ChartId(..)
  , ChartType
  )
import HarborView.Maunaloa.HRuler as H
import HarborView.Maunaloa.VRuler as V
import HarborView.Maunaloa.Line as L
import HarborView.Maunaloa.Bar as Bar
import HarborView.Maunaloa.Candlestick as CNDL
import HarborView.Maunaloa.LevelLine as LevelLine 

type ChartContent = 
  { lines :: L.Lines
  , candlesticks :: CNDL.Candlesticks
  , bars :: Bar.Bars
  , vruler :: V.VRuler
  , w :: ChartWidth
  , mapping :: ChartMapping
  }

type ChartContent2 = 
  { canvasId :: HtmlId 
  , w :: ChartWidth
  , h :: ChartHeight
  }

data Chart 
  = Chart ChartContent
  | ChartWithoutTicker ChartContent2 
  | EmptyChart

emptyChart :: ChartContent
emptyChart = 
  { lines: []
  , candlesticks: []
  , bars: []
  --, canvasId: HtmlId ""
  , vruler: V.VRuler 
              { ppy: Pix 0.0
              , maxVal: 0.0
              , w: ChartWidth 0.0
              , h: ChartHeight 0.0
              , padding: 
                  Padding 
                  { left: 0.0
                  , top: 0.0
                  , right: 0.0
                  , bottom: 0.0
                  }
              }
  , w: ChartWidth 0.0
  --, h: ChartHeight 0.0
  --, chartLevel: Nothing
  , mapping : ChartMapping 
                { chartId: ChartId "-"
                , canvasId: HtmlId ""
                , chartHeight: ChartHeight 0.0
                , levelCanvasId: HtmlId ""
                }
  }

derive instance eqChart :: Eq Chart

instance showChart :: Show Chart where
  show (Chart cx) = 
    "(Chart lines: " <> show cx.lines <> 
    ", candlesticks: " <> show cx.candlesticks <> 
    --", canvasId: " <> show cx.canvasId <> 
    ", vruler: " <> show cx.vruler <> 
    ", w: " <> show cx.w <> ")"
    --", h: " <> show cx.h <> 
    --", chartLevel: " <> show cx.chartLevel <> ")"
  show (ChartWithoutTicker cx) = 
    "(ChartWithoutTicker : " <> 
    ", canvasId: " <> show cx.canvasId <> 
    ", w: " <> show cx.w <> 
    ", h: " <> show cx.h <> ")"
  show (EmptyChart) = 
    "(EmptyChart)" 


{-

chartWidth :: ChartWidth 
chartWidth = ChartWidth 1200.0 
-}

padding :: Padding 
padding = Padding { left: 50.0, top: 0.0, right: 50.0, bottom: 0.0 }

vruler :: ValueRange -> ChartWidth -> ChartHeight -> V.VRuler
vruler vr w h = V.create vr w h padding

valueRangeFor :: Array Number -> ValueRange
valueRangeFor [mi,ma] = ValueRange { minVal: mi, maxVal: ma }
valueRangeFor _ = ValueRange { minVal: 0.0, maxVal: 0.0 }

toRectangle :: Chart -> Canvas.Rectangle
toRectangle (Chart {w: (ChartWidth w), mapping : (ChartMapping m)} ) =  
  let 
    ChartHeight h = m.chartHeight
  in
  { x: 0.0, y: 0.0, width: w, height: h } 
toRectangle (ChartWithoutTicker {w: (ChartWidth w), h: (ChartHeight h)} ) =  
  { x: 0.0, y: 0.0, width: w, height: h } 
toRectangle EmptyChart = 
  { x: 0.0, y: 0.0, width: 0.0, height: 0.0 } 

paint :: H.HRuler -> ChartType -> Chart -> Effect Unit
paint hruler ct chart@(Chart {vruler: vrobj@(V.VRuler vr), mapping : (ChartMapping m), lines: lines, candlesticks: candlesticks, bars: bars}) =
  case m.canvasId of 
    NoHtmlId -> 
      pure unit
    HtmlId curId ->
      Canvas.getCanvasElementById curId >>= \canvas ->
          case canvas of
              Nothing -> 
                  logShow $ "CanvasId " <> curId <> " does not exist!"
              Just canvas1 ->
                  logShow ("Drawing canvas: " <> curId) *>
                  Canvas.getContext2D canvas1 >>= \ctx ->
                      let 
                          r = toRectangle chart
                      in
                      Canvas.clearRect ctx r *>
                      V.paint vrobj ctx *>
                      H.paint hruler vr.h ctx *>
                      L.paint hruler lines ctx *>
                      Bar.paint hruler bars ctx *>
                      CNDL.paint hruler candlesticks ctx *>
                      if m.levelCanvasId /= NoHtmlId then
                        LevelLine.updateVruler ct vrobj
                      else
                        pure unit
paint _ _ _ = 
    pure unit

paintEmpty :: Chart -> Effect Unit  
paintEmpty chart@(ChartWithoutTicker {canvasId: (HtmlId curId)}) = 
    Canvas.getCanvasElementById curId >>= \canvas ->
        case canvas of
            Nothing -> 
                logShow $ "CanvasId " <> curId <> " does not exist!"
            Just canvas1 ->
                logShow ("Drawing canvas: " <> curId) *>
                Canvas.getContext2D canvas1 >>= \ctx ->
                    let 
                        r = toRectangle chart
                    in
                    Canvas.clearRect ctx r
paintEmpty _ =
    pure unit

