module Rapanui.Critter.Rules where

import Rapanui.Common (Cid, Oid, OptionTicker, Pid, Rtyp)

--import Prelude

type AcceptRule =
  { oid :: Oid
  , pid :: Pid
  , cid :: Cid
  , rtyp :: Rtyp
  , value :: Number
  , active :: Boolean
  }

type Critter =
  { oid :: Oid
  , vol :: Int
  , status :: Int
  , accRules :: Array AcceptRule
  }

type StockOption =
  { ticker :: OptionTicker
  , oid :: Oid
  , price :: Number
  , critters :: Array Critter
  }