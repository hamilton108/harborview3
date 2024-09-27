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

{-
[
  {
    "ticker":"NHY9E30",
    "oid":47,
    "price":5.8,
    "critters":
      [
        {
          "vol":10,
          "accRules":
          [
            {"value":3.0,"active":true,"pid":47,"oid":72,"cid":45,"rtyp":1},
            {"value":2.0,"active":false,"pid":47,"oid":73,"cid":45,"rtyp":7}
          ],
          "status":7,
          "oid":45
        }
      ]
  }
]
-}