module HarborView.OptionPurchase.View.Table
  ( purchaseTable
  ) where

import Prelude

import Data.Int as DI
import Data.Number.Format as Format
import Halogen.HTML (HTML, ClassName(..))
import Halogen.HTML as HH
import Halogen.HTML.Properties (IProp)
import Halogen.HTML.Properties as HP
import Halogen.Query.Input (RefLabel(..))
import HarborView.Common as Common
import HarborView.OptionPurchase.Types (Purchase, Purchases)
import HarborView.UI (Title(..))
import HarborView.UI as UI
import Web.UIEvent.MouseEvent (MouseEvent)

--import HarborView.OptionPurchase.Command (Action, Field(..))

noSort ∷ ∀ r i. Array (IProp (class ∷ String | r) i)
noSort =
  [ HP.classes [ ClassName "no-sort " ] ]

header :: forall w i. HTML w i
header =
  HH.thead_
    [ HH.tr_
        --[ HH.th [ HP.attr (AttrName "data-sortable") "false" ] [ HH.text "Sell" ]
        [ HH.th noSort [ HH.text "Sell" ]
        , HH.th noSort [ HH.text "Oid" ]
        , HH.th noSort [ HH.text "Stock" ]
        , HH.th noSort [ HH.text "Option Type" ]
        , HH.th noSort [ HH.text "Ticker" ]
        , HH.th noSort [ HH.text "Purchase Date" ]
        , HH.th noSort [ HH.text "Expiry" ]
        , HH.th noSort [ HH.text "Days" ]
        , HH.th noSort [ HH.text "Purchase Price" ]
        , HH.th noSort [ HH.text "Bid" ]
        , HH.th noSort [ HH.text "Spot" ]
        , HH.th noSort [ HH.text "Purchase vol." ]
        , HH.th noSort [ HH.text "Sales vol." ]
        ]
    ]

row :: forall w i. (Purchase -> MouseEvent -> i) -> Purchase -> HTML w i
row evt p =
  let
    oid = Format.toString $ DI.toNumber p.oid
    days = Format.toString $ DI.toNumber p.days
    price = Common.numToString p.price
    bid = Common.numToString p.bid
    spot = Common.numToString p.spot
    pvol = Format.toString $ DI.toNumber p.pvol
    svol = Format.toString $ DI.toNumber p.svol
  in
    HH.tr_
      [ UI.mkButton2 (Title "Sell") (evt p)
      -- HH.a [ HP.ref (RefLabel "jaada") ] [ HH.text "Sell" ]
      , HH.td_ [ HH.text oid ]
      , HH.td_ [ HH.text p.stock ]
      , HH.td_ [ HH.text p.ot ]
      , HH.td_ [ HH.text p.ticker ]
      , HH.td_ [ HH.text p.pdate ]
      , HH.td_ [ HH.text p.exp ]
      , HH.td_ [ HH.text days ]
      , HH.td_ [ HH.text price ]
      , HH.td_ [ HH.text bid ]
      , HH.td_ [ HH.text spot ]
      , HH.td_ [ HH.text pvol ]
      , HH.td_ [ HH.text svol ]
      ]

purchaseTable :: forall w i. (Purchase -> MouseEvent -> i) -> Purchases -> HTML w i
purchaseTable evt purchases =
  let
    rows = map (row evt) purchases
  in
    HH.table
      [ HP.classes [ ClassName "table", ClassName "sortable" ] ]
      [ header
      , HH.tbody_
          rows
      ]