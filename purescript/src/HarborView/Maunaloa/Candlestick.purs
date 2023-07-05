module HarborView.Maunaloa.Candlestick where

import Prelude

import Effect (Effect)
import Graphics.Canvas (Context2D)

import HarborView.Maunaloa.Common (Xaxis)

import HarborView.Maunaloa.VRuler as V
import HarborView.Maunaloa.HRuler as H
import HarborView.Maunaloa.JsonCharts
  ( JsonCandlestick 
  )

foreign import fi_paint_candlestix :: Xaxis -> Candlesticks -> Context2D -> Effect Unit 

newtype Candlestick = Candlestick 
  { o :: Number
  , h :: Number
  , l :: Number
  , c :: Number
  }

instance showCandlestick :: Show Candlestick where
  show (Candlestick v) = "(Candlestick " <> show v <> ")"

derive instance eqCandlestick :: Eq Candlestick 

type Candlesticks = Array Candlestick

candleToPix :: V.VRuler -> JsonCandlestick -> Candlestick 
candleToPix vr {o,h,l,c} =  
    let 
        po = V.valueToPix vr o
        ph = V.valueToPix vr h
        pl = V.valueToPix vr l
        pc = V.valueToPix vr c
    in
    Candlestick { o: po, h: ph, l: pl, c: pc }

paint :: H.HRuler -> Candlesticks -> Context2D -> Effect Unit
paint (H.HRuler {xaxis: xaxis}) cndls ctx = 
  fi_paint_candlestix xaxis cndls ctx