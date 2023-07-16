module HarborView.Maunaloa.View where


import Effect.Class (class MonadEffect)
import Effect.Aff.Class (class MonadAff)
import HarborView.Maunaloa.Common ( ChartType )
import HarborView.UI as UI
import HarborView.UI  ( Title(..)
                      , InputVal(..)
                      , SelectItem
                      , SelectItems
                      )
import Web.UIEvent.MouseEvent (MouseEvent)
import Web.UIEvent.MouseEvent as ME
import Web.Event.Event as E
import DOM.HTML.Indexed.InputType (InputType(..))
import Halogen as H
import Halogen.HTML.Properties as HP
import Halogen.HTML as HH
import Halogen.HTML ( HTML
                    , ClassName(..)
                    )

import Effect.Console (logShow)
import Effect.Class (liftEffect)

import Prelude

{- <select class="form-control" id="tickers-1">
  <option>-</option>
  <option value="18">AKSO - Aker Solutions</option>
  <option value="27">BAKKA - Bakkafrost</option>
  <option value="26">BWLPG - BW LPG</option>
  <option value="19">DNB - DNB</option>
  <option value="20">DNO - DNO International</option>
  <option value="2">EQNR - Equinor</option>
  <option value="21">GJF - Gjensidige Forsikr</option>
  <option value="28">GOGL - Golden Ocean Group</option>
  <option value="29">NAS - Norw. Air Shuttle</option>
  <option value="1">NHY - Norsk hydro</option>
  <option value="9">ORK - Orkla</option>
  <option value="12">PGS - Petroleum Geo-Serv</option>
  <option value="14">STB - Storebrand</option>
  <option value="23">SUBC - Subsea 7</option>
  <option value="6">TEL - Telenor</option>
  <option value="16">TGS - TGS-NOPEC Geophysica</option>
  <option value="17">TOM - Tomra</option>
  <option value="3">YAR - Yara</option>
</select>-}
 
type State = 
  { tickers :: SelectItems
  , ct :: ChartType
  }

mkTickers :: SelectItems
mkTickers = 
  [ { v: "18", t: "AKSO - Aker Solutions" } 
  , { v: "3", t: "YAR - Yara" } 
  ]

data Action 
  = Noop MouseEvent
  | SelectChange String

component :: forall q i o m. MonadAff m => ChartType -> H.Component q i o m
component c =
  H.mkComponent
    { initialState: \_ -> { tickers: mkTickers 
                          , ct : c
                          }
    , render
    , eval: H.mkEval H.defaultEval { handleAction = handleAction }
    }

mainClass :: ClassName
mainClass = ClassName "grid-menu-bar-ps"

menuBarClass :: ClassName 
menuBarClass = ClassName "form-group form-group--menu-bar" 

tmp = 
      HH.span 
        [ HP.classes [ ClassName "scrap-span" ]]
        [ HH.i 
            [ HP.classes [ ClassName "fa-solid fa-arrow-right fa-fw" ]]
            []
        ]

render :: forall cs m. State -> H.ComponentHTML Action cs m
render st =
  let
    tickers = UI.mkSelectSimple st.tickers SelectChange 
  in
  HH.div
  [ HP.classes [ mainClass ]]
  [
    HH.div 
    [ HP.classes [ menuBarClass ]]
    [ tickers
    ]
    , HH.div 
    [ HP.classes [ menuBarClass ]]
    [ tmp 
    , tmp 
    , tmp 
    ]
    -- , HH.p_ [ HH.text st.chart ]
  ]


handleAction :: forall cs o m. MonadAff m => Action -> H.HalogenM State Action cs o m Unit       
handleAction = case _ of
  (Noop e) -> pure unit
  (SelectChange s) -> 
    liftEffect (logShow s) *>
    pure unit

