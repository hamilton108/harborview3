module Rapanui.Nordnet.Transform
  ( mapCritter
  , mapPayloads
  ) where

import Prelude

import Rapanui.Common (Oid(..), Pid(..), Cid(..), Rtyp(..), OptionTicker(..))
import Rapanui.Critter.Rules (AcceptRule, Critter, StockOption)
import Rapanui.Nordnet.CoreJson (JsonCritter, JsonPayload, JsonAccRule)

--import Rapanui.State (State)

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

mapAccRule :: JsonAccRule -> AcceptRule
mapAccRule ja =
  { active: true
  , cid: Cid ja.cid
  , oid: Oid ja.oid
  , pid: Pid ja.pid
  , rtyp: Rtyp ja.rtyp
  , value: ja.value
  }

mapCritter :: JsonCritter -> Critter
mapCritter jc =
  { oid: Oid jc.oid
  , vol: jc.vol
  , status: jc.status
  , accRules: map mapAccRule jc.accRules
  }

mapCritters :: Array JsonCritter -> Array Critter
mapCritters critters =
  map mapCritter critters

mapPayload :: JsonPayload -> StockOption
mapPayload payload =
  { oid: Oid payload.oid
  , ticker: OptionTicker payload.ticker
  , price: payload.price
  , critters: mapCritters payload.critters
  }

mapPayloads :: Array JsonPayload -> Array StockOption
mapPayloads payloads =
  map mapPayload payloads
--traverse mapCritter payload.critters

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