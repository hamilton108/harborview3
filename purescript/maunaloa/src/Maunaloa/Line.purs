module HarborView.Maunaloa.Line where

import Prelude 
import Data.Array (zipWith)
import Graphics.Canvas (Context2D)
import Effect (Effect)
--import Control.Monad.State (State,get,put,runState)

import HarborView.Maunaloa.VRuler as V
import HarborView.Maunaloa.HRuler as H
import HarborView.Maunaloa.Common (Xaxis)


type Line = Array Number

type Lines = Array Line

foreign import fi_paint_lines :: Array JSLine -> Context2D -> Effect Unit 


lineToPix :: V.VRuler -> Line -> Line 
lineToPix vr line = 
  let
    vfun = V.valueToPix vr
  in
  map vfun line

newtype JSLine = JSLine { 
    yaxis :: Line 
  , xaxis :: Xaxis 
  , strokeStyle :: String }


strokes :: Array String
strokes = [ "#ff0000", "#aa00ff" ]

createJsLine :: Xaxis -> Line -> String -> JSLine
createJsLine xaxis line strokeStyle = 
  JSLine { xaxis: xaxis, yaxis: line, strokeStyle: strokeStyle }

paint :: H.HRuler -> Lines -> Context2D -> Effect Unit
paint (H.HRuler {xaxis: xaxis}) lx ctx = 
  let 
    fn = createJsLine xaxis
    jsLines = zipWith fn lx strokes
  in
  fi_paint_lines jsLines ctx 


