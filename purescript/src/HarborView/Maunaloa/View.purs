module HarborView.Maunaloa.View where


-- import Data.Tuple ( Tuple(..) )
import Effect.Class (class MonadEffect)
import Effect.Aff.Class (class MonadAff)
import Data.Maybe (Maybe(..))
import HarborView.Maunaloa.Common 
  ( ChartType 
  , Drop(..)
  , Take(..)
  , StockTicker(..)
  , chartTypeAsInt
  )
import HarborView.UI as UI
import HarborView.UI  ( Title(..)
                      , InputVal(..)
                      , SelectItem
                      , SelectItems
                      )
import HarborView.Maunaloa.Core as Core
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
import Halogen.HTML.Events as HE

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
  , selectedTicker :: SelectItem
  }

mkTickers :: SelectItems
mkTickers = 
  [ { v: "18", t: "AKSO - Aker Solutions" } 
  , { v: "3", t: "YAR - Yara" } 
  ]

data Action 
  = SelectChange String
  | Initialize
  | AddLevelLine MouseEvent

component :: forall q i o m. MonadAff m => ChartType -> H.Component q i o m
component c =
  H.mkComponent
    { initialState: \_ -> { tickers: mkTickers 
                          , ct: c
                          , selectedTicker: UI.emptySelectItem
                          }
    , render
    , eval: H.mkEval H.defaultEval 
      { handleAction = handleAction 
      , initialize = Just Initialize
      }
    }

mainClass :: ClassName
mainClass = ClassName "grid-menu-bar-ps"

menuBarClass :: ClassName 
menuBarClass = ClassName "form-group form-group--menu-bar" 

type Icon = 
  { iconClass :: String
  , htmlId :: String
  , title :: String 
  } 

icon :: forall w i. Icon -> (MouseEvent -> i) -> HTML w i
icon { iconClass, htmlId, title } evt = 
  let 
    cn = "fa-solid " <> iconClass <> " fa-fw"
  in
  HH.span 
    [ HP.classes [ ClassName "scrap-span" ]]
    [ HH.i 
        [ HE.onClick evt,  HP.classes [ ClassName cn ], HP.title title ]
        []
    ]


resetChart :: Icon
resetChart = 
  { iconClass: "fa-ghost", htmlId: "", title: "Reset Chart" }

arrowRight :: Icon 
arrowRight =
  { iconClass: "fa-arrow-right", htmlId: "", title: "Next" }

arrowLeft :: Icon 
arrowLeft = 
  { iconClass: "fa-arrow-left", htmlId: "", title: "Previous" }

arrowLast :: Icon
arrowLast = 
  { iconClass: "fa-arrow-right-to-bracket", htmlId: "", title: "Last" }

levelLine :: Icon
levelLine = 
  { iconClass: "fa-ruler-vertical", htmlId: "", title: "Level Line" }

persistentLevelLine :: Icon
persistentLevelLine = 
  { iconClass: "fa-pen-ruler", htmlId: "", title: "Persistent Level Line" }

deleteLevelLine :: Icon
deleteLevelLine = 
  { iconClass: "fa-ruler-combined", htmlId: "", title: "Delete Level Line" }


render :: forall cs m. State -> H.ComponentHTML Action cs m
render st =
  let
    tickers = UI.mkSelect_ st.tickers SelectChange 
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
    [ -- icon resetChart DemoMouse
    -- , icon arrowLeft 
    -- , icon arrowRight 
    -- , icon arrowLast
    icon levelLine AddLevelLine
    -- , icon persistentLevelLine 
    -- , icon deleteLevelLine 
    ]
  ]

handleAction :: forall cs o m. MonadAff m => Action -> H.HalogenM State Action cs o m Unit       
handleAction = case _ of
  SelectChange s -> 
    H.gets _.ct >>= \ct1 ->
      let 
        ticker = StockTicker s
      in 
      if s == "0" then
        liftEffect (Core.paintEmpty ct1) *>
        pure unit
      else
        liftEffect (Core.paint ct1 (StockTicker s) (Drop 0) (Take 90)) *>
        pure unit
  Initialize -> 
    H.gets _.ct >>= \ct1 ->
      liftEffect (logShow $ chartTypeAsInt ct1) *>
      liftEffect (Core.initEvents ct1) *>
      pure unit
  AddLevelLine _ -> 
    H.gets _.ct >>= \ct1 ->
      liftEffect (Core.addLevelLine ct1)

