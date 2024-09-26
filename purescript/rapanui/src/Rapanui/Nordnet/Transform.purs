module Rapanui.Nordnet.Transform
  ( map
  ) where

import Rapanui.Common (Oid(..), Pid(..), Cid(..), Rtyp(..))
import Rapanui.Critter.AcceptRule (AcceptRule)
import Rapanui.Nordnet.CoreJson (RapanuiResponse, JsonAccRule)
import Rapanui.State (State)

--import Prelude

mapAccRule :: JsonAccRule -> AcceptRule
mapAccRule { oid, pid, cid, rtyp, value, active } =
  { oid: Oid oid
  , pid: Pid pid
  , cid: Cid cid
  , rtyp: Rtyp rtyp
  , value: value
  , active: active
  }

map :: RapanuiResponse -> State
map response =
  { accRule: mapAccRule response.payload.accRule }
