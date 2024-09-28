module Rapanui.Command
  ( MainAction(..)
  , handleAction
  ) where

import Prelude

import Data.Either (Either(..))
import Effect.Aff.Class (class MonadAff)
import Effect.Console (logShow)
import Effect.Class (liftEffect)
import Halogen as H
import HarborView.Common (handleError)
import Rapanui.Nordnet.Core as Core
import Rapanui.State (State)
import Web.UIEvent.MouseEvent (MouseEvent)

data MainAction
  = Initialize
  | Demo MouseEvent

handleAction
  :: forall cs o m
   . MonadAff m
  => MainAction
  -> H.HalogenM State MainAction cs o m Unit
handleAction = case _ of
  Initialize ->
    pure unit
  Demo _ ->
    H.liftAff Core.demo >>= \result ->
      case result of
        Left err ->
          liftEffect $ handleError err
        Right result1 ->
          liftEffect $ logShow result1
