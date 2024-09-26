module Rapanui.Nordnet.CoreJson
  ( JsonAccRule
  , RapanuiResponse
  , rapanuiResponseDecoder
  ) where

import Data.Argonaut.Core (Json)
import Data.Argonaut.Decode as Decode
import Data.Argonaut.Decode.Error (JsonDecodeError)
import Data.Either (Either)

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

type RapanuiResponse =
  { payload ::
      { accRule :: JsonAccRule
      }
  , status :: Int
  , msg :: String
  }

rapanuiResponseDecoder :: Json -> Either JsonDecodeError RapanuiResponse
rapanuiResponseDecoder = Decode.decodeJson