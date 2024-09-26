module Rapanui.Command
  ( MainAction
  , handleAction
  ) where

import Effect.Aff.Class (class MonadAff)
import Halogen as H
import Rapanui.State (State)

import Prelude

data MainAction = Initialize

handleAction
  :: forall cs o m
   . MonadAff m
  => MainAction
  -> H.HalogenM State MainAction cs o m Unit
handleAction = case _ of
  Initialize ->
    pure unit