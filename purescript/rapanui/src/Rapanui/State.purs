module Rapanui.State
  ( State
  , defaultState
  ) where

--import Prelude

import Rapanui.Critter.Rules (StockOptionPurchase)

type State =
  { stockOptions :: Array StockOptionPurchase
  }

defaultState :: State
defaultState =
  { stockOptions: []
  }