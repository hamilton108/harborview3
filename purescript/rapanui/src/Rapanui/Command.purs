module Rapanui.Command
  ( MainAction(..)
  , handleAction
  ) where

import Prelude

import Control.Monad.State.Class (class MonadState)
import Data.Either (Either(..))
import Effect.Aff.Class (class MonadAff)
import Effect.Class (liftEffect)
import Effect.Console (logShow)
import Halogen as H
import HarborView.Common (handleError)
import Rapanui.Common (OptionTicker(..), Oid(..))
import Rapanui.Nordnet.Core as Core
import Rapanui.Nordnet.CoreJson (CritterResponse)
import Rapanui.Nordnet.Transform as Transform
import Rapanui.State (State)
import Web.UIEvent.MouseEvent (MouseEvent)

data MainAction
  = Initialize
  | Demo MouseEvent

mapJsonResult
  :: forall m
   . MonadState State m
  => MonadAff m
  => CritterResponse
  -> m Unit
mapJsonResult result =
  case result.payload of
    [] ->
      pure unit
    --[ x : xs ] ->
    items ->
      H.modify_
        \stx ->
          stx
            { stockOptions = []
            -- stockOptions = Transform.mapResponse items
            }

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
          (liftEffect $ logShow result1) *>
            mapJsonResult result1
