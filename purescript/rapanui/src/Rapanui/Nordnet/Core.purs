module Rapanui.Nordnet.Core where

-- import Prelude

import Data.Either (Either)
import Effect.Aff (Aff)
import HarborView.Common (HarborViewError)
import HarborView.Util.HttpUtil as HU
import Rapanui.Nordnet.CoreJson (DefaultResponse, CritterResponse)
import Rapanui.Nordnet.CoreJson as CoreJson

demo :: Aff (Either HarborViewError CritterResponse)
demo =
  HU.get
    "http://localhost:8082/critter/purchase/11"
    CoreJson.critterResponseDecoder
