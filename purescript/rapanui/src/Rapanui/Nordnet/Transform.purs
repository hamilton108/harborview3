module Rapanui.Nordnet.Transform
  ( map
  ) where

import Prelude

import Data.Maybe (Maybe(..))
import Rapanui.Common (Oid(..), Pid(..), Cid(..), Rtyp(..))
import Rapanui.Critter.AcceptRule (AcceptRule)
import Rapanui.Nordnet.CoreJson (CritterResponse, JsonAccRule)
import Rapanui.State (State)

mapAccRule :: JsonAccRule -> AcceptRule
mapAccRule { oid, pid, cid, rtyp, value, active } =
  { oid: Oid oid
  , pid: Pid pid
  , cid: Cid cid
  , rtyp: Rtyp rtyp
  , value: value
  , active: active
  }

map :: CritterResponse -> State
map response =
  { accRule: Just $ mapAccRule response.payload.accRule }
