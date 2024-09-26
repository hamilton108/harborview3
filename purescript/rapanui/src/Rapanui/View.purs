module Rapanui.View where

--import Prelude

import Effect.Aff.Class (class MonadAff)
import Halogen as H
import Halogen.HTML as HH
import Rapanui.Command (MainAction, handleAction)
import Rapanui.State (State, defaultState)

component :: forall q i o m. MonadAff m => H.Component q i o m
component =
  H.mkComponent
    { initialState: \_ ->
        defaultState
    , render
    , eval: H.mkEval H.defaultEval { handleAction = handleAction }
    }

render :: ∀ s m. MonadAff m => State -> H.ComponentHTML MainAction s m
render st =
  HH.div [] []