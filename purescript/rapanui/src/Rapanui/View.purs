module Rapanui.View where

import Prelude

import Effect.Aff.Class (class MonadAff)
import Halogen as H
import Halogen.HTML (ClassName(..), HTML)
import Halogen.HTML as HH
import HarborView.UI.Button (ButtonParams, mkButton)
import HarborView.UI.Common (Title(..))
import Rapanui.Command (MainAction(..), handleAction)
import Rapanui.State (State, defaultState)
import Web.UIEvent.MouseEvent (MouseEvent)

defaultButtonParams :: forall i. String -> (MouseEvent -> i) -> ButtonParams i
defaultButtonParams t curEvt =
  let
    clazz =
      "ps-btn btn btn-outline-success"
  in
    { title: Title t
    , evt: curEvt
    , btnClazz: [ ClassName clazz ]
    , disabled: false
    }

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
  HH.div [] [ mkButton $ defaultButtonParams "Demo" Demo ]

{-
[
  {
    "ticker": "NHY9E30",
    "oid": 47,
    "critters": [
      {
        "vol": 10,
        "accRules": [
          {
            "value": 3,
            "active": true,
            "pid": 47,
            "oid": 72,
            "cid": 45,
            "rtyp": 1
          },
          {
            "value": 2,
            "active": false,
            "pid": 47,
            "oid": 73,
            "cid": 45,
            "rtyp": 7
          }
        ],
        "status": 7,
        "oid": 45
      }
    ],
    "price": 5.8
  }
]
-}