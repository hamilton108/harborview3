module HarborView.Maunaloa.Bar where

import Prelude

import Effect (Effect)
import Graphics.Canvas (Context2D)
import Data.Array (zipWith)

import HarborView.Maunaloa.Common (Xaxis)

import HarborView.Maunaloa.VRuler as V
import HarborView.Maunaloa.HRuler as H

foreign import fi_paint_bars :: Array JSBar -> Context2D -> Effect Unit 

type Bar = Array Number

type Bars = Array Bar

newtype JSBar = JSBar { 
    yaxis :: Bar 
  , xaxis :: Xaxis 
  , strokeStyle :: String }

barToPix :: V.VRuler -> Bar -> Bar
barToPix vr bar = 
  let
    vfun = V.valueToPix vr
  in
  map vfun bar

strokes :: Array String
strokes = [ "#ff0000" ]
-- strokes = [ "#ff0000", "#aa00ff" ]

createJSBar :: Xaxis -> Bar -> String -> JSBar 
createJSBar xaxis bar strokeStyle = 
  JSBar { xaxis: xaxis, yaxis: bar, strokeStyle: strokeStyle }

paint :: H.HRuler -> Bars -> Context2D -> Effect Unit
paint (H.HRuler {xaxis: xaxis}) bars ctx = 
  let 
    fn = createJSBar xaxis
    jsBars = zipWith fn bars strokes
  in
  fi_paint_bars jsBars ctx
