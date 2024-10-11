module Rapanui.State
  ( State
  , defaultState
  ) where

--import Prelude

import Rapanui.Critter.Rules (Critter)

type State =
  { critters :: Array Critter
  }

defaultState :: State
defaultState =
  { critters: []
  }