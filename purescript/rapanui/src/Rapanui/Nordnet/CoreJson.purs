module Rapanui.Nordnet.CoreJson
  ( CritterResponse
  , DefaultResponse
  , JsonAccRule
  , JsonCritter
  , JsonPayload
  , critterResponseDecoder
  , defaultResponseDecoder
  ) where

import Data.Argonaut.Core (Json)
import Data.Argonaut.Decode as Decode
import Data.Argonaut.Decode.Error (JsonDecodeError)
import Data.Either (Either)
import Data.Maybe (Maybe)

--import Data.Maybe (Maybe)

--import Prelude

type JsonAccRule =
  { oid :: Int
  , pid :: Int
  , cid :: Int
  , rtyp :: Int
  , value :: Number
  , active :: Boolean
  }

type JsonCritter =
  { vol :: Int
  , status :: Int
  , oid :: Int
  , accRules :: Array JsonAccRule
  }

type JsonPayload =
  { ticker :: String
  , oid :: Int
  , price :: Number
  , critters :: Array JsonCritter
  }

type CritterResponse =
  { appStatusCode :: Int
  , error :: Maybe String
  , payload :: Array JsonPayload
  }

critterResponseDecoder :: Json -> Either JsonDecodeError CritterResponse
critterResponseDecoder = Decode.decodeJson

type DefaultResponse =
  { status :: Int
  , error :: String
  }

defaultResponseDecoder :: Json -> Either JsonDecodeError DefaultResponse
defaultResponseDecoder = Decode.decodeJson

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