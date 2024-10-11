module Rapanui.State
  ( State
  , defaultState
  ) where

--import Prelude

import Rapanui.Critter.Rules (StockOption)

type State =
  { stockOptions :: Array StockOption
  }

defaultState :: State
defaultState =
  { stockOptions: []
  }