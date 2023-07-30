module Main where

import Prelude

import Effect (Effect)
import Effect.Class (liftEffect)
import Effect.Console (logShow)
import Effect.Aff  (Aff)
import Halogen.Aff as HA
import Halogen.VDom.Driver (runUI)
import Web.DOM.ParentNode (QuerySelector(..))
import Web.HTML (HTMLElement)
import Web.DOM.ParentNode (QuerySelector(..))
import Data.Maybe (Maybe(..))

import HarborView.Maunaloa.LevelLine 
  ( Line(..)
  , clear
  )
import HarborView.Maunaloa.Core as Core
import HarborView.Maunaloa.View as View
import HarborView.Maunaloa.Common 
  ( ChartType(..)
  , StockTicker(..)
  , Take(..)
  , Drop(..)
  )

tmp :: Line -> Int
tmp (StdLine v) = 1
tmp (RiscLine v) = 2
tmp (BreakEvenLine v) = 3

{-
main :: Effect Unit
main = 
    paint 4  [] "-" 0 0 *>
    paintEmpty [] *>
    clearLevelLines 1  *> 
    resetCharts 
-}


run :: Maybe HTMLElement -> QuerySelector -> ChartType -> Aff Unit
run node qs c = 
  case node of 
    Nothing ->
      let 
        QuerySelector qs1 = qs
      in
      liftEffect (logShow $ "No such element: " <> qs1) 
    Just nodex ->
      runUI (View.component c) unit nodex *>
      pure unit

main :: Effect Unit
main = 
  HA.runHalogenAff $
    liftEffect (
      Core.paint EmptyChartType (StockTicker "-") (Drop 0) (Take 0) *>
      Core.paintEmpty EmptyChartType *>
      Core.clearLevelLines 1  *> 
      Core.resetCharts
    ) *>
    HA.awaitLoad *> 
    let 
      qs1 = QuerySelector "#ps-menubar-1"
      qs2 = QuerySelector "#ps-menubar-2"
      qs3 = QuerySelector "#ps-menubar-3"
    in
    HA.selectElement qs1 >>= \node ->
      run node qs1 DayChart *>
    HA.selectElement qs2 >>= \node ->
      run node qs2 WeekChart *>
    HA.selectElement qs3 >>= \node ->
      run node qs3 MonthChart 

{-
run :: Maybe HTMLElement -> QuerySelector -> Aff Unit
run node qs = 
  case node of 
      Nothing ->
          let 
            QuerySelector qs1 = qs
          in
          liftEffect (logShow $ "No such element: " <> qs1) 
      Just nodex ->
          -- runUI Button.component unit nodex *>
          pure unit

main :: Effect Unit
main = HA.runHalogenAff $
    HA.awaitLoad *> 
    let 
      qs1 = QuerySelector "#ps-main"
    in
    HA.selectElement qs1 >>= \node ->
      run node qs1

newtype Ax = Ax
  { a :: ChartHeight}

instance showAx :: Show Ax where
  show (Ax x) = "(Ax " <> show x <> ")"

tryMe :: Number -> Ax -> Maybe Ax
tryMe v (Ax {a: (ChartHeight h)}) = 
  let 
    axx = Ax { a: ChartHeight (v * h) }
  in
  Just axx

tryMes :: Number -> Array Ax -> Effect Unit
tryMes v axs = 
  let 
    tt = map (tryMe v) axs
  in
  logShow tt
 -}
