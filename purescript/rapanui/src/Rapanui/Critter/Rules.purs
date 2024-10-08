module Rapanui.Critter.Rules where

import Rapanui.Common (Oid, Pid, Cid, Rtyp)

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