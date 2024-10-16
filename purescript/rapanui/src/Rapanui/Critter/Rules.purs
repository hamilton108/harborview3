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

type StockOptionPurchase =
  { ticker :: OptionTicker
  , oid :: Oid
  , price :: Number
  , critters :: Array Critter
  }

--derive instance genericSomeType :: Generic StockOptionPurchase _

--instance showStockOptionPurchase :: Show StockOptionPurchase where
--  show = genericShow
