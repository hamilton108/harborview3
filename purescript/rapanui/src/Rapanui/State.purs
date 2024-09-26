module Rapanui.State
  ( State
  , defaultState
  ) where

--import Prelude

import Data.Maybe (Maybe(..))
import Rapanui.Critter.AcceptRule (AcceptRule)

type State =
  { accRule :: Maybe AcceptRule }

defaultState :: State
defaultState =
  { accRule: Nothing
  }