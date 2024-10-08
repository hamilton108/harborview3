module Rapanui.Nordnet.Transform
  ( mapCritter
  , mapResponse
  ) where

import Prelude

import Data.Maybe (Maybe(..))
import Rapanui.Common (Oid(..), Pid(..), Cid(..), Rtyp(..))
import Rapanui.Critter.Rules (Critter, AcceptRule)
import Rapanui.Nordnet.CoreJson (CritterResponse, JsonAccRule, JsonCritter)
import Rapanui.State (State, defaultState)

{-
mapAccRule :: Array JsonAccRule -> AcceptRule
mapAccRule { oid, pid, cid, rtyp, value, active } =
  { oid: Oid oid
  , pid: Pid pid
  , cid: Cid cid
  , rtyp: Rtyp rtyp
  , value: value
  , active: active
  }
  -}

mapCritter :: JsonCritter -> Critter
mapCritter jc =
  { oid: Oid 1
  , vol: 10
  , status: 7
  , accRules: []
  }

mapResponse :: CritterResponse -> State
mapResponse response =
  defaultState

{-
  case response.payload of
    [] ->
      defaultState
    ->
      defaultState

      let
        critters = map mapCritter p1.
      in
        defaultState
-}

--{ accRule: Just $ mapAccRule response.payload.accRule }

{-
{
  "appStatusCode": 1,
  "error": null,
  "payload": [
    {
      "ticker": "NHY9E30",
      "oid": 47,
      "price": 5.8,
      "critters": [
        {
          "vol": 10,
          "status": 7,
          "oid": 45,
          "accRules": [
            {
              "oid": 72,
              "pid": 47,
              "cid": 45,
              "rtyp": 1
              "value": 3,
              "active": true,
            },
            {
              "oid": 73,
              "pid": 47,
              "cid": 45,
              "rtyp": 7
              "value": 2,
              "active": false,
            }
          ]
        }
      ]
    }
  ]
}
-}